package dat250.FeedApp.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant


class UserTest {
    @Test
    fun `user can be created with username and empty collections`() {
        val user = User(username = "john_doe")

        assertEquals("john_doe", user.username)
        assertTrue(user.polls.isEmpty())
        assertTrue(user.votes.isEmpty())
    }

    @Test
    fun `user can have polls and votes added`() {
        val user = User(username = "jane_smith")
        val poll = Poll(question = "Favorite language?", publishedAt = Instant.now(), validUntil = Instant.now(), creator = user)
        Vote(publishedAt = Instant.now(), voteOption = VoteOption(caption = "Kotlin", poll = poll), user = user)

        assertEquals(1, user.polls.size)
        assertEquals(1, user.votes.size)
    }
}
