<script lang="ts">
  import { createEventDispatcher } from "svelte";

  const props = $props<{ user: { username: string } | null }>();
  const user = $derived(props.user);

  const dispatch = createEventDispatcher<{ "poll-created": void }>();

  let question = $state("");
  let options = $state<string[]>(["", ""]);
  let validUntil = $state<string>("");
  let submitting = $state(false);

  function addOption() {
    options = [...options, ""];
  }

  function removeOption(index: number) {
    if (options.length > 2) {
      options = options.filter((_, i) => i !== index);
    }
  }

  async function submit() {
    if (!user) {
      alert("Sign in before creating a poll.");
      return;
    }
    if (!question.trim() || options.some((o) => !o.trim())) {
      alert("Provide a question and at least two option texts.");
      return;
    }
    if (!validUntil) {
      alert("Select when the poll should expire.");
      return;
    }

    submitting = true;
    try {
      const payload = {
        question: question.trim(),
        validUntil: new Date(validUntil).toISOString(),
        voteOptions: options.map((o) => o.trim())
      };

      const res = await fetch("/api/v1/polls", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const message = await res.text();
        throw new Error(message || "Failed to create poll");
      }

      alert("Poll created successfully!");
      question = "";
      options = ["", ""];
      validUntil = "";
      dispatch("poll-created");
    } catch (error) {
      console.error("Error creating poll:", error);
      alert("Failed to create poll");
    } finally {
      submitting = false;
    }
  }
</script>

<div class="panel create-poll">
  <h2>Create Poll</h2>
  <p class="panel-hint">Draft a question and add options for people to vote on.</p>

  <div class="form-group">
    <label for="question">Question:</label>
    <input
      id="question"
      placeholder="Enter your poll question"
      bind:value={question}
    />
  </div>

  <div class="form-group">
    <label for="validUntil">Expires at:</label>
    <input
      id="validUntil"
      type="datetime-local"
      bind:value={validUntil}
    />
  </div>

  <div class="options-section">
    <h3>Options:</h3>
    {#each options as option, i}
      <div class="option-row">
        <input
          placeholder="Option {i + 1}"
          bind:value={options[i]}
        />
        {#if options.length > 2}
          <button
            type="button"
            class="remove-btn"
            onclick={() => removeOption(i)}
          >
            Remove
          </button>
        {/if}
      </div>
    {/each}
  </div>

  <div class="actions">
    <button type="button" onclick={addOption}>+ Add Option</button>
    <button type="button" onclick={submit} class="primary" disabled={submitting}>
      {submitting ? "Creating…" : "Create Poll"}
    </button>
  </div>
</div>
