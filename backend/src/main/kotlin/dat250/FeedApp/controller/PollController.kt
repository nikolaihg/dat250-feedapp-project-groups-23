package dat250.FeedApp.controller

import dat250.FeedApp.domain.Poll
import dat250.FeedApp.manager.FeedAppManager
import io.swagger.v3.oas.annotations.Operation
import java.time.Instant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/polls")
class PollController(private val feedAppManager: FeedAppManager) {

    @GetMapping
    @Operation(summary = "Get all polls", description = "Returns list of all polls")
    fun getAllPolls(): ResponseEntity<List<Poll>> {
        return ResponseEntity.ok(feedAppManager.findAllPolls())
    }

    @PostMapping
    @Operation(summary = "Create polls", description = "Returns newly created poll")
    fun createPolls(
            @AuthenticationPrincipal oauth2User: OAuth2User,
            @RequestBody pollRequest: PollRequest
    ): ResponseEntity<Poll> {
        val username = oauth2User.getAttribute<String>("login")
        if (username == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
        return feedAppManager.addPoll(username, pollRequest)?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete poll", description = "Delete a poll by the poll id")
    fun deletePoll(@PathVariable("id") id: Long): ResponseEntity<Void> {
        feedAppManager.deletePoll(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    public data class PollRequest(
            val question: String,
            val validUntil: Instant,
            val voteOptions: List<String>
    )
}
