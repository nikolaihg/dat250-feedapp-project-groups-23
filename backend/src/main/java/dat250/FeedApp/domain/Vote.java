package dat250.FeedApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;

@Entity
@Table(name = "VOTES")
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	Instant publishedAt;

	@ManyToOne
	@JoinColumn(name = "vote_option_id")
	VoteOption voteOption;

	@ManyToOne
	@JoinColumn(name = "user_username")
	@JsonBackReference
	User user;

	public Vote() {
	}

	public Vote(Instant publishedAt, VoteOption voteOption, User user) {
		this.publishedAt = publishedAt;
		this.voteOption = voteOption;
		this.user = user;
		this.user.votes.add(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public VoteOption getVoteOption() {
		return voteOption;
	}

	public void setVoteOption(VoteOption voteOption) {
		this.voteOption = voteOption;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
