package dat250.FeedApp.controller

import dat250.FeedApp.domain.Vote
import dat250.FeedApp.manager.FeedAppManager
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/votes")
class VoteController(private val feedAppManager: FeedAppManager) {
    @GetMapping("/polls/{id}")
    @Operation(
            summary = "Get votes for poll",
            description = "Returns a list of all votes on a poll"
    )
    fun getAllVotes(@PathVariable("id") id: Long): ResponseEntity<List<Vote>> {
        return ResponseEntity.ok(feedAppManager.getVotesForPoll(id))
    }

    @PostMapping
    @Operation(summary = "Create vote", description = "Creates a vote on a given poll for the user")
    fun createPolls(
            @AuthenticationPrincipal oauth2User: OAuth2User,
            @RequestBody voteRequest: VoteRequest
    ): ResponseEntity<Vote> {
        val username = oauth2User.getAttribute<String>("login")
        if (username == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        return feedAppManager.castVote(username, voteRequest)?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    public data class VoteRequest(val pollId: Long, val caption: String)
}
