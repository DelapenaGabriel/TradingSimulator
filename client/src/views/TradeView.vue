<template>
  <div class="trading-container">
    <nav-bar />
    <trading-box/>
    <trade-log />
    <footer-bar/>
  </div>
</template>

<script>
import NavBar from "../components/NavBar.vue";
import TradeLog from "../components/TradeLog.vue";
import FooterBar from "../components/FooterBar.vue";
import TradingBox from "../components/TradingBox.vue"
export default {
  components: {
    TradingBox,
     NavBar,
     TradeLog,
     FooterBar,
  },
};
</script>

<style scoped>

</style>
