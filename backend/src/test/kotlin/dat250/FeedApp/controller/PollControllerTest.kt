package dat250.FeedApp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dat250.FeedApp.domain.Poll
import dat250.FeedApp.domain.User
import dat250.FeedApp.domain.VoteOption
import dat250.FeedApp.manager.FeedAppManager
import org.hamcrest.Matchers
import java.time.Instant
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class PollControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockitoBean private lateinit var feedAppManager: FeedAppManager

    @Autowired lateinit var objectMapper: ObjectMapper


    @Test
    fun `Scenario- create poll`() {
        val mockUsername = "testuser"
        val user = User(mockUsername)

        val pollRequest =
                PollController.PollRequest("Poll 1 question", Instant.now(), listOf("Option 1", "Option 2"))

        mockMvc.perform(
                        get("/api/v1/polls")
                                .with(oauth2Login().attributes { it["login"] = mockUsername })
                )
                .andExpect(status().isOk)
                .andExpect(content().string(Matchers.containsString("[]")))

        Mockito.`when`(feedAppManager.findUser(mockUsername)).thenReturn(user)
        Mockito.`when`(feedAppManager.addPoll(mockUsername, pollRequest)).thenReturn(
            Poll(
                question = pollRequest.question,
                id = 1,
                publishedAt = Instant.now(),
                validUntil = pollRequest.validUntil,
                voteOptions = mutableListOf(),
                creator = user
            )
        )

        mockMvc.perform(
            post("/api/v1/polls")
                .with(oauth2Login().attributes { it["login"] = mockUsername })
                .content(objectMapper.writeValueAsString(pollRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(Matchers.containsString(pollRequest.question)))
    }

}
