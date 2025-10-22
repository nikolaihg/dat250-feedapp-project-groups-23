package dat250.FeedApp.domain

import java.util.UUID
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "VOTE_OPTIONS")
class VoteOption(
    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 	val id: Long = 0,

    val caption: String,
    @ManyToOne
    @JoinColumn(name = "poll_id")
    @JsonBackReference
    var poll: Poll,
) {
	init {
		poll.voteOptions.add(this);
	}
}
