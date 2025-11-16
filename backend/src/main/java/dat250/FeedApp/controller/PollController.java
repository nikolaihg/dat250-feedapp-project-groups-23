package dat250.FeedApp.controller;

import dat250.FeedApp.domain.Poll;
import dat250.FeedApp.domain.Vote;
import dat250.FeedApp.domain.VoteOptionCount;
import dat250.FeedApp.manager.FeedAppManager;
import io.swagger.v3.oas.annotations.Operation;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/polls")
public class PollController {

	private FeedAppManager feedAppManager;

	PollController(@Autowired FeedAppManager feedAppManager) {
		this.feedAppManager = feedAppManager;
	}

	@GetMapping
	@Operation(summary = "Get all polls", description = "Returns list of all polls")
	ResponseEntity<List<Poll>> getAllPolls() {
		return ResponseEntity.ok(feedAppManager.findAllPolls());
	}

	@PostMapping
	@Operation(summary = "Create polls", description = "Returns newly created poll")
	public ResponseEntity<Poll> createPolls(
			@AuthenticationPrincipal OAuth2User oauth2User,
			@RequestBody PollRequest pollRequest) {
		String username = oauth2User.getAttribute("login");
		if (username == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Poll poll = feedAppManager.addPoll(username, pollRequest);
		if (poll != null) {
			return ResponseEntity.ok(poll);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete poll", description = "Delete a poll by the poll id")
	public ResponseEntity<Void> deletePoll(@PathVariable("id") Long id) {
		feedAppManager.deletePoll(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{id}/votes")
	@Operation(summary = "Get votes for poll", description = "Returns a list of all votes on a poll")
	public ResponseEntity<List<Vote>> getAllVotes(@PathVariable("id") Long id) {
		List<Vote> votes = feedAppManager.getVotesForPoll(id);
		return ResponseEntity.ok(votes);
	}

	@GetMapping("/{id}/voteCount")
	@Operation(summary = "Get votes per vote options for poll", description = "Returns list of vote options with count of votes per option")
	public ResponseEntity<List<VoteOptionCount>> getAllVoteOptions(@PathVariable("id") Long id) {
		List<VoteOptionCount> voteOptionCounts = feedAppManager.getVoteOptionCountForPoll(id);
		return ResponseEntity.ok(voteOptionCounts);
	}

	public record PollRequest(
			String question,
			Instant validUntil,
			List<String> voteOptions) {
	}

}
