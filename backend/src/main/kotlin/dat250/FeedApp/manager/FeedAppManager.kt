package dat250.FeedApp.manager

import dat250.FeedApp.controller.PollController
import dat250.FeedApp.domain.Poll
import dat250.FeedApp.domain.User
import dat250.FeedApp.domain.VoteOption
import dat250.FeedApp.repository.PollRepository
import dat250.FeedApp.repository.UserRepository
import dat250.FeedApp.repository.VoteOptionRepository
import java.time.Instant
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FeedAppManager(
        private val userRepo: UserRepository,
        private val pollRepo: PollRepository,
        private val voteOptionRepo: VoteOptionRepository
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
        return poll
    }

    fun deletePoll(pollId: Long) {
        pollRepo.deleteById(pollId)
    }
}
