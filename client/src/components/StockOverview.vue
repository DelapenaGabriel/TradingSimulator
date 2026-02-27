<template>
  <div class="overview-wrapper">
    <div class="back-link">
      <router-link :to="{ name: 'stocks' }" class="nav-back">
        <i class="bx bx-left-arrow-alt"></i> Back to Explorer
      </router-link>
    </div>

    <div
      class="stock-overview-card glass-panel"
      v-if="!isLoading && stock.symbol"
    >
      <!-- Header -->
      <div class="card-header">
        <div class="header-main">
          <img
            :src="
              stock.logo
                ? stock.logo
                : 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'
            "
            class="company-logo"
            :alt="stock.symbol"
          />
          <div class="symbol-info">
            <h1 class="symbol">{{ stock.symbol }}</h1>
            <h2 class="company-name">{{ stock.name }}</h2>
          </div>
        </div>
        <div class="current-price-badge">
          <span class="price-val text-mono">${{ stock.price }}</span>
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

      <!-- Description -->
      <div class="description-section">
        <h3 class="section-title">About</h3>
        <p class="description-text">
          {{ stockDescription || "No company description available." }}
        </p>
      </div>

      <!-- Data Details Grid -->
      <div class="details-grid">
        <div class="detail-group">
          <h3 class="section-title">Company Info</h3>
          <div class="info-row">
            <span class="label">Website</span>
            <a :href="stock.url" target="_blank" class="value link">{{
              stock.url || "N/A"
            }}</a>
          </div>
          <div class="info-row">
            <span class="label">Country</span>
            <span class="value">{{ stock.country || "N/A" }}</span>
          </div>
          <div class="info-row">
            <span class="label">Exchange</span>
            <span class="value">{{ stock.exchange || "N/A" }}</span>
          </div>
          <div class="info-row">
            <span class="label">IPO Date</span>
            <span class="value text-mono">{{ stock.ipo || "N/A" }}</span>
          </div>
        </div>

        <div class="detail-group">
          <h3 class="section-title">Market Data</h3>
          <div class="info-row">
            <span class="label">Open</span>
            <span class="value text-mono">${{ stock.openPrice || 0 }}</span>
          </div>
          <div class="info-row">
            <span class="label">Prev Close</span>
            <span class="value text-mono">${{ stock.previousClose || 0 }}</span>
          </div>
          <div class="info-row">
            <span class="label">Day High</span>
            <span class="value text-mono">${{ stock.highPrice || 0 }}</span>
          </div>
          <div class="info-row">
            <span class="label">Day Low</span>
            <span class="value text-mono">${{ stock.lowPrice || 0 }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="loading-state" v-if="isLoading">
      <loading-spinner id="spinner" :spin="isLoading" />
    </div>
  </div>
</template>

<script>
import stockService from "../services/StockService";
import LoadingSpinner from "./LoadingSpinner.vue";
export default {
  data() {
    return {
      stock: {},
      isLoading: false,
      stockDescription: null,
    };
  },
  created() {
    this.getStockProfile();
  },
  methods: {
    getStockProfile() {
      this.isLoading = true;
      const stockSymbol = this.$route.params.id;
      const fetchInfo = stockService.getStock(stockSymbol);
      const fetchDescription = stockService.getStockDescription(stockSymbol);

      Promise.all([fetchInfo, fetchDescription])
        .then(([response1, response2]) => {
          this.stock = response1.data;
          this.stockDescription = response2.data.results.description;
          this.isLoading = false;
        })
        .catch((error) => {
          console.error(
            "Error Occurred retrieving stock and description.",
            error,
          );
        });
    },
  },
  components: {
    LoadingSpinner,
  },
};
</script>

<style scoped>
.overview-wrapper {
  padding: 40px 24px;
  max-width: 1000px;
  margin: 0 auto;
  min-height: 80vh;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.back-link {
  margin-bottom: 8px;
}

.nav-back {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 500;
  transition: var(--transition-smooth);
}

.nav-back:hover {
  color: var(--accent-primary);
  transform: translateX(-4px);
}

.stock-overview-card {
  display: flex;
  flex-direction: column;
  padding: 0;
  overflow: hidden;
}

/* Header styling */
.card-header {
  padding: 32px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  background: rgba(255, 255, 255, 0.02);
  border-bottom: 1px solid var(--border-glass);
}

.header-main {
  display: flex;
  align-items: center;
  gap: 24px;
}

.company-logo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  background: rgba(255, 255, 255, 0.1);
  padding: 4px;
}

.symbol-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.symbol {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  line-height: 1;
}

.company-name {
  font-size: 1.1rem;
  font-weight: 400;
  color: var(--text-secondary);
  margin: 0;
}

.current-price-badge {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.price-val {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.change-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.95rem;
  font-weight: 600;
  color: #fff;
}

/* Description */
.description-section {
  padding: 32px;
  border-bottom: 1px solid var(--border-glass);
}

.section-title {
  font-size: 1rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  margin: 0 0 16px 0;
}

.description-text {
  font-size: 1rem;
  line-height: 1.6;
  color: var(--text-primary);
  margin: 0;
}

/* Grid */
.details-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
  padding: 32px;
}

.detail-group {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.03);
}

.label {
  color: var(--text-secondary);
  font-size: 0.95rem;
}

.value {
  color: var(--text-primary);
  font-weight: 500;
}

.link {
  color: var(--accent-primary);
  text-decoration: none;
  transition: var(--transition-smooth);
}

.link:hover {
  text-decoration: underline;
  text-shadow: 0 0 8px rgba(59, 130, 246, 0.4);
}

.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 24px;
  }
  .current-price-badge {
    align-items: flex-start;
  }
  .details-grid {
    grid-template-columns: 1fr;
  }
}
</style>
