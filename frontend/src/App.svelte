<script lang="ts">
  import CreateUser from "./lib/CreateUser.svelte";
  import CreatePoll from "./lib/CreatePoll.svelte";
  import Vote from "./lib/Vote.svelte";

  type View = "users" | "polls" | "vote";
  let current = $state<View>("users");

  type User = { username?: string };
  let user = $state<User>({})
  async function getCurrentUser() {
  	const res = await fetch("/api/v1/user", {
		method: "GET"
	})
	.then( response => response.json() )
    .then( data => {
		user = data
	})
  }
  getCurrentUser()
</script>

<main>
  <h1>Poll SPA</h1>

  <h2>Current user: {user.username}</h2>

  <nav>
    <button onclick={() => current = "users"}>Users</button>
    <button onclick={() => current = "polls"}>Polls</button>
    <button onclick={() => current = "vote"}>Vote</button>
  </nav>

  {#if current === "users"}
    <CreateUser />
  {:else if current === "polls"}
    <CreatePoll />
  {:else if current === "vote"}
    <Vote />
  {/if}
</main>

<style>
  nav {
    margin-bottom: 1rem;
  }
  button {
    margin-right: 0.5rem;
  }
</style>
