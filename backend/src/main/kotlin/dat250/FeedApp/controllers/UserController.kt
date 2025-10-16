package dat250.FeedApp.controllers

import java.security.Principal
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/v1/user")
class UserController {

	@GetMapping
	@Operation(summary = "Get current user", description = "Returns user information of currently logged in user")
	fun getUserData(principal: Principal): ResponseEntity<String> {
		val oauth2User = (principal as OAuth2AuthenticationToken).principal as OAuth2User

		val userName: String? = oauth2User.getAttribute<String>("login")
		if (userName != null) {
			// TODO: Database lookup
			return ResponseEntity.ok("{\"username\": \"$userName\"}");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
	}
}
