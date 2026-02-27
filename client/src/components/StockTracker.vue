<template>
  <div class="stock-tracker-wrapper">
    <div class="headline">
      <h1 class="title-primary">Explore Stocks</h1>
    </div>

    <div class="search-filter">
      <div class="search-box">
        <i class="bx bx-search search-icon"></i>
        <input
          type="text"
          v-model="searchStock"
          placeholder="Search a Stock Symbol (e.g. AAPL)"
          class="search-input"
          @keyup.enter="getSearchedStock"
        />
      </div>
      <button @click="getSearchedStock" class="btn-primary">Search</button>
    </div>

    <div
      class="searched-container"
      v-if="searchStock && !isloading && searchedStock.symbol"
    >
      <div
        class="stock-card glass-panel"
        @click="routeToStockOverview(searchedStock.symbol)"
      >
        <div class="logo-symbol">
          <img
            :src="
              searchedStock.logo
                ? searchedStock.logo
                : 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'
            "
          />
          <h2>{{ searchedStock.symbol }}</h2>
        </div>
        <div class="price-change">
          <p class="price text-mono">${{ searchedStock.price }}</p>
          <div
            class="change-badge text-mono"
            :class="searchedStock.percentChange >= 0 ? 'bg-up' : 'bg-down'"
          >
            <i
              :class="
                searchedStock.percentChange >= 0
                  ? 'bx bx-trending-up'
                  : 'bx bx-trending-down'
              "
            ></i>
            {{
              searchedStock.percentChange > 0
                ? "+" + searchedStock.percentChange
                : searchedStock.percentChange
            }}%
          </div>
        </div>
        <div class="price-info">
          <div class="info-block">
            <span class="label">Open</span>
            <span class="value text-mono">${{ searchedStock.openPrice }}</span>
          </div>
          <div class="info-block">
            <span class="label">Prev Close</span>
            <span class="value text-mono"
              >${{ searchedStock.previousClose }}</span
            >
          </div>
          <div class="info-block">
            <span class="label">High</span>
            <span class="value text-mono">${{ searchedStock.highPrice }}</span>
          </div>
          <div class="info-block">
            <span class="label">Low</span>
            <span class="value text-mono">${{ searchedStock.lowPrice }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="stock-grid" v-if="!isloading && !searchStock">
      <div
        class="stock-card glass-panel"
        v-for="stock in displayStocks"
        :key="stock.symbol"
        @click="routeToStockOverview(stock.symbol)"
      >
        <div class="logo-symbol">
          <img
            :src="
              stock.logo
                ? stock.logo
                : 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'
            "
          />
          <h2>{{ stock.symbol }}</h2>
        </div>
        <div class="price-change">
          <p class="price text-mono">${{ stock.price }}</p>
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
        <div class="price-info">
          <div class="info-block">
            <span class="label">Open</span>
            <span class="value text-mono">${{ stock.openPrice }}</span>
          </div>
          <div class="info-block">
            <span class="label">High</span>
            <span class="value text-mono">${{ stock.highPrice }}</span>
          </div>
          <div class="info-block">
            <span class="label">Low</span>
            <span class="value text-mono">${{ stock.lowPrice }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="loading-state" v-else-if="isloading">
      <loading-spinner id="spinner" :spin="isloading" />
    </div>

    <div class="load-more" v-if="!searchStock && hasMoreSymbols && !isloading">
      <button @click="loadMoreStocks" class="btn-secondary">
        Load More Stocks
      </button>
    </div>
  </div>
</template>

<script>
import stockService from "../services/StockService";
import LoadingSpinner from "../components/LoadingSpinner.vue";

export default {
  data() {
    return {
      stocks: [],
      searchedStock: {},
      displayStocks: [],
      displayCounter: 5,
      currentPage: 0,
      isloading: false,
      searchStock: null,
    };
  },
  created() {
    this.getStocks();
  },
  components: {
    LoadingSpinner,
  },
  methods: {
    getStocks() {
      this.isloading = true;
      stockService
        .getAllStocks()
        .then((response) => {
          if (response.status == 200) {
            this.stocks = response.data;
          }
        })
        .catch((error) => {
          console.error("Error Occurred retrieving stocks.", error);
        })
        .then(() => {
          this.loadMoreStocks();
        });
    },
    loadMoreStocks() {
      this.isloading = true;
      const start = this.currentPage * this.displayCounter;
      const end = start + this.displayCounter;
      const newStocks = this.stocks.slice(start, end);

      const promises = newStocks
        .map((stock) => {
          if (stock.displaySymbol != undefined || stock.displaySymbol) {
            return stockService
              .getStock(stock.displaySymbol)
              .then((res) => res.data);
          }
        })
        .filter(Boolean);

      Promise.all(promises)
        .then((results) => {
          this.displayStocks = [...this.displayStocks, ...results];
        })
        .catch((err) => console.error(err))
        .finally(() => {
          this.isloading = false;
        });

      this.currentPage++;
    },
    getSearchedStock() {
      if (this.searchStock) {
        this.isloading = true;
        stockService
          .getStock(this.searchStock.toUpperCase())
          .then((response) => {
            this.searchedStock = response.data;
            this.isloading = false;
          })
          .catch((error) => {
            console.error("Error occurred retrieving searched stock.", error);
            this.isloading = false;
          });
      }
    },
    routeToStockOverview(symbol) {
      if (!symbol) return;
      this.$router.push({ name: "stock-overview", params: { id: symbol } });
    },
  },
  computed: {
    hasMoreSymbols() {
      return this.currentPage * this.displayCounter < this.stocks.length;
    },
  },
};
</script>

<style scoped>
.stock-tracker-wrapper {
  min-height: 80vh;
  display: flex;
  flex-direction: column;
  padding: 40px 24px;
  max-width: 1400px;
  margin: 0 auto;
  gap: 32px;
}

.headline {
  text-align: center;
  margin-bottom: 10px;
}

.title-primary {
  font-size: 2.5rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

/* Search Box */
.search-filter {
  display: flex;
  justify-content: center;
  gap: 12px;
  max-width: 600px;
  margin: 0 auto;
  width: 100%;
}

.search-box {
  position: relative;
  flex: 1;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  color: var(--text-muted);
  font-size: 1.2rem;
}

.search-input {
  width: 100%;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-md);
  padding: 14px 16px 14px 44px;
  color: var(--text-primary);
  font-size: 1rem;
  outline: none;
  transition: var(--transition-smooth);
}

.search-input:focus {
  border-color: var(--accent-primary);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  background: rgba(255, 255, 255, 0.05);
}

.btn-primary {
  background: var(--accent-primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-md);
  padding: 0 24px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition-smooth);
}

.btn-primary:hover {
  background: #2563eb;
  transform: translateY(-2px);
  box-shadow: var(--shadow-glow);
}

/* Searched Container layout */
.searched-container {
  display: flex;
  justify-content: center;
}

/* Grid Layout */
.stock-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

/* Cards */
.stock-card {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
  cursor: pointer;
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-lg);
  transition: var(--transition-smooth);
  position: relative;
  overflow: hidden;
}

.stock-card:hover {
  transform: translateY(-5px);
  border-color: var(--accent-primary);
  box-shadow: var(--shadow-glow);
}

.logo-symbol {
  display: flex;
  align-items: center;
  gap: 16px;
}

.logo-symbol img {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  background: rgba(255, 255, 255, 0.1);
  padding: 2px;
}

.logo-symbol h2 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  color: var(--text-primary);
}

.price-change {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.price {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  line-height: 1;
}

.change-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 600;
  color: #fff;
}

.price-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid var(--border-glass);
}

.info-block {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-block .label {
  font-size: 0.8rem;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.info-block .value {
  font-size: 1.1rem;
  color: var(--text-secondary);
  font-weight: 500;
}

/* Loading & Load More */
.loading-state {
  display: flex;
  justify-content: center;
  padding: 60px 0;
}

.load-more {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--border-glass);
  border-radius: 50px;
  padding: 12px 32px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition-smooth);
}

.btn-secondary:hover {
  border-color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.05);
}

@media (max-width: 600px) {
  .search-filter {
    flex-direction: column;
  }
  .btn-primary {
    padding: 14px;
  }
}
</style>
