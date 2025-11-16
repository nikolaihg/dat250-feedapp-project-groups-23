package dat250.FeedApp.controller;

import dat250.FeedApp.domain.User;
import dat250.FeedApp.manager.FeedAppManager;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
class UserController {

	private FeedAppManager feedAppManager;

	UserController(@Autowired FeedAppManager feedAppManager) {
		this.feedAppManager = feedAppManager;
	}

	@GetMapping
	@Operation(summary = "Get current user", description = "Returns user information of currently logged in user")
	ResponseEntity<User> getUserData(@AuthenticationPrincipal OAuth2User oauth2User) {
		String userName = oauth2User.getAttribute("login");
		if (userName != null) {
			var user = feedAppManager.findUser(userName);
			// create user in our db if does not exist
			if (user.isEmpty()) {
				user = Optional.of(new User(userName));
				feedAppManager.saveUser(user.get());
			}
			return ResponseEntity.ok(user.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
