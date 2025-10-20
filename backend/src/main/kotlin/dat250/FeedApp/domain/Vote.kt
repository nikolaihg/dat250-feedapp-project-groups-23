package dat250.FeedApp.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "VOTES")
data class Vote(
    @Id val id: UUID = UUID.randomUUID(),

    val publishedAt: Instant,
    val voteOption: VoteOption,
    @JsonBackReference
    val user: User? = null,
)
