package dat250.FeedApp.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "POLLS")
class Poll(
    @Id val id: UUID = UUID.randomUUID(),

    val question: String,
    val publishedAt: Instant,
    val validUntil: Instant,
    @OneToMany(mappedBy = "poll", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference
    val voteOptions: MutableList<VoteOption> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "creator_username")
    @JsonBackReference
    var creator: User? = null,
)
