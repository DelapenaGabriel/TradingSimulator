<template>
  <nav class="nav-container">
    <div class="logo">
      <router-link :to="{ name: 'home' }"><img src="../assets/logo.png" /></router-link>
    </div>

    <div class="nav-links">
      <ul>
        <li><router-link :to="{ name: 'home' }">Dashboard</router-link></li>
        <li><router-link :to="{ name: 'trade' }">Trade</router-link></li>
        <li><router-link :to="{ name: 'stocks' }">Stocks</router-link></li>
        <li><router-link :to="{ name: 'news' }">News</router-link></li>
        <li>
          <img :src="profile.avatarUrl? profile.avatarUrl : `../assets/default_avatar.jpg`" @mouseover="showMenu" @click="hideMenu" />
          <div class="menu" v-if="isMenuVisible">
            <router-link :to="{ name: 'edit-profile' }">Edit Profile</router-link>
            <router-link :to="{ name: 'logout' }">Logout</router-link>
          </div>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import profileService from "../services/ProfileService";
export default {
  created() {
    this.getProfile();
  },

  data() {
    return {
      profile: {},
      isMenuVisible: false,
    };
  },
  methods: {
    getProfile() {
      profileService.getCurrentProfile().then((response) => {
        if (response.status == 200) {
          this.profile = response.data;
        }
      }).catch(error =>{
        console.error("Error Occurred retrieving profile.", error)
      });
    },
    showMenu() {
      this.isMenuVisible = true;
    },
    hideMenu() {
      this.isMenuVisible = false;
    },
  },
};
</script>

<style scoped>
.nav-container {
  background-color: #031224;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  height: 125px;
}

.logo img {
  width: 125px;
  height: 125px;
}

.logo a {
  display: flex;
  align-items: center;
}

.menu a {
  font-weight: 400;
  color: black;
  display: block;
  text-decoration: none;
  text-align: center;
  padding: 10px;
}

.menu a:hover {
  border-radius: 5px;
  background-color: #f0f0f0;
}

.menu {
  display: block;
  position: relative;
  top: 70px;
  background-color: #ffffff;
  width: 120px;
  border-radius: 5px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;
  z-index: 9999;
}

h1 {
  color: #e0e0e0;
  font-size: 25px;
  font-weight: 500;
}
.logo a {
  text-decoration: none;
  color: #e0e0e0;
}

ul li {
  list-style: none;
  margin-left: 60px;
}

ul li a:hover {
  color: #2196f3;
  transition: all 0.3 ease;
}

ul li a {
  font-weight: 300;
  color: #e0e0e0;
  text-decoration: none;
}

.router-link-exact-active {
  color: #2196f3;
}

ul {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}

ul li:last-child{
  margin-left: 110px;
}

li img {
  width: 50px;
  height: 50px;
  border: 2px solid white;
  border-radius: 50px;
  object-fit: cover;
  overflow: hidden;
  position: absolute;
  right:30px;
  top:36px;
}

@media screen and (max-width:780px){
  .nav-container{
    flex-direction: column;
    justify-content: center;
    height:230px;
  }

  .logo img{
    width: 150px;
    height: auto;
  }

  li img{
    top:163px;
  }

  .menu{
    position: absolute;
    right:65px;
    top:205px;
  }
}
@media screen and (max-width:600px) {
  li img{
    right:40px;
    top:163px;
  }
  ul li{
    margin-left: 35px;
  }
  ul{
    justify-content: flex-start;
  }
}

@media screen and (max-width:499px){

  li img{
    width: 40px;
    height:40px;
    right:30px;
    top:167px;
  }
  ul li{
    margin-left: 20px;
  }
}

@media screen and (max-width:400px){

  ul{
    padding-left: 10px;
  }
ul li{
  margin-left: 10px;
}
}
</style>
