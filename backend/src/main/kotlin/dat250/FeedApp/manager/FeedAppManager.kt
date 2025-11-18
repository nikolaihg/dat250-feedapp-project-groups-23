package dat250.FeedApp.manager

import dat250.FeedApp.controller.PollController
import dat250.FeedApp.controller.VoteController
import dat250.FeedApp.domain.Poll
import dat250.FeedApp.domain.User
import dat250.FeedApp.domain.Vote
import dat250.FeedApp.domain.VoteOption
import dat250.FeedApp.domain.VoteOptionCount
import dat250.FeedApp.messaging.FeedEventPublisher
import dat250.FeedApp.messaging.PollCreatedEvent
import dat250.FeedApp.messaging.VoteCastEvent
import dat250.FeedApp.repository.PollRepository
import dat250.FeedApp.repository.UserRepository
import dat250.FeedApp.repository.VoteOptionRepository
import dat250.FeedApp.repository.VoteRepository
import java.time.Instant
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FeedAppManager(
        private val userRepo: UserRepository,
        private val pollRepo: PollRepository,
        private val voteOptionRepo: VoteOptionRepository,
        private val voteRepo: VoteRepository,
        private val eventPublisher: FeedEventPublisher
) {
    fun findUser(username: String): User? = userRepo.findByIdOrNull(username)

    fun saveUser(user: User): User = userRepo.save(user)

    fun findAllPolls(): List<Poll> = pollRepo.findAll().toList()

    fun addPoll(username: String, pollRequest: PollController.PollRequest): Poll? {
        val user = findUser(username)
        if (user == null) {
            return null
        }
        val poll =
                pollRepo.save(
                        Poll(
                                question = pollRequest.question,
                                publishedAt = Instant.now(),
                                validUntil = pollRequest.validUntil,
                                creator = user,
                        )
                )
        for (vo in pollRequest.voteOptions) {
            voteOptionRepo.save(VoteOption(caption = vo, poll = poll))
        }
        eventPublisher.publishPollCreated(
                PollCreatedEvent(
                        pollId = poll.id,
                        question = poll.question,
                        publishedAt = poll.publishedAt,
                        validUntil = poll.validUntil,
                        creator = user.username,
                        voteOptions = pollRequest.voteOptions
                )
        )
        return poll
    }

    @Caching(
            evict = [
                CacheEvict(cacheNames = ["pollVotes"], key = "#pollId"),
                CacheEvict(cacheNames = ["pollVoteCounts"], key = "#pollId")
            ]
    )
    fun deletePoll(pollId: Long) = pollRepo.deleteById(pollId)

    @Caching(
            evict = [
                CacheEvict(cacheNames = ["pollVotes"], key = "#voteRequest.pollId"),
                CacheEvict(cacheNames = ["pollVoteCounts"], key = "#voteRequest.pollId")
            ]
    )
    fun castVote(username: String, voteRequest: VoteController.VoteRequest): Vote? {
        val user = findUser(username)
        if (user == null) {
            return null
        }
        val vo = voteOptionRepo.findByPollIdAndCaption(voteRequest.pollId, voteRequest.caption)
        if (vo == null) {
            return null
        }
        val vote = voteRepo.save(Vote(publishedAt = Instant.now(), voteOption = vo, user = user))
        eventPublisher.publishVoteCast(
                VoteCastEvent(
                        pollId = vo.poll.id,
                        voteOption = vo.caption,
                        voter = user.username,
                        publishedAt = vote.publishedAt
                )
        )
        return vote
    }

    @Cacheable(cacheNames = ["pollVotes"], key = "#id")
    fun getVotesForPoll(id: Long): List<Vote> = voteRepo.findAllVotesByPollId(id)

    @Cacheable(cacheNames = ["pollVoteCounts"], key = "#id")
    fun getVoteOptionCountForPoll(id: Long): List<VoteOptionCount> =
            voteOptionRepo.findVoteOptionCountsByPollId(id)
}
