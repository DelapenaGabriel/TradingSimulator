<template>
    <div class="dashboard-container">
        <header><nav-bar/></header>

            <div class="overview-container">
                <balance-market/>
            </div>
            <div class="log"><trade-log/></div>

        <footer><footer-bar/></footer>
        
       

    </div>
</template>

<script>
import NavBar from '../components/NavBar.vue'
import FooterBar from '../components/FooterBar.vue'
import BalanceMarket from '../components/BalanceMarket.vue';
import TradeLog from '../components/TradeLog.vue';
export default{
components:{
    NavBar,
    FooterBar,
    BalanceMarket,
    TradeLog
}
}
</script>

<style scoped>

</style>