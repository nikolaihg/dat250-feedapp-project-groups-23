package dat250.FeedApp.domain

public data class VoteOptionCount(val caption: String, val voteCount: Long) {
    private constructor() : this("dummy", 0)
}
