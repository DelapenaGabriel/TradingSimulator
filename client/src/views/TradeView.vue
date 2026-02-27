<template>
  <div class="trading-container">
    <nav-bar />
    <trading-box @trade-placed="refreshLog" />
    <div class="log-section">
      <trade-log ref="tradeLog" />
    </div>
    <footer-bar />
  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";
import TradeLog from "../components/TradeLog.vue";
import FooterBar from "../components/FooterBar.vue";
import TradingBox from "../components/TradingBox.vue";
export default {
  components: {
    TradingBox,
    NavBar,
    TradeLog,
    FooterBar,
  },
  methods: {
    refreshLog() {
      if (this.$refs.tradeLog) {
        this.$refs.tradeLog.getTrades();
      }
    },
  },
};
</script>

<style scoped>
.trading-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
}

.log-section {
  padding: 0 24px 48px;
  width: 100%;
}
</style>
