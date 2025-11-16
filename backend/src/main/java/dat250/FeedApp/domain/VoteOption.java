package dat250.FeedApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "VOTE_OPTIONS")
public class VoteOption {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String caption;

	@ManyToOne
	@JoinColumn(name = "poll_id")
	@JsonBackReference
	Poll poll;

	public VoteOption() {
	}

	public VoteOption(String caption, Poll poll) {
		this.caption = caption;
		this.poll = poll;
		this.poll.voteOptions.add(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}
}
