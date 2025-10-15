<script lang="ts">
  let username = $state("");
  let email = $state("");

  async function submit() {
    if (!username || !email) return;
    const res = await fetch("/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email })
    });
    if (res.ok) {
      alert("User created!");
      username = "";
      email = "";
    } else {
      alert("Failed to create user");
    }
  }
</script>

<div>
  <h2>Create User</h2>
  <input placeholder="Username" bind:value={username} />
  <input placeholder="Email" type="email" bind:value={email} />
  <button onclick={submit}>Create</button>
</div>
