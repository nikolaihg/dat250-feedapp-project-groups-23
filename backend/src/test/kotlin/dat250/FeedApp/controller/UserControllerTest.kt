package dat250.FeedApp.controller

import dat250.FeedApp.domain.User
import dat250.FeedApp.manager.FeedAppManager
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
class UserControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockitoBean private lateinit var feedAppManager: FeedAppManager

    @Test
    fun `should return error info when not logged in`() {
        // redirect because directed to OAuth login page
        mockMvc.perform(get("/api/v1/user")).andExpect(status().is3xxRedirection())
    }

    @Test
    fun `should return user info when logged in`() {
        val mockUsername = "testuser"
        val user = User(mockUsername)

        Mockito.`when`(feedAppManager.findUser(mockUsername)).thenReturn(user)

        mockMvc.perform(
                        get("/api/v1/user")
                                .with(oauth2Login().attributes { it["login"] = mockUsername })
                )
                .andExpect(status().isOk)
                .andExpect(content().string(org.hamcrest.Matchers.containsString(mockUsername)))
    }
}
