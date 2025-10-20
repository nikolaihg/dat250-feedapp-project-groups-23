package dat250.FeedApp.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "POLLS")
data class Poll(
    @Id val id: UUID = UUID.randomUUID(),

    val question: String,
    val publishedAt: Instant,
    val validUntil: Instant,
    @JsonManagedReference
    val voteOptions: MutableList<VoteOption> = mutableListOf(),
    @JsonBackReference
    val creator: User? = null,
)
