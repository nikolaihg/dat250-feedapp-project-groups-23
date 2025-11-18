<script lang="ts">
  import { onMount } from "svelte";

  const props = $props<{ user: { username: string } | null }>();
  const user = $derived(props.user);

  interface VoteOption {
    id: number;
    caption: string;
  }

  interface VoteOptionCount {
  	caption: string;
	voteCount: number;
  }

  interface Poll {
    id: number;
    question: string;
    validUntil: string;
    voteOptions: VoteOption[];
	voteOptionCounts: VoteOptionCount[];
  }

  let polls = $state<Poll[]>([]);
  let loading = $state(false);
  let error = $state<string | null>(null);
  let lastLoadedFor = $state<string | null>(null);

  async function loadPolls() {
    if (!user) {
      polls = [];
      error = "Sign in to load polls.";
      return;
    }

    loading = true;
    error = null;
    try {
      const res = await fetch("/api/v1/polls", { credentials: "same-origin" });
      if (!res.ok) {
        throw new Error(await res.text());
      }
      polls = await res.json();
	  await loadVoteOptionCountsForPolls();
    } catch (err) {
      console.error("Failed to load polls", err);
      polls = [];
      error = "Could not load polls. Try again.";
    } finally {
      loading = false;
    }
  }

  async function loadVoteOptionCountsForPolls() {
  	await polls.forEach(async (poll: Poll) => {
		try {
		  const res = await fetch(`/api/v1/polls/${poll.id}/voteCount`, { credentials: "same-origin" });
		  if (!res.ok) {
			throw new Error(await res.text());
		  }
		  let voteOptionCount = await res.json();
		  console.log(voteOptionCount);
		  poll.voteOptionCounts = voteOptionCount;
		} catch (err) {
		  console.error("Failed to load poll vote counts", error);
			 error = "Could not load polls. Try again.";
		}
	});
  }

  export async function reload() {
    await loadPolls();
  }

  onMount(() => {
    if (user) {
      loadPolls();
    }
  });

  $effect(() => {
    const identifier = user ? user.username : null;
    if (identifier === lastLoadedFor) {
      return;
    }
    lastLoadedFor = identifier;
    if (user) {
      loadPolls();
    } else {
      polls = [];
      error = "Sign in to vote.";
    }
  });

  async function castVote(pollId: number, caption: string) {
    if (!user) {
      alert("Sign in to vote.");
      return;
    }
    try {
      const res = await fetch("/api/v1/votes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ pollId, caption })
      });

      if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg || "Failed to submit vote");
      }

      alert("Vote submitted!");
      await loadPolls();
    } catch (err) {
      console.error("Failed to vote", err);
      alert("Failed to submit vote.");
    }
  }
</script>

<div class="panel vote-panel">
  <h2>Vote</h2>
  <p class="panel-hint">Choose a poll and make your voice heard.</p>

  {#if loading}
    <p>Loading polls…</p>
  {:else if error}
    <p>{error}</p>
  {:else if polls.length === 0}
    <p>No polls available yet. Create one first!</p>
  {:else}
    {#each polls as poll}
      <div class="poll">
        <h3>{poll.question}</h3>
        <small>Valid until {new Date(poll.validUntil).toLocaleString()}</small>
        <div class="options">
          {#each poll.voteOptions as opt}
            <button onclick={() => castVote(poll.id, opt.caption)}>
              {opt.caption}
            </button>
          {/each}
        </div>
		<div class="voteOptionCounts">
			<table>
			  <thead>
				<tr>
				  <th>Vote Option</th>
				  <th>Vote Count</th>
				</tr>
			  </thead>
			  <tbody>
				  {#each poll.voteOptionCounts as voc}
				  <tr>
					<td>{voc.caption}</td>
					<td>{voc.voteCount}</td>
				  </tr>
				  {/each}
			  </tbody>
			</table>
		</div>
      </div>
    {/each}
  {/if}
</div>
