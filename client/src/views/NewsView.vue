<template>
    <div class="container">
        <nav><nav-bar/></nav>
        <main><news-section/></main>
        <footer><footer-bar/></footer>
    </div>
</template>

<script>
import NewsSection from '../components/NewsSection.vue';
import NavBar from '../components/NavBar.vue';
import FooterBar from '../components/FooterBar.vue';
export default{
    components:{
        NewsSection,
        NavBar,
        FooterBar
    }
}
</script>