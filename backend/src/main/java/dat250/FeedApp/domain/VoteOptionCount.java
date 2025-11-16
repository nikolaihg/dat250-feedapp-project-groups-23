package dat250.FeedApp.domain;

public class VoteOptionCount {
	String caption;
	Long voteCount;

	public VoteOptionCount() {
	}

	public VoteOptionCount(String caption, Long voteCount) {
		this.caption = caption;
		this.voteCount = voteCount;
	}
}
