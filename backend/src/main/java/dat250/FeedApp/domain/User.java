package dat250.FeedApp.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
	@Id
	String username;

	@OneToMany(mappedBy = "creator")
	@JsonManagedReference
	Set<Poll> polls;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	Set<Vote> votes;

	public User() {
	}

	public User(String username) {
		this.username = username;
		this.polls = new HashSet<>();
		this.votes = new HashSet<>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Poll> getPolls() {
		return polls;
	}

	public void setPolls(Set<Poll> polls) {
		this.polls = polls;
	}

	public Set<Vote> getVotes() {
		return votes;
	}

	public void setVotes(Set<Vote> votes) {
		this.votes = votes;
	}

}
