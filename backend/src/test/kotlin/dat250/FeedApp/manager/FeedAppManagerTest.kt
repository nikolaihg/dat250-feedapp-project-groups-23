package dat250.FeedApp.manager

import dat250.FeedApp.controller.*
import dat250.FeedApp.domain.*
import dat250.FeedApp.repository.*
import java.time.Instant
import java.util.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class FeedAppManagerTest {

    @Autowired lateinit var feedAppManager: FeedAppManager

    @Autowired lateinit var userRepo: UserRepository

    @Autowired lateinit var pollRepo: PollRepository

    @Autowired lateinit var voteOptionRepo: VoteOptionRepository

    @Autowired lateinit var voteRepo: VoteRepository

    private lateinit var testUser: User

    @BeforeEach
    fun setup() {
        testUser = User(username = "testuser")
        userRepo.save(testUser)
    }

    @Test
    fun `test findUser returns user by username`() {
        val user = feedAppManager.findUser("testuser")
        assertNotNull(user)
        assertEquals("testuser", user?.username)
    }

    @Test
    fun `test saveUser saves user to the repository`() {
        val user =
                feedAppManager.saveUser(
                        User(
                                username = "newuser",
                        )
                )
        assertNotNull(user)
        assertEquals("newuser", user.username)
    }

    @Test
    fun `test addPoll adds a poll and its vote options`() {
        val pollRequest =
                PollController.PollRequest(
                        question = "What is your favorite color?",
                        validUntil = Instant.now().plusSeconds(3600),
                        voteOptions = listOf("Red", "Blue", "Green")
                )

        val poll = feedAppManager.addPoll("testuser", pollRequest)

        assertNotNull(poll)
        assertEquals("What is your favorite color?", poll?.question)
        assertEquals(3, voteOptionRepo.findAll().count())
    }

    @Test
    fun `test deletePoll deletes poll successfully`() {
        val pollRequest =
                PollController.PollRequest(
                        question = "What is your favorite color?",
                        validUntil = Instant.now().plusSeconds(3600),
                        voteOptions = listOf("Red", "Blue", "Green")
                )
        val poll = feedAppManager.addPoll("testuser", pollRequest)
        val pollId = poll?.id ?: throw IllegalStateException("Poll not created")

        feedAppManager.deletePoll(pollId)

        val deletedPoll = pollRepo.findById(pollId)
        assertTrue(deletedPoll.isEmpty())
    }

    @Test
    fun `test castVote casts a vote successfully`() {
        val pollRequest =
                PollController.PollRequest(
                        question = "What is your favorite color?",
                        validUntil = Instant.now().plusSeconds(3600),
                        voteOptions = listOf("Red", "Blue", "Green")
                )
        val poll = feedAppManager.addPoll("testuser", pollRequest)

        val pollId = poll?.id ?: throw IllegalStateException("Poll not created")
        val voteRequest = VoteController.VoteRequest(pollId = pollId, caption = "Red")

        val vote = feedAppManager.castVote("testuser", voteRequest)

        assertNotNull(vote)
        assertEquals("Red", vote?.voteOption?.caption)
        assertEquals("testuser", vote?.user?.username)
    }

    @Test
    fun `test getVotesForPoll returns list of votes for poll`() {
        val pollRequest =
                PollController.PollRequest(
                        question = "What is your favorite color?",
                        validUntil = Instant.now().plusSeconds(3600),
                        voteOptions = listOf("Red", "Blue", "Green")
                )
        val poll = feedAppManager.addPoll("testuser", pollRequest)
        val pollId = poll?.id ?: throw IllegalStateException("Poll not created")

        val voteRequest1 = VoteController.VoteRequest(pollId = pollId, caption = "Red")
        val voteRequest2 = VoteController.VoteRequest(pollId = pollId, caption = "Blue")

        feedAppManager.castVote("testuser", voteRequest1)
        feedAppManager.castVote("testuser", voteRequest2)

        val votes = feedAppManager.getVotesForPoll(pollId)

        assertEquals(2, votes.size)
    }

    @Test
    fun `test getVoteOptionCountForPoll returns count of votes for each option`() {
        val pollRequest =
                PollController.PollRequest(
                        question = "What is your favorite color?",
                        validUntil = Instant.now().plusSeconds(3600),
                        voteOptions = listOf("Red", "Blue", "Green")
                )
        val poll = feedAppManager.addPoll("testuser", pollRequest)
        val pollId = poll?.id ?: throw IllegalStateException("Poll not created")

        val voteRequest1 = VoteController.VoteRequest(pollId = pollId, caption = "Red")
        val voteRequest2 = VoteController.VoteRequest(pollId = pollId, caption = "Blue")

        feedAppManager.castVote("testuser", voteRequest1)
        feedAppManager.castVote("testuser", voteRequest2)

        val counts = feedAppManager.getVoteOptionCountForPoll(pollId)

        assertEquals(3, counts.size) // Three vote options
        assertTrue(counts.any { it.caption == "Red" && it.voteCount == 1L })
        assertTrue(counts.any { it.caption == "Blue" && it.voteCount == 1L })
        assertTrue(counts.any { it.caption == "Green" && it.voteCount == 0L })
    }
}
