package dat250.FeedApp.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

class PollTest {
    @Test
    fun `poll is created with unique id and provided data`() {
        val now = Instant.now()
        val validUntil = now.plusSeconds(3600)
		val user = User("testuser")
        val poll = Poll(
            question = "What's your favorite color?",
            publishedAt = now,
            validUntil = validUntil,
			creator = user,
        )

        assertNotNull(poll.id)
        assertEquals("What's your favorite color?", poll.question)
        assertEquals(now, poll.publishedAt)
        assertEquals(validUntil, poll.validUntil)
        assertTrue(poll.voteOptions.isEmpty())
    }

    @Test
    fun `poll can have vote options added`() {
		val user = User("testuser")
        val poll = Poll(
            question = "Pick a number",
            publishedAt = Instant.now(),
            validUntil = Instant.now(),
			creator = user,
        )

        VoteOption(caption = "1", poll = poll)
        VoteOption(caption = "2", poll = poll)

        assertEquals(2, poll.voteOptions.size)
    }
}
