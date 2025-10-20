<script lang="ts">
  import { onMount } from "svelte";

  interface VoteOption {
    id: number;
    caption: string;
    presentationOrder: number;
  }

  interface Poll {
    id: number;
    question: string;
    optionIds: number[];
  }

  let polls = $state<Poll[]>([]);
  let options = $state<Record<number, VoteOption[]>>({});
  let userId = $state<number | null>(null);

  onMount(() => {
    loadPolls();
  });

  async function loadPolls() {
    const res = await fetch("/api/polls");
    if (res.ok) {
      const fetchedPolls: Poll[] = await res.json();
      polls = fetchedPolls;
      
      for (const poll of fetchedPolls) {
        const optRes = await fetch(`/api/polls/${poll.id}/options`);
        if (optRes.ok) {
          const opts: VoteOption[] = await optRes.json();
          options[poll.id] = opts;
        }
      }
    }
  }

  async function castVote(pollId: number, voteOptionId: number) {
    if (!userId) {
      alert("Please enter your user ID");
      return;
    }

    const res = await fetch(`/api/polls/${pollId}/votes`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, voteOptionId })
    });

    if (res.ok) {
      alert("Vote submitted!");
    } else {
      alert("Failed to vote");
    }
  }
</script>

<div class="panel vote-panel">
  <h2>Vote</h2>
  <p class="panel-hint">Choose a poll and make your voice heard.</p>
  <input
    placeholder="Your User ID"
    type="number"
    bind:value={userId}
  />
  
  {#each polls as poll}
    <div class="poll">
      <h3>{poll.question}</h3>
      <div class="options">
        {#if options[poll.id]}
          {#each options[poll.id] as opt}
            <button onclick={() => castVote(poll.id, opt.id)}>
              {opt.caption}
            </button>
          {/each}
        {:else}
          <p>Loading options...</p>
        {/if}
      </div>
    </div>
  {/each}
</div>
