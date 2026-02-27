<template>
  <div class="home-wrapper">
    <div class="glass-panel demo-box">
      <div id="heading-line">
        <h1 class="title-primary">
          Home
          <loading-spinner id="spinner" :spin="isLoading" />
        </h1>
      </div>
      <h2 class="subtitle" v-if="profile && profile.username">
        Session: {{ profile.username }}
      </h2>

      <p class="description-text">
        This is a demonstration of how you can show or hide a "spinner" icon to
        let the user know something is happening. Before calling an API, you'd
        set the data property <code>isLoading</code> to <code>true</code>. When
        the call completes, set it to <code>false</code>.
      </p>
      <p class="description-text">
        For this demonstration, clicking the checkbox below sets
        <code>isLoading</code> to <code>true</code>.
      </p>

      <div class="checkbox-wrapper">
        <input
          type="checkbox"
          name="loading"
          id="loading"
          v-model="isLoading"
          class="custom-checkbox"
        />
        <label for="loading" class="checkbox-label">Is Loading</label>
      </div>

      <div class="login-message-box" v-if="!isLoggedIn">
        <p class="description-text">
          Welcome! You may browse anonymously as much as you wish,<br />
          but you must
          <router-link :to="{ name: 'login' }" class="link">Login</router-link>
          to add items to your portfolio.
        </p>
      </div>

      <hr class="divider" />

      <h2 class="subtitle">Font-awesome demonstration</h2>
      <p class="description-text">
        Below are two icons: one to indicate a "tile" view of products, and
        another to indicate a "table" view.
      </p>
      <div class="icon-demo">
        <font-awesome-icon
          :class="{ 'view-icon': true, active: cardView }"
          @click="cardView = true"
          icon="fa-solid fa-grip"
          title="View tiles"
        />
        <font-awesome-icon
          :class="{ 'view-icon': true, active: !cardView }"
          @click="cardView = false"
          icon="fa-solid fa-table"
          title="View table"
        />
      </div>
    </div>
  </div>
</template>

<script>
import LoadingSpinner from "../components/LoadingSpinner.vue";
import profileService from "../services/ProfileService";

export default {
  components: {
    LoadingSpinner,
  },
  data() {
    return {
      isLoading: false,
      cardView: true,
      profile: {},
    };
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.token.length > 0;
    },
  },
  created() {
    if (this.isLoggedIn) {
      this.getLoggedProfile();
    }
  },
  methods: {
    getLoggedProfile() {
      profileService
        .getCurrentProfile()
        .then((response) => {
          this.profile = response.data;
        })
        .catch((err) => console.error("Could not fetch profile", err));
    },
  },
};
</script>

<style scoped>
.home-wrapper {
  padding: 40px 24px;
  max-width: 800px;
  margin: 0 auto;
  min-height: 80vh;
  display: flex;
  flex-direction: column;
}

.demo-box {
  padding: 40px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.title-primary {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  display: flex;
  align-items: center;
  gap: 16px;
}

.subtitle {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.description-text {
  font-size: 1rem;
  line-height: 1.6;
  color: var(--text-secondary);
  margin: 0;
}

code {
  background: rgba(255, 255, 255, 0.1);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: "Roboto Mono", monospace;
  color: var(--accent-primary);
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 10px;
}

.custom-checkbox {
  width: 20px;
  height: 20px;
  accent-color: var(--accent-primary);
  cursor: pointer;
}

.checkbox-label {
  color: var(--text-primary);
  font-weight: 500;
  cursor: pointer;
}

.login-message-box {
  background: rgba(59, 130, 246, 0.05);
  border-left: 4px solid var(--accent-primary);
  padding: 16px;
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
  margin-top: 20px;
}

.link {
  color: var(--accent-primary);
  text-decoration: none;
  font-weight: 600;
  transition: var(--transition-smooth);
}

.link:hover {
  text-decoration: underline;
  text-shadow: 0 0 8px rgba(59, 130, 246, 0.4);
}

.divider {
  border: none;
  border-top: 1px solid var(--border-glass);
  margin: 30px 0;
}

/* Icons */
.icon-demo {
  display: flex;
  gap: 16px;
  margin-top: 10px;
}

.view-icon {
  font-size: 1.5rem;
  padding: 12px;
  color: var(--text-muted);
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--transition-smooth);
}

.view-icon:hover {
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.1);
}

.view-icon.active {
  color: #fff;
  background: var(--accent-primary);
  border-color: var(--accent-primary);
  box-shadow: var(--shadow-glow);
}
</style>
