<template>
  <div class="trading-wrapper">
    <!-- Trading Controls Panel -->
    <div class="trade-box glass-panel">
      <!-- Search -->
      <div class="search-section">
        <div class="search-input-wrapper">
          <i class="bx bx-search search-icon"></i>
          <input
            type="text"
            placeholder="Enter symbol (e.g. AAPL)"
            v-model="inputStock"
            class="custom-input"
            @keyup.enter="getStock"
          />
        </div>
        <button @click="getStock" class="btn-primary">Search</button>
      </div>

      <div class="stock-info-container" v-if="!isLoading && stock.symbol">
        <!-- Stock Header -->
        <div class="stock-header">
          <img
            :src="
              stock.logo
                ? stock.logo
                : 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'
            "
            class="stock-logo"
          />
          <div class="symbol-desc">
            <h2 class="symbol">{{ stock.symbol }}</h2>
            <p class="description">{{ stock.name }}</p>
          </div>
        </div>

        <!-- Price Details Grid -->
        <div class="price-details text-mono">
          <div class="detail-item prime">
            <span class="label text-sans">Price</span>
            <span class="value">${{ stock.price }}</span>
          </div>
          <div class="detail-item prime-change">
            <span class="label text-sans">Change</span>
            <span
              class="value change-badge"
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
            </span>
          </div>
          <div class="detail-item">
            <span class="label text-sans">High</span>
            <span class="value">${{ stock.highPrice }}</span>
          </div>
          <div class="detail-item">
            <span class="label text-sans">Low</span>
            <span class="value">${{ stock.lowPrice }}</span>
          </div>
          <div class="detail-item">
            <span class="label text-sans">Open</span>
            <span class="value">${{ stock.openPrice }}</span>
          </div>
          <div class="detail-item">
            <span class="label text-sans">Prev</span>
            <span class="value">${{ stock.previousClose }}</span>
          </div>
        </div>
      </div>

      <div class="loading-state" v-if="isLoading && inputStock">
        <p class="text-muted">Loading data...</p>
      </div>

      <!-- Action Section -->
      <div class="action-section" v-if="!isLoading && stock.symbol">
        <div class="shares-input-wrapper">
          <input
            type="number"
            class="custom-input text-mono"
            required
            min="1"
            placeholder="0"
            v-model="inputShares"
          />
          <span class="input-suffix">Shares</span>
        </div>

        <div class="buy-sell-actions">
          <button class="action-btn btn-buy" @click="buyTrade">BUY</button>
          <button class="action-btn btn-sell" @click="sellTrade">SELL</button>
        </div>
      </div>

      <!-- Account Info Footer -->
      <div class="account-footer text-mono">
        <div class="acc-item">
          <span class="acc-label text-sans">Account #</span>
          <span class="acc-value">{{ account.id || "---" }}</span>
        </div>
        <div class="acc-item">
          <span class="acc-label text-sans">Balance</span>
          <span class="acc-value">${{ account.balance || "0.00" }}</span>
        </div>
      </div>

      <!-- Order Status -->
      <div class="order-status" v-if="isPlaced">
        <div class="success-message">
          <i class="bx bx-check-circle"></i> Order placed for {{ stock.symbol }}
        </div>
      </div>
    </div>

    <!-- TradingView Chart Panel -->
    <div class="chart-panel glass-panel">
      <div id="chart" class="tradingview-widget-container"></div>
    </div>
  </div>
</template>

<script>
import accountService from "../services/AccountService";
import stockService from "../services/StockService";
import tradeService from "../services/TradeService";

export default {
  data() {
    return {
      inputStock: "",
      stock: {},
      account: {},
      isLoading: true,
      isPlaced: false,
      inputShares: "",
      overview: {},
    };
  },
  created() {
    //Lifecycle hook that is call before the component is rendered to the DOM.
    this.getAccount();
  },
  mounted() {
    //Lifecycle hook that is called after the component is mounted or rendered to the DOM.
    this.loadTradingView(); //We place the widget here and not created() hook because we need the HTML container for the TradingView widget.
  },
  methods: {
    getStock() {
      if (this.inputStock != "") {
        stockService
          .getStock(this.inputStock.toUpperCase())
          .then((response) => {
            this.stock = response.data;
            this.isLoading = false;
            this.isPlaced = false;
          })
          .catch((error) => {
            console.error("Error occurred fetching stock", error);
          });
      }
    },
    getAccount() {
      this.isLoading = true;
      accountService
        .getAccount()
        .then((response) => {
          if (response.status == 200) {
            this.account = response.data;
          }
        })
        .catch((error) => {
          console.error("Error occurred fetching account", error);
        });
    },
    loadTradingView() {
      //Window is a global object meaning any object defined in the global scope is a property of the window object.
      if (window.TradingView) {
        //Checking to see if TradingView library is loaded/created and is ready to use. Using "window" is important when working with external libraries.
        new window.TradingView.widget({
          //We initialize the TradingView widget with desired attributes/properties
          container_id: "chart", // Set the container here. IMPORTANT why we need to use mounted() hook instead of created().
          autosize: true,
          symbol: "AAPL",
          interval: "240",
          timezone: "Etc/UTC",
          theme: "dark",
          style: "1",
          locale: "en",
          toolbar_bg: "#0B111A" /* match background of deep charcoal */,
          enable_publishing: false,
          withdateranges: true,
          watchlist: ["AAPL", "TSLA", "NVDA"],
          hide_side_toolbar: false,
          allow_symbol_change: true,
          details: false,
          hotlist: true,
          calendar: true,
          show_popup_button: true,
          popup_width: "1000",
          popup_height: "650",
        });
      } else {
        console.error("TradingView is not loaded");
      }
    },
    buyTrade() {
      if (this.stock.symbol && this.inputShares != "") {
        const buyOrder = {
          symbol: this.stock.symbol,
          quantity: this.inputShares,
        };

        const totalCost = buyOrder.quantity * this.stock.price;
        const balance = this.account.balance;

        if (totalCost > balance) {
          alert(
            "Insufficient balance to complete this order. Please reduce the number of shares.",
          );
        } else if (
          isNaN(buyOrder.quantity) ||
          !buyOrder.symbol ||
          !buyOrder.quantity
        ) {
          alert("Trade execution failed. Please try again.");
        } else {
          tradeService
            .buyStock(buyOrder)
            .then((response) => {
              this.isPlaced = true;
              this.getAccount();
              this.$emit("trade-placed");
            })
            .catch((error) => {
              console.error("Error occured placing a buy order.", error);
            });
        }
      }
    },
    sellTrade() {
      if (this.stock.symbol && this.inputShares != "") {
        const sellOrder = {
          symbol: this.stock.symbol,
          quantity: this.inputShares,
        };

        const totalCost = sellOrder.quantity * this.stock.price;
        const balance = this.account.balance;

        if (
          sellOrder.symbol &&
          sellOrder.quantity &&
          !isNaN(sellOrder.quantity)
        ) {
          tradeService
            .sellStock(sellOrder)
            .then((response) => {
              this.isPlaced = true;
              this.getAccount();
              this.$emit("trade-placed");
            })
            .catch((error) => {
              console.error("Error occured placing a sell order.", error);
            });
        } else {
          alert("Trade execution failed. Please try again.");
        }
      }
    },
  },
};
</script>

<style scoped>
.trading-wrapper {
  display: flex;
  gap: 24px;
  min-height: calc(100vh - 80px);
  width: 100%;
  padding: 24px;
}

@media (max-width: 1024px) {
  .trading-wrapper {
    flex-direction: column;
    height: auto;
  }
}

/* Control Panel */
.trade-box {
  width: 100%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 24px;
  flex-shrink: 0;
}

@media (max-width: 1024px) {
  .trade-box {
    max-width: 100%;
  }
}

/* Search */
.search-section {
  display: flex;
  gap: 12px;
}

.search-input-wrapper {
  position: relative;
  flex: 1;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-muted);
  font-size: 1.2rem;
}

.custom-input {
  width: 100%;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-md);
  padding: 12px 16px;
  color: var(--text-primary);
  font-size: 1rem;
  outline: none;
  transition: var(--transition-smooth);
}

.search-input-wrapper .custom-input {
  padding-left: 40px;
}

.custom-input:focus {
  border-color: var(--accent-primary);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  background: rgba(255, 255, 255, 0.05);
}

.btn-primary {
  background: var(--accent-primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-md);
  padding: 0 20px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition-smooth);
}

.btn-primary:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

/* Stock Info */
.stock-info-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-glass);
}

.stock-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stock-logo {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  background: rgba(255, 255, 255, 0.1);
  padding: 2px;
}

.symbol-desc {
  display: flex;
  flex-direction: column;
}

.symbol {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  line-height: 1.2;
}

.description {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin: 0;
}

/* Price Details Grid */
.price-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  background: rgba(255, 255, 255, 0.02);
  padding: 10px;
  border-radius: var(--radius-sm);
}

.detail-item.prime {
  grid-column: span 1;
  background: rgba(59, 130, 246, 0.05);
  border: 1px solid rgba(59, 130, 246, 0.2);
}
.detail-item.prime-change {
  grid-column: span 1;
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid var(--border-glass);
}

.detail-item.prime .value {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--text-primary);
}

.label {
  font-size: 0.75rem;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.text-sans {
  font-family: "Inter", sans-serif;
}

.value {
  font-size: 0.95rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.change-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 1rem;
  font-weight: 600;
  color: var(--text-primary);
}
.change-badge.bg-up {
  color: var(--accent-up);
  background: transparent;
  padding: 0;
}
.change-badge.bg-down {
  color: var(--accent-down);
  background: transparent;
  padding: 0;
}

/* Actions */
.action-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.shares-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.shares-input-wrapper .custom-input {
  padding-right: 70px;
  font-size: 1.2rem;
  text-align: right;
}

.input-suffix {
  position: absolute;
  right: 16px;
  color: var(--text-muted);
  font-family: "Inter", sans-serif;
  font-size: 0.9rem;
  pointer-events: none;
}

.buy-sell-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  flex: 1;
  padding: 16px;
  border: none;
  border-radius: var(--radius-md);
  font-size: 1.1rem;
  font-weight: 700;
  color: #fff;
  cursor: pointer;
  transition: var(--transition-smooth);
}

.btn-buy {
  background: var(--accent-up);
  box-shadow: 0 4px 14px rgba(16, 185, 129, 0.2);
}

.btn-buy:hover {
  background: #059669; /* Slightly darker */
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(16, 185, 129, 0.4);
}

.btn-sell {
  background: var(--accent-down);
  box-shadow: 0 4px 14px rgba(239, 68, 68, 0.2);
}

.btn-sell:hover {
  background: #dc2626; /* Slightly darker */
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(239, 68, 68, 0.4);
}

.action-btn:active {
  transform: translateY(0);
}

/* Account Footer */
.account-footer {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding-top: 20px;
  border-top: 1px solid var(--border-glass);
}

.acc-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
}

.acc-label {
  color: var(--text-muted);
}

.acc-value {
  color: var(--text-secondary);
}

/* Order Status */
.order-status {
  padding-top: 12px;
}

.success-message {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: var(--accent-up);
  background: rgba(16, 185, 129, 0.1);
  padding: 12px;
  border-radius: var(--radius-md);
  font-weight: 500;
  font-size: 0.9rem;
  animation: pulseGlow 2s infinite alternate;
}

@keyframes pulseGlow {
  0% {
    box-shadow: 0 0 0 rgba(16, 185, 129, 0);
  }
  100% {
    box-shadow: 0 0 10px rgba(16, 185, 129, 0.2);
  }
}

.loading-state {
  display: flex;
  justify-content: center;
  padding: 20px;
}

/* Chart Panel */
.chart-panel {
  flex: 1;
  padding: 0; /* Trading view widget will cover it */
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.tradingview-widget-container {
  flex: 1;
  width: 100%;
  height: 100%;
}

#chart {
  width: 100%;
  height: 100%;
  min-height: 500px;
}
</style>
