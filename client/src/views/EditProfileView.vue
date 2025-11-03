<template>
    <div class="container">

        <nav><nav-bar/></nav>
        <main><edit-profile/></main>
        <footer><footer-bar/></footer>
    </div>
</template>

<script>
import EditProfile from '../components/EditProfile.vue';
import NavBar from '../components/NavBar.vue';
import FooterBar from '../components/FooterBar.vue';
export default{
    components:{

        EditProfile,
        NavBar,
        FooterBar
    }
}
</script>