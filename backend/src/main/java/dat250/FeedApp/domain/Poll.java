package dat250.FeedApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "POLLS")
public class Poll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String question;
	Instant publishedAt;
	Instant validUntil;

	@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	Set<VoteOption> voteOptions;

	@ManyToOne
	@JoinColumn(name = "creator_username")
	@JsonBackReference
	User creator;

	public Poll() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

	public Instant getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Instant validUntil) {
		this.validUntil = validUntil;
	}

	public Set<VoteOption> getVoteOptions() {
		return voteOptions;
	}

	public void setVoteOptions(Set<VoteOption> voteOptions) {
		this.voteOptions = voteOptions;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Poll(String question, Instant publishedAt, Instant validUntil, User creator) {
		this.question = question;
		this.publishedAt = publishedAt;
		this.validUntil = validUntil;
		this.voteOptions = new HashSet<>();
		this.creator = creator;
		this.creator.polls.add(this);
	}
}
