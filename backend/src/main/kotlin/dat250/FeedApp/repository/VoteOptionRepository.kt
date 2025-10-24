package dat250.FeedApp.repository

import dat250.FeedApp.domain.VoteOption
import org.springframework.data.repository.CrudRepository

interface VoteOptionRepository : CrudRepository<VoteOption, String>
