<template>
  <div class="dashboard-container">
    <header><nav-bar /></header>

    <main class="dashboard-main">
      <div class="bento-grid">
        <div class="bento-item-main glass-panel">
          <balance-market />
        </div>
        <div class="bento-item-side glass-panel">
          <trade-log />
        </div>
      </div>
    </main>

    <footer><footer-bar /></footer>
  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";
import FooterBar from "../components/FooterBar.vue";
import BalanceMarket from "../components/BalanceMarket.vue";
import TradeLog from "../components/TradeLog.vue";
export default {
  components: {
    NavBar,
    FooterBar,
    BalanceMarket,
    TradeLog,
  },
};
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.dashboard-main {
  flex: 1;
  padding: 40px 24px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
}

.bento-item-main {
  grid-column: span 8;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.bento-item-side {
  grid-column: span 4;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

@media (max-width: 1024px) {
  .bento-item-main,
  .bento-item-side {
    grid-column: span 12;
  }
}
</style>
