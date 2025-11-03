import { createRouter as createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

// Import components
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import LogoutView from '../views/LogoutView.vue'
import RegisterView from '../views/RegisterView.vue'
import CreateProfileView from '../views/CreateProfileView.vue'
import CreateAccountView from '../views/CreateAccountView.vue'
import DashboardView from '../views/DashboardView.vue'
import TradeView from '../views/TradeView.vue'
import NewsView from '../views/NewsView.vue'
import StocksView from '../views/StocksView.vue'
import StockOverviewView from '../views/StockOverviewView.vue'
import EditProfileView from '../views/EditProfileView.vue'


/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */
const routes = [
    {
      path: '/',
      name: 'home',
      component: DashboardView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: LogoutView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: RegisterView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/create-profile",
      name: "create-profile",
      component: CreateProfileView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/create-account",
      name: "create-account",
      component: CreateAccountView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/trade",
      name: "trade",
      component: TradeView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/news",
      name: "news",
      component: NewsView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/stocks",
      name: "stocks",
      component: StocksView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/stocks/:id",
      name: "stock-overview",
      component: StockOverviewView,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/edit-profile",
      name: "edit-profile",
      component: EditProfileView,
      meta: {
        requiresAuth: true
      }
    },
  ];

// Create the router
const router = createRouter({
  history: createWebHistory(),
  routes: routes
});

router.beforeEach((to) => {

  // Get the Vuex store
  const store = useStore();

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return {name: "login"};
  }
  // Otherwise, do nothing and they'll go to their next destination
});

export default router;
