<script lang="ts">
  import CreatePoll from "./lib/CreatePoll.svelte";
  import Vote from "./lib/Vote.svelte";

  type View = "polls" | "vote";
  let current = $state<View>("polls");

  type User = { username: string };
  let user = $state<User | null>(null);
  let loadingUser = $state(true);
  type VoteInstance = InstanceType<typeof Vote>;
  let voteComponent = $state<VoteInstance | null>(null);

  async function getCurrentUser() {
    loadingUser = true;
    try {
      const res = await fetch("/api/v1/user", { credentials: "same-origin" });
      if (res.ok) {
        user = await res.json();
      } else {
        user = null;
      }
    } catch (err) {
      console.error("Failed to load user", err);
      user = null;
    } finally {
      loadingUser = false;
    }
  }

  function promptLogin() {
    window.location.href = "/oauth2/authorization/github";
  }

  function handlePollCreated() {
    voteComponent?.reload();
  }

  getCurrentUser();
</script>

<main>
  <h1>FeedApp polls</h1>

  {#if loadingUser}
    <p>Loading user information…</p>
  {:else}
    {#if user}
      <h2>Signed in as {user.username}</h2>
      <nav>
        <button onclick={() => current = "polls"} class:active={current === "polls"}>Create poll</button>
        <button onclick={() => {
          current = "vote";
          voteComponent?.reload();
        }} class:active={current === "vote"}>Vote</button>
      </nav>

      {#if current === "polls"}
        <CreatePoll {user} on:poll-created={handlePollCreated} />
      {:else if current === "vote"}
        <Vote bind:this={voteComponent} {user} />
      {/if}
    {:else}
      <div class="login-card">
        <p>You need to sign in with GitHub to create polls or vote.</p>
        <button onclick={promptLogin}>Sign in with GitHub</button>
      </div>
    {/if}
  {/if}
</main>

<style>
  nav {
    margin: 1rem 0;
    display: flex;
    gap: 0.5rem;
  }
  button {
    margin-right: 0.5rem;
  }
  button.active {
    font-weight: 600;
    text-decoration: underline;
  }
  .login-card {
    border: 1px solid #ccc;
    padding: 1rem;
    border-radius: 0.5rem;
    max-width: 24rem;
  }
</style>
