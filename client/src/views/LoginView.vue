<template>
  <div class="login-container">
    <div class="login-card glass-panel">
      <div class="brand-header">
        <h1 class="title-primary">Trading Simulator</h1>
        <p class="subtitle">Master the Market. Risk-Free. Trade Smarter.</p>
      </div>

      <div class="form-section">
        <h2 class="form-title">Welcome Back</h2>
        <h3 class="form-subtitle">Sign in to Continue</h3>

        <form @submit.prevent="login">
          <div class="input-group">
            <input
              type="text"
              placeholder="Username"
              v-model="user.username"
              required
              autocomplete="off"
              class="custom-input"
            />

            <input
              type="password"
              placeholder="Password"
              v-model="user.password"
              required
              class="custom-input"
            />

            <button type="submit" class="btn-primary">Login</button>
          </div>
        </form>

        <div class="register-here">
          Need an account?
          <router-link :to="{ name: 'register' }" class="link">
            Register Here
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";
import profileService from "../services/ProfileService";
import accountService from "../services/AccountService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      profile: {
        id: "",
      },
      account: {
        id: "",
      },
    };
  },

  methods: {
    getAccountProfile() {
      const fetchAccount = accountService.getAccount();
      const fetchProfile = profileService.getCurrentProfile();

      Promise.all([fetchAccount, fetchProfile])
        .then(([response1, response2]) => {
          this.account.id = response1.data.id;
          this.profile.id = response2.data.id;
        })
        .catch((error) => {
          console.error("An error occurred: ", error);
        })
        .finally(() => {
          if (!this.account.id && !this.profile.id) {
            this.$router.push({ name: "create-profile" });
          } else if (!this.account.id && this.profile.id) {
            this.$router.push({ name: "create-account" });
          } else {
            this.$router.push({ name: "home" });
          }
        });
    },

    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);

            this.getAccountProfile();
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            alert(error);
          } else if (response.status === 401) {
            alert("Invalid username and password!");
          } else {
            alert(response.message);
          }
        });
    },
  },
};
</script>

<style scoped>
.login-container {
  min-height: calc(100vh - 100px);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 440px;
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  gap: 32px;
  animation: fadeIn 0.6s cubic-bezier(0.16, 1, 0.3, 1);
}

.brand-header {
  text-align: center;
}

.title-primary {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

.subtitle {
  font-size: 0.95rem;
  color: var(--accent-primary);
  margin: 8px 0 0 0;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.form-section {
  display: flex;
  flex-direction: column;
}

.form-title {
  font-size: 1.5rem;
  color: var(--text-primary);
  text-align: center;
  margin: 0 0 4px 0;
  font-weight: 600;
}

.form-subtitle {
  font-size: 0.95rem;
  color: var(--text-muted);
  text-align: center;
  margin: 0 0 24px 0;
  font-weight: 400;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.custom-input {
  width: 100%;
  padding: 14px 16px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-glass);
  color: var(--text-primary);
  border-radius: var(--radius-md);
  outline: none;
  font-size: 1rem;
  transition: var(--transition-smooth);
}

.custom-input::placeholder {
  color: var(--text-muted);
}

.custom-input:focus {
  background: rgba(255, 255, 255, 0.05);
  border-color: var(--accent-primary);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.btn-primary {
  margin-top: 8px;
  width: 100%;
  padding: 14px;
  font-size: 1.05rem;
  font-weight: 600;
  border: none;
  border-radius: var(--radius-md);
  background: var(--accent-primary);
  color: #fff;
  cursor: pointer;
  transition: var(--transition-smooth);
}

.btn-primary:hover {
  transform: translateY(-2px);
  background: #2563eb;
  box-shadow: var(--shadow-glow);
}

.register-here {
  text-align: center;
  margin-top: 24px;
  color: var(--text-secondary);
  font-size: 0.95rem;
}

.link {
  color: var(--accent-primary);
  font-weight: 600;
  text-decoration: none;
  transition: var(--transition-smooth);
  margin-left: 4px;
}

.link:hover {
  text-decoration: underline;
  text-shadow: 0 0 8px rgba(59, 130, 246, 0.4);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
