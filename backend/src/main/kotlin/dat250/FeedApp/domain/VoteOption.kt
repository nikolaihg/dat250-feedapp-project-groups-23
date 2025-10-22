package dat250.FeedApp.domain

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
@Table(name = "VOTE_OPTIONS")
class VoteOption(
    @Id val id: UUID = UUID.randomUUID(),

    val caption: String,
    @ManyToOne
    @JoinColumn(name = "poll_id")
    @JsonBackReference
    var poll: Poll? = null,
)
