package dat250.FeedApp.repository

import dat250.FeedApp.domain.Poll
import org.springframework.data.repository.CrudRepository

interface PollRepository : CrudRepository<Poll, Long>
