<template>
  <div class="overview-wrapper">
    <!-- Balance Section -->
    <div class="balance-section">
      <div class="balance-header">
        <h2 class="title-primary">Total Balance</h2>
        <span class="account-tag">Acct #{{ account.id }}</span>
      </div>

      <div class="balance-input-wrapper">
        <span class="currency-symbol">$</span>
        <input
          type="text"
          class="balance-input text-mono"
          @change="updateBalance"
          v-model="editBalance.balance"
          aria-label="Account Balance"
        />
      </div>
    </div>

    <!-- Market Snapshot Section -->
    <div class="market-section">
      <div class="section-header">
        <h3 class="title-secondary">Market Snapshot</h3>
        <span class="live-indicator">LIVE</span>
      </div>

      <div class="market-grid" v-if="!isloading">
        <div
          class="market-card glass-panel"
          v-for="stock in fetchStockCards"
          :key="stock.symbol"
          @click="routeToStockOverview(stock.symbol)"
        >
          <div class="card-header">
            <img :src="stock.logo" class="company-logo" :alt="stock.symbol" />
            <span class="stock-symbol">{{ stock.symbol }}</span>
          </div>
          <div class="card-body">
            <div class="price text-mono">${{ stock.price }}</div>
            <div
              class="change-badge text-mono"
              :class="stock.percentChange >= 0 ? 'bg-up' : 'bg-down'"
            >
              <i
                :class="
                  stock.percentChange >= 0
                    ? 'bx bx-trending-up'
                    : 'bx bx-trending-down'
                "
              ></i>
              {{
                stock.percentChange > 0
                  ? "+" + stock.percentChange
                  : stock.percentChange
              }}%
            </div>
          </div>
        </div>
      </div>
      <div v-else class="loading-state">
        <loading-spinner id="spinner" v-bind:spin="isloading" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.overview-wrapper {
  display: flex;
  flex-direction: column;
  gap: 32px;
  height: 100%;
}

/* Balance Section */
.balance-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--border-glass);
}

.balance-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-primary {
  font-size: 1.5rem;
  letter-spacing: -0.02em;
  color: var(--text-primary);
  margin: 0;
}

.account-tag {
  font-size: 0.85rem;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 20px;
  color: var(--text-muted);
  font-family: var(--font-mono);
}

.balance-input-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
}

.currency-symbol {
  font-size: 2.5rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.balance-input {
  background: transparent;
  border: none;
  outline: none;
  font-size: 3rem;
  font-weight: 600;
  color: var(--text-primary);
  width: 100%;
  padding: 0;
}

.balance-input:focus {
  color: var(--accent-primary);
}

/* Market Section */
.market-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-secondary {
  font-size: 1.1rem;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin: 0;
}

.live-indicator {
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--accent-up);
  padding: 4px 8px;
  border-radius: 4px;
  background: rgba(0, 230, 118, 0.1);
  display: flex;
  align-items: center;
  gap: 6px;
  animation: pulse 2s infinite;
}

.live-indicator::before {
  content: "";
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: var(--accent-up);
}

@keyframes pulse {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  100% {
    opacity: 1;
  }
}

/* Market Cards Grid */
.market-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.market-card {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  cursor: pointer;
  border: 1px solid var(--border-glass);
  background: var(--bg-surface);
  border-radius: var(--radius-md);
  transition: var(--transition-smooth);
}

.market-card:hover {
  transform: translateY(-4px);
  border-color: var(--accent-primary);
  box-shadow: var(--shadow-glow);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.company-logo {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 2px;
}

.stock-symbol {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--text-primary);
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.price {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
}

.change-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.85rem;
  font-weight: 600;
  width: fit-content;
  color: #fff;
}

.loading-state {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

@media (max-width: 768px) {
  .balance-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  .balance-input {
    font-size: 2.5rem;
  }
}
</style>

<script>
import accountService from "../services/AccountService";
import stockService from "../services/StockService";
import LoadingSpinner from "../components/LoadingSpinner.vue";
export default {
  created() {
    this.getAccount();
    this.getSnapshotStocks();
  },
  computed: {
    fetchStockCards() {
      this.getSnapshotStocks;
      const cards = this.stockCards;
      return cards;
    },
  },
  data() {
    return {
      account: {},
      stockCards: [],
      symbols: ["AAPL", "TSLA", "NVDA"],
      isloading: true,
      toggleSymbols: false,

      editBalance: {
        balance: "",
      },
    };
  },
  methods: {
    getAccount() {
      accountService
        .getAccount()
        .then((response) => {
          if (response.status == 200) {
            this.account = response.data;
            this.editBalance.balance = response.data.balance;
          }
        })
        .catch((error) => {
          console.error("There was an error retrieving account.", error);
        });
    },
    getSnapshotStocks() {
      this.isloading = true;
      const promises = this.symbols.map((symbol) => {
        return stockService.getStock(symbol);
      });

      Promise.all(promises)
        .then((responses) => {
          this.stockCards = responses.map((response) => {
            return response.data;
          });
        })
        .catch((error) => {
          console.error("Error fetching stocks", error);
        })
        .finally(() => {
          this.isloading = false;
        });
    },
    updateBalance() {
      accountService
        .updateAccount(this.account.id, this.editBalance)
        .then()
        .catch((error) => {
          console.log("Error updating balance", error);
        });
    },
    routeToStockOverview(symbol) {
      this.$router.push({ name: "stock-overview", params: { id: symbol } });
    },
  },
  components: {
    LoadingSpinner,
  },
};
</script>
