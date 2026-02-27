<template>
  <nav class="nav-wrapper">
    <div class="nav-container glass-panel">
      <div class="logo">
        <router-link :to="{ name: 'home' }"
          ><img src="../assets/logo.png" alt="Logo"
        /></router-link>
      </div>

      <div class="nav-links">
        <ul>
          <li><router-link :to="{ name: 'home' }">Dashboard</router-link></li>
          <li><router-link :to="{ name: 'trade' }">Trade</router-link></li>
          <li><router-link :to="{ name: 'stocks' }">Stocks</router-link></li>
          <li><router-link :to="{ name: 'news' }">News</router-link></li>
        </ul>
      </div>

      <div class="nav-profile" @mouseover="showMenu" @mouseleave="hideMenu">
        <img
          :src="
            profile.avatarUrl
              ? profile.avatarUrl
              : `../assets/default_avatar.jpg`
          "
          class="avatar"
        />
        <div class="menu glass-panel" v-if="isMenuVisible">
          <router-link :to="{ name: 'edit-profile' }">Edit Profile</router-link>
          <router-link :to="{ name: 'logout' }">Logout</router-link>
        </div>
      </div>
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
      profileService
        .getCurrentProfile()
        .then((response) => {
          if (response.status == 200) {
            this.profile = response.data;
          }
        })
        .catch((error) => {
          console.error("Error Occurred retrieving profile.", error);
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
.nav-wrapper {
  display: flex;
  justify-content: center;
  padding: 24px 24px 0 24px;
  position: relative;
  z-index: 1000;
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  padding: 8px 24px;
  border-radius: 50px; /* Pill shape */
}

/* Adjust logo size to fit pill */
.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 48px;
  width: auto;
  object-fit: contain;
}

/* Links */
.nav-links ul {
  display: flex;
  gap: 32px;
  list-style: none;
  align-items: center;
  margin: 0;
  padding: 0;
}

.nav-links a {
  text-decoration: none;
  color: var(--text-secondary);
  font-weight: 500;
  font-size: 0.95rem;
  transition: var(--transition-smooth);
}

.nav-links a:hover,
.nav-links a.router-link-exact-active {
  color: var(--text-primary);
  text-shadow: 0 0 8px rgba(255, 255, 255, 0.4);
}

/* Profile */
.nav-profile {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
}

/* Invisible bridge to keep hover active when moving to dropdown */
.nav-profile::after {
  content: "";
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  height: 20px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid transparent;
  transition: var(--transition-smooth);
}

.nav-profile:hover .avatar {
  border-color: var(--accent-primary);
  box-shadow: 0 0 10px rgba(59, 130, 246, 0.3);
}

/* Dropdown Menu */
.menu {
  position: absolute;
  top: calc(100% + 10px);
  right: 0;
  min-width: 160px;
  display: flex;
  flex-direction: column;
  padding: 8px;
  border-radius: var(--radius-sm);
}

.menu a {
  padding: 10px 16px;
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: 6px;
  transition: var(--transition-smooth);
  font-size: 0.9rem;
}

.menu a:hover {
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-primary);
}

/* Responsive */
@media (max-width: 768px) {
  .nav-wrapper {
    padding: 16px;
  }
  .nav-container {
    flex-direction: column;
    border-radius: var(--radius-md);
    padding: 16px;
    gap: 16px;
  }
  .nav-links ul {
    gap: 16px;
    flex-wrap: wrap;
    justify-content: center;
  }
  .nav-profile {
    align-self: center;
  }
}
</style>
