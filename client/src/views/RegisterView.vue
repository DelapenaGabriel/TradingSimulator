<template>
  <div class="register-container">
    <div class="register-card glass-panel">
      <div class="brand-header">
        <h1 class="title-primary">Trading Simulator</h1>
        <p class="subtitle">Trade Smart. Risk-Free. Master the Market.</p>
      </div>

      <div class="form-section">
        <h2 class="form-title">Create Account</h2>
        <h3 class="form-subtitle">Sign Up to Get Started</h3>

        <form @submit.prevent="register">
          <div class="input-group">
            <input
              type="text"
              placeholder="Username"
              v-model="user.username"
              autocomplete="off"
              required
              class="custom-input"
            />

            <input
              type="password"
              placeholder="Password"
              v-model="user.password"
              required
              class="custom-input"
            />

            <input
              type="password"
              placeholder="Confirm Password"
              v-model="user.confirmPassword"
              required
              class="custom-input"
            />

            <button type="submit" class="btn-primary">Register</button>
          </div>
        </form>

        <div class="login-instead">
          Already have an account?
          <router-link :to="{ name: 'login' }" class="link"
            >Login Here</router-link
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "user",
      },
    };
  },

  methods: {
    error(msg) {
      alert(msg);
    },
    success(msg) {
      alert(msg);
    },

    register() {
      if (this.user.password !== this.user.confirmPassword) {
        this.error("Passwords do not match.");
        return;
      }

      authService
        .register(this.user)
        .then((response) => {
          if (response.status === 201) {
            this.success("Registration successful. Please sign in.");
            this.$router.push({ name: "login" });
          }
        })
        .catch((error) => {
          const response = error.response;

          if (!response) {
            this.error(error);
          } else if (response.status === 400) {
            if (response.data.errors) {
              let msg = "Validation error: ";
              for (let err of response.data.errors) {
                msg += `'${err.field}': ${err.defaultMessage}. `;
              }
              this.error(msg);
            } else {
              this.error(response.data.message);
            }
          } else {
            this.error(response.data.message);
          }
        });
    },
  },
};
</script>

<style scoped>
.register-container {
  min-height: calc(100vh - 100px);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 24px;
}

.register-card {
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

.login-instead {
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
