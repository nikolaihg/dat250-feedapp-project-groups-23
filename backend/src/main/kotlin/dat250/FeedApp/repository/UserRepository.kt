package dat250.FeedApp.repository

import dat250.FeedApp.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String>
