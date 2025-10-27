package dat250.FeedApp.repository

import dat250.FeedApp.domain.VoteOption
import dat250.FeedApp.domain.VoteOptionCount
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface VoteOptionRepository : CrudRepository<VoteOption, String> {
    // new dat250.FeedApp.domain.
    // SELECT VoteOptionCount(o.caption, COUNT(v.id))
    @Query(
            """
        SELECT new dat250.FeedApp.domain.VoteOptionCount(o.caption, COUNT(v.id))
        FROM VoteOption o
        LEFT JOIN Vote v ON o.id = v.voteOption.id
        WHERE o.poll.id = :pollId
        GROUP BY o.caption
        ORDER BY o.caption
    """
    )
    fun findVoteOptionCountsByPollId(@Param("pollId") pollId: Long): List<VoteOptionCount>

    fun findByPollIdAndCaption(pollId: Long, caption: String): VoteOption?
}
