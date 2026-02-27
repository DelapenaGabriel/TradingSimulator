<template>
  <div class="trade-log-wrapper">
    <!-- Header -->
    <div class="trade-header">
      <h2 class="title-primary">Trade History</h2>
      <div class="search-box">
        <i class="bx bx-search search-icon"></i>
        <input
          type="text"
          v-model="filteredText"
          placeholder="Search..."
          class="search-input"
        />
      </div>
    </div>

    <!-- Table -->
    <div class="table-container custom-scrollbar">
      <table class="trade-table">
        <thead>
          <tr>
            <th>Sym</th>
            <th>Type</th>
            <th class="text-right">Qty</th>
            <th class="text-right">Price</th>
            <th class="text-right">Date</th>
          </tr>
        </thead>
        <tbody v-if="trades.length > 0">
          <tr v-for="trade in filteredList" :key="trade.id" class="trade-row">
            <td class="font-bold">{{ trade.symbol }}</td>
            <td>
              <span
                :class="[
                  'type-badge',
                  trade.tradeType.toLowerCase() === 'buy'
                    ? 'bg-buy'
                    : 'bg-sell',
                ]"
              >
                {{ trade.tradeType }}
              </span>
            </td>
            <td class="text-right text-mono font-medium">
              {{ trade.quantity }}
            </td>
            <td class="text-right text-mono font-medium">${{ trade.price }}</td>
            <td class="text-right text-muted text-sm">
              {{ formatShortDate(trade.tradeDate) }}
            </td>
          </tr>
        </tbody>
        <tbody v-else>
          <tr>
            <td colspan="5" class="empty-state">
              <span v-if="!isLoading">No trades found</span>
              <loading-spinner :spin="isLoading" v-else />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import tradeService from "../services/TradeService";
import LoadingSpinner from "../components/LoadingSpinner.vue";

export default {
  components: { LoadingSpinner },
  data() {
    return {
      trades: [],
      isLoading: true,
      filteredText: "",
    };
  },
  created() {
    this.getTrades();
  },
  methods: {
    getTrades() {
      this.isLoading = true;
      tradeService
        .getAllTrades()
        .then((res) => {
          this.trades = res.data;
        })
        .catch((err) => console.error("Error fetching trades:", err))
        .finally(() => {
          this.isLoading = false;
        });
    },
    formatDate(dateStr) {
      if (!dateStr) return "";
      const date = new Date(dateStr);
      const options = {
        year: "numeric",
        month: "short",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
      };
      return date.toLocaleString(undefined, options);
    },
    formatShortDate(dateStr) {
      if (!dateStr) return "";
      const date = new Date(dateStr);
      return date.toLocaleDateString(undefined, {
        month: "short",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    },
  },
  computed: {
    filteredList() {
      if (!this.filteredText) return this.trades;
      const text = this.filteredText.toLowerCase();
      return this.trades.filter(
        (trade) =>
          trade.symbol?.toLowerCase().includes(text) ||
          trade.tradeType?.toLowerCase().includes(text) ||
          trade.id?.toString().includes(text) ||
          trade.userId?.toString().includes(text) ||
          trade.quantity?.toString().includes(text) ||
          trade.price?.toString().includes(text) ||
          trade.tradeDate?.toLowerCase().includes(text),
      );
    },
  },
};
</script>

<style scoped>
.trade-log-wrapper {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: 20px;
}

/* Header */
.trade-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.title-primary {
  font-size: 1.25rem;
  color: var(--text-primary);
  margin: 0;
  font-weight: 600;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 12px;
  color: var(--text-muted);
  font-size: 1.1rem;
}

.search-input {
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-sm);
  padding: 8px 12px 8px 36px;
  color: var(--text-primary);
  font-size: 0.9rem;
  outline: none;
  transition: var(--transition-smooth);
  width: 100%;
  max-width: 250px;
}

.search-input:focus {
  border-color: var(--accent-primary);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

/* Table container styling */
.table-container {
  overflow-y: auto;
  flex: 1;
  max-height: 500px;
  padding-right: 4px;
}

/* Custom internal scrollbar */
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: var(--border-glass);
  border-radius: 4px;
}

.trade-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.trade-table th {
  position: sticky;
  top: 0;
  background: var(--bg-surface);
  backdrop-filter: blur(10px);
  padding: 12px 16px;
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
  border-bottom: 1px solid var(--border-glass);
  z-index: 10;
}

.trade-table td {
  padding: 14px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.03);
  vertical-align: middle;
}

.trade-row {
  transition: background-color 0.2s ease;
}

.trade-row:hover {
  background-color: rgba(255, 255, 255, 0.02);
}

/* Typography & Layout Utilities */
.text-right {
  text-align: right;
}
.font-bold {
  font-weight: 700;
  color: var(--text-primary);
}
.font-medium {
  font-weight: 500;
  color: var(--text-primary);
}
.text-sm {
  font-size: 0.85rem;
}
.text-muted {
  color: var(--text-muted);
}

/* Badges */
.type-badge {
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  display: inline-block;
}

.bg-buy {
  background: rgba(0, 230, 118, 0.15);
  color: var(--accent-up);
  border: 1px solid rgba(0, 230, 118, 0.3);
}

.bg-sell {
  background: rgba(255, 59, 59, 0.15);
  color: var(--accent-down);
  border: 1px solid rgba(255, 59, 59, 0.3);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 40px;
  color: var(--text-muted);
  font-style: italic;
}
</style>
