<template>
  <div class="account-wrapper">
    <!-- Animated background orb -->
    <div class="bg-orb"></div>

    <div class="form-container">
      <form
        class="account-card glass-panel"
        @submit.prevent="createAcc(account)"
      >
        <div class="card-header">
          <div class="icon-wrapper">
            <i class="bx bx-wallet-alt"></i>
          </div>
          <h1 class="title-primary">Set Your Balance</h1>
          <p class="subtitle">Initialize your virtual trading account</p>
        </div>

        <div class="input-group">
          <div class="balance-input-wrapper">
            <span class="currency-symbol">$</span>
            <input
              type="text"
              placeholder="0.00"
              v-model="account.balance"
              required
              autocomplete="off"
              class="balance-input text-mono"
            />
          </div>
          <div class="helper-text">
            <span>numeric values only</span>
            <span class="dot">•</span>
            <span>no commas</span>
          </div>
        </div>

        <button type="submit" class="submit-btn">
          <span>Start Trading</span>
          <i class="bx bx-right-arrow-alt"></i>
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import accountService from "../services/AccountService";

export default {
  data() {
    return {
      account: {
        balance: "",
      },
    };
  },
  methods: {
    createAcc(account) {
      accountService
        .createAccount(account)
        .then((res) => {
          if (res.status === 201) {
            this.$router.push({ name: "home" });
          }
        })
        .catch((err) => {
          console.error("Error creating account", err);
        });
    },
  },
};
</script>

<style scoped>
.account-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 100px);
  padding: 24px;
  overflow: hidden;
}

/* Background animated glow */
.bg-orb {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: radial-gradient(
    circle,
    rgba(59, 130, 246, 0.15) 0%,
    rgba(59, 130, 246, 0) 70%
  );
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 0;
  animation: pulse 4s ease-in-out infinite alternate;
  pointer-events: none;
}

@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(0.9);
    opacity: 0.6;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.1);
    opacity: 1;
  }
}

.form-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 520px;
}

.account-card {
  padding: 48px 40px;
  display: flex;
  flex-direction: column;
  gap: 40px;
  border-radius: var(--radius-lg);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.08); /* slightly stronger border for effect */
  background: rgba(15, 23, 42, 0.6); /* deeper backplate */
  backdrop-filter: blur(20px);
}

/* Subtle top border highlight */
.account-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(
    90deg,
    transparent,
    var(--accent-primary),
    transparent
  );
  opacity: 0.6;
}

/* Header */
.card-header {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: rgba(59, 130, 246, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  border: 1px solid rgba(59, 130, 246, 0.2);
  box-shadow: 0 0 30px rgba(59, 130, 246, 0.15);
}

.icon-wrapper i {
  font-size: 2rem;
  color: var(--accent-primary);
}

.title-primary {
  font-size: 2.2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

.subtitle {
  color: var(--text-muted);
  font-size: 1.05rem;
  margin: 0;
}

/* Input Area */
.input-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.balance-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  background: rgba(0, 0, 0, 0.3);
  border-radius: var(--radius-md);
  padding: 12px 24px;
  border: 1px solid var(--border-glass);
  transition: var(--transition-smooth);
}

.balance-input-wrapper:focus-within {
  border-color: var(--accent-primary);
  box-shadow:
    0 0 0 2px rgba(59, 130, 246, 0.2),
    inset 0 0 20px rgba(59, 130, 246, 0.05);
  background: rgba(0, 0, 0, 0.4);
}

.currency-symbol {
  font-size: 2.5rem;
  font-weight: 500;
  color: var(--text-secondary);
  font-family: "Roboto Mono", monospace;
  margin-right: 12px;
}

.balance-input {
  background: transparent;
  border: none;
  outline: none;
  font-size: 3.5rem;
  font-weight: 600;
  color: var(--text-primary);
  width: 100%;
  text-align: left;
  padding: 16px 0;
}

.balance-input::placeholder {
  color: rgba(255, 255, 255, 0.05);
}

.helper-text {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 0.8rem;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-weight: 600;
}

.dot {
  color: var(--accent-primary);
  font-size: 1.5rem;
  line-height: 0;
}

/* Submit Button */
.submit-btn {
  width: 100%;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: linear-gradient(135deg, var(--accent-primary), #1d4ed8);
  color: #fff;
  border: none;
  border-radius: var(--radius-md);
  font-size: 1.15rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(59, 130, 246, 0.3);
  overflow: hidden;
  position: relative;
}

.submit-btn::after {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 50%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: left 0.6s ease;
  transform: skewX(-20deg);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.5);
}

.submit-btn:hover::after {
  left: 200%;
}

.submit-btn i {
  font-size: 1.5rem;
  transition: transform 0.3s ease;
}

.submit-btn:hover i {
  transform: translateX(6px);
}

/* Responsive */
@media screen and (max-width: 600px) {
  .account-card {
    padding: 32px 24px;
    gap: 32px;
  }
  .title-primary {
    font-size: 1.8rem;
  }
  .balance-input {
    font-size: 2.5rem;
  }
  .currency-symbol {
    font-size: 2rem;
  }
  .helper-text {
    flex-wrap: wrap;
    justify-content: center;
    text-align: center;
  }
}
</style>
