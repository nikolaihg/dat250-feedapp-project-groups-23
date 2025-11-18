@GetMapping
@Operation(
		summary = "Get current user",
		description = "Returns user information of currently logged in user"
)
fun getUserData(@AuthenticationPrincipal oauth2User: OAuth2User): ResponseEntity<User> {

	val userName: String? = oauth2User.getAttribute<String>("login")
	if (userName != null) {
		var user = feedAppManager.findUser(userName)
		// create user in our db if does not exist
		if (user == null) {
			user = User(userName)
			feedAppManager.saveUser(user)
		}
		return ResponseEntity.ok(user)
	}
	return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
}
