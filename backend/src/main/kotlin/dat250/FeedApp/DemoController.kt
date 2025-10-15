package dat250.FeedApp

import java.security.Principal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController {
    @GetMapping("/demo")
    fun getUserData(principal: Principal): String {
        val oauth2User = (principal as OAuth2AuthenticationToken).principal as OAuth2User

        val userName: String? = oauth2User.getAttribute<String>("login")
        if (userName != null) {
            return userName
        }
        return "Error username not found"
    }
}
