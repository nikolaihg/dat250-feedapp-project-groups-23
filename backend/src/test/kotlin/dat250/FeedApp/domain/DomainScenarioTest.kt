package dat250.FeedApp.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

// Based on the same scenario as in expass2 TDD task
class DomainScenarioTest {
    @Test
    fun `complete user poll voting scenario`() {
        val users = mutableListOf<User>()
        val polls = mutableListOf<Poll>()
        val allVotes = mutableListOf<Vote>()

        // Create a new user
        val user1 = User(username = "alice")
        users.add(user1)
        assertEquals(1, users.size)
        assertEquals("alice", users[0].username)

        // List all users (shows the newly created user)
        assertEquals(1, users.size)

        // Create another user
        val user2 = User(username = "bob")
        users.add(user2)

        // List all users again (shows two users)
        assertEquals(2, users.size)
        assertEquals("alice", users[0].username)
        assertEquals("bob", users[1].username)

        // User 1 creates a new poll
        val now = Instant.now()
        val poll = Poll(
            question = "Best Kotlin feature?",
            publishedAt = now,
            validUntil = now.plusSeconds(86400)
        )
        poll.voteOptions.add(VoteOption(caption = "Null safety"))
        poll.voteOptions.add(VoteOption(caption = "Extension functions"))
        user1.polls.add(poll)
        polls.add(poll)

        // List polls (shows the new poll)
        assertEquals(1, polls.size)
        assertEquals("Best Kotlin feature?", polls[0].question)
        assertEquals(2, polls[0].voteOptions.size)

        // User 2 votes on the poll
        var vote1 = Vote(publishedAt = now, voteOption = poll.voteOptions[0])
        vote1 = vote1.copy(user = user2)
        user2.votes.add(vote1)
        allVotes.add(vote1)
        assertEquals(1, allVotes.size)

        // User 2 changes his vote
        var vote2 = Vote(publishedAt = now.plusSeconds(60), voteOption = poll.voteOptions[1])
        vote2 = vote2.copy(user = user2)
        user2.votes.add(vote2)
        allVotes.add(vote2)
        assertEquals(2, allVotes.size)

        // List votes (shows the most recent vote for User 2)
        val user2Votes = user2.votes
        assertEquals(2, user2Votes.size)
        val mostRecentVote = user2Votes.maxByOrNull { it.publishedAt }
        assertEquals("Extension functions", mostRecentVote?.voteOption?.caption)

        // Delete the one poll
        polls.removeAll { it.id == poll.id }
        assertEquals(0, polls.size)

        // List votes (empty)
        allVotes.removeAll { it.voteOption.poll?.id == poll.id }
        assertEquals(2, allVotes.size) // Votes still exist

        // But if we filter by valid polls, they're orphaned
        val validVotes = allVotes.filter { vote -> polls.any { p -> p.voteOptions.contains(vote.voteOption) } }
        assertEquals(0, validVotes.size)
    }
}