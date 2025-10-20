package dat250.FeedApp.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "VOTES")
class Vote(
    @Id val id: UUID = UUID.randomUUID(),

    val publishedAt: Instant,

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    val voteOption: VoteOption,

    @ManyToOne
    @JoinColumn(name = "user_username")
    @JsonBackReference
    var user: User? = null,
)
