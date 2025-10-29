package dat250.FeedApp.repository

import dat250.FeedApp.domain.Vote
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface VoteRepository : CrudRepository<Vote, String> {
    @Query(
            """
        SELECT v 
        FROM Vote v 
        JOIN v.voteOption o 
        WHERE o.poll.id = :pollId
    """
    )
    fun findAllVotesByPollId(@Param("pollId") pollId: Long): List<Vote>
}
