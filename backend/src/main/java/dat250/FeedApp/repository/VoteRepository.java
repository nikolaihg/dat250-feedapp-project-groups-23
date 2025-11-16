package dat250.FeedApp.repository;

import dat250.FeedApp.domain.Vote;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends CrudRepository<Vote, String> {
	@Query("""
			    SELECT v
			    FROM Vote v
			    JOIN v.voteOption o
			    WHERE o.poll.id = :pollId
			""")
	List<Vote> findAllVotesByPollId(@Param("pollId") Long pollId);
}
