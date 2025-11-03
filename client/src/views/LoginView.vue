<template>
  <div id="login">
    <div class="login-poster">
      <h1>Trading Simulator</h1>
      <p>Trade Smart, Risk-Free, Master the Market with Our Simulator!</p>
    </div>

    <div class="login-form">
      <h2>Hello Again!</h2>
      <h3>Welcome Back</h3>
      <form v-on:submit.prevent="login">
        <div id="fields">
          <input
            type="text"
            id="username"
            placeholder="Username"
            v-model="user.username"
            required
            autofocus
            autocomplete="off"
          />
          <input
            type="password"
            id="password"
            placeholder="Password"
            v-model="user.password"
            required
          />
          <div><button type="submit">Login</button></div>
        </div>
      </form>
      <div class="register-here">
        Need an account?
        <router-link v-bind:to="{ name: 'register' }"
          >Register Here! {{ profile.id }}</router-link
        >
      </div>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";
import profileService from "../services/ProfileService";
import accountService from "../services/AccountService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      profile: {
        id:""
      },
      account: {
        id:""
      },
    };
  },
  methods: {
    getAccountProfile(){

      const fetchAccount = accountService.getAccount();
      const fetchProfile = profileService.getCurrentProfile();

      Promise.all([fetchAccount, fetchProfile]).then(([response1, response2])=>{
        this.account.id = response1.data.id;
        this.profile.id = response2.data.id;

        console.log(this.account.id, this.profile.id);
      }).catch((error)=>{
        console.error("An error occurred: ", error);
      }).finally(() => {
          if (!this.account.id && !this.profile.id) {
            this.$router.push({ name: "create-profile" });
          } else if (
            !this.account.id &&
            this.profile.id
          ) {
            this.$router.push({ name: "create-account" });
          } else {
            this.$router.push({ name: "home" });
          }
        });

    },
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);

            this.getAccountProfile();
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            alert(error);
          } else if (response.status === 401) {
            alert("Invalid username and password!");
          } else {
            alert(response.message);
          }
        });
    },
  },
};
</script>

<style scoped>
#login {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 100%;
}
#username {
  border: 1px solid #eeeeee;
  border-radius: 30px;
  width: 30vw;
  height: 5vh;
  padding: 15px;
  margin-bottom: 10px;
  outline: none;
}
#password {
  border: 1px solid #eeeeee;
  border-radius: 30px;
  width: 30vw;
  height: 5vh;
  padding: 15px;
  margin-bottom: 20px;
  outline: none;
}

button {
  border: none;
  border-radius: 30px;
  width: 30vw;
  height: 5vh;
  background-color: #0575e6;
  color: white;
  margin-bottom: 40px;
  font-size: 18px;
}
button:hover {
  background-color: #105ca9;
  cursor: pointer;
}

#fields {
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  font-weight: bold;
  font-size: 45px;
  padding-bottom: 10px;
}

h3 {
  font-size: 20px;
  font-weight: 500;
  padding-bottom: 30px;
}

.login-form {
  background-color: white;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  height: 80vh;
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 50px;
}

h1 {
  font-size: 55px;
  color: white;
  font-weight: bold;
}

p {
  color: white;
  text-align: center;
  width: 329px;
}

.login-poster {
  background-image: linear-gradient(#0575e6, #02298a, #021b79);
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  height: 80vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 700px;
}

@media screen and (max-width: 925px) {
  h2 {
    font-size: 30px;
  }
}

@media screen and (max-width: 876px) {
  h1 {
    font-size: 40px;
  }
}
@media screen and (max-width: 876px) {
  h1 {
    font-size: 35px;
  }
}
@media screen and (max-width: 643px) {
  .login-poster {
    display: none;
  }

  .login-form {
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
    width: 95vw;
  }
}
</style>
