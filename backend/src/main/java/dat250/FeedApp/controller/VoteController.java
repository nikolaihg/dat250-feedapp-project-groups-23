package dat250.FeedApp.controller;

import dat250.FeedApp.domain.Vote;
import dat250.FeedApp.manager.FeedAppManager;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {

	private FeedAppManager feedAppManager;

	VoteController(@Autowired FeedAppManager feedAppManager) {
		this.feedAppManager = feedAppManager;
	}

	@PostMapping
	@Operation(summary = "Create vote", description = "Creates a vote on a given poll for the user")
	ResponseEntity<Vote> createPolls(
			@AuthenticationPrincipal OAuth2User oauth2User,
			@RequestBody VoteRequest voteRequest) {
		String username = oauth2User.getAttribute("login");
		if (username == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Vote vote = feedAppManager.castVote(username, voteRequest);
		if (vote != null) {
			return ResponseEntity.ok(vote);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	public record VoteRequest(Long pollId, String caption) {
	}
}
