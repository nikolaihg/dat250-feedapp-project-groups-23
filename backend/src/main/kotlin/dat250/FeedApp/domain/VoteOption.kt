package dat250.FeedApp.domain

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id

@Entity
@Table(name = "VOTE_OPTIONS")
data class VoteOption(
    @Id val id: UUID = UUID.randomUUID(),

    val caption: String,
    @JsonBackReference
    val poll: Poll? = null,
)
