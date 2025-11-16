package dat250.FeedApp.manager;

import dat250.FeedApp.controller.PollController;
import dat250.FeedApp.controller.VoteController;
import dat250.FeedApp.domain.Poll;
import dat250.FeedApp.domain.User;
import dat250.FeedApp.domain.Vote;
import dat250.FeedApp.domain.VoteOption;
import dat250.FeedApp.domain.VoteOptionCount;
import dat250.FeedApp.repository.PollRepository;
import dat250.FeedApp.repository.UserRepository;
import dat250.FeedApp.repository.VoteOptionRepository;
import dat250.FeedApp.repository.VoteRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedAppManager {
	private UserRepository userRepo;
	private PollRepository pollRepo;
	private VoteOptionRepository voteOptionRepo;
	private VoteRepository voteRepo;

	FeedAppManager(@Autowired UserRepository userRepository, @Autowired PollRepository pollRepository, @Autowired VoteOptionRepository voteOptionRepository, @Autowired VoteRepository voteRepository) {
	this.userRepo = userRepository;
	this.pollRepo = pollRepository;
	this.voteOptionRepo = voteOptionRepository;
	this.voteRepo = voteRepository;
	}

	public Optional<User> findUser(String username) {
		return userRepo.findById(username);
	}

	public User saveUser(User user) {
		return userRepo.save(user);
	}

	public List<Poll> findAllPolls() {
		List<Poll> pollsList = new ArrayList<>();
		pollRepo.findAll().forEach(pollsList::add);
		return pollsList;
	}

	public Poll addPoll(String username, PollController.PollRequest pollRequest) {
		Optional<User> user = findUser(username);
		if (user.isEmpty()) {
			return null;
		}

		Poll poll = pollRepo.save(new Poll(
				pollRequest.question(),
				Instant.now(),
				pollRequest.validUntil(),
				user.get()));

		for (String vo : pollRequest.voteOptions()) {
			voteOptionRepo.save(new VoteOption(vo, poll));
		}

		return poll;
	}

	public void deletePoll(Long pollId) {
		pollRepo.deleteById(pollId);
	}

	public Vote castVote(String username, VoteController.VoteRequest voteRequest) {
		Optional<User> user = findUser(username);
		if (user.isEmpty()) {
			return null;
		}

		VoteOption vo = voteOptionRepo.findByPollIdAndCaption(voteRequest.pollId(), voteRequest.caption());
		if (vo == null) {
			return null;
		}

		return voteRepo.save(new Vote(Instant.now(), vo, user.get()));
	}

	public List<Vote> getVotesForPoll(Long id) {
		// TODO: caching
		return voteRepo.findAllVotesByPollId(id);
	}

	public List<VoteOptionCount> getVoteOptionCountForPoll(Long id) {
		// TODO: caching
		return voteOptionRepo.findVoteOptionCountsByPollId(id);
	}
}
