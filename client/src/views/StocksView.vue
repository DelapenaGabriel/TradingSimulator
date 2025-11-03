<template>
    <div class="container">
        <nav><nav-bar/></nav>
        <main><stock-tracker/></main>
        <footer><footer-bar/></footer>
    </div>
</template>

<script>
import StockTracker from '../components/StockTracker.vue';
import NavBar from '../components/NavBar.vue';
import FooterBar from '../components/FooterBar.vue'
export default{
    components:{
        StockTracker,
        NavBar,
        FooterBar
    }
}
</script>