package dat250.FeedApp.manager

import dat250.FeedApp.domain.User
import dat250.FeedApp.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FeedAppManager(private val userRepo: UserRepository) {
    fun findUser(username: String): User? = userRepo.findByIdOrNull(username)

    fun saveUser(user: User): User = userRepo.save(user)
}
