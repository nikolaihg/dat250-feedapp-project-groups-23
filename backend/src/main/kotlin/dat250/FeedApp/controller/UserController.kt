package dat250.FeedApp.controller

import dat250.FeedApp.domain.User
import dat250.FeedApp.manager.FeedAppManager
import io.swagger.v3.oas.annotations.Operation
import java.security.Principal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val feedAppManager: FeedAppManager) {

    @GetMapping
    @Operation(
            summary = "Get current user",
            description = "Returns user information of currently logged in user"
    )
    fun getUserData(principal: Principal): ResponseEntity<User> {
        val oauth2User = (principal as OAuth2AuthenticationToken).principal as OAuth2User

        val userName: String? = oauth2User.getAttribute<String>("login")
        if (userName != null) {
            var user = feedAppManager.findUser(userName)
            // create user in our db if does not exist
            if (user == null) {
                user = User(userName)
                feedAppManager.saveUser(user)
            }
            return ResponseEntity.ok(user)
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}
