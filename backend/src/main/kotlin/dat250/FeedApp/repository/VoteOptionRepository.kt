package dat250.FeedApp.repository

import dat250.FeedApp.domain.VoteOption
import org.springframework.data.repository.CrudRepository

interface VoteOptionRepository : CrudRepository<VoteOption, String> {
    // @Query(
    //         """
    //     SELECT new dat250.FeedApp.domain.VoteOptionCount(o.caption, COUNT(v.id))
    //     FROM VoteOption o
    //     LEFT JOIN Vote v ON o.id = v.votesOn.id
    //     WHERE o.poll.id = :pollId
    //     GROUP BY o.caption
    //     ORDER BY o.caption
    // """
    // )
    // fun findVoteOptionCountsByPollId(@Param("pollId") pollId: Long): List<VoteOptionCount>

    fun findByPollIdAndCaption(pollId: Long, caption: String): VoteOption?

    data class VoteOptionCount(val caption: String, val voteCount: Long)
}
