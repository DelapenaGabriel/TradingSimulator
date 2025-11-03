<template>
  <div id="register">
    <div class="signup-poster">
      <h1>Trading Simulator</h1>
      <p>Trade Smart, Risk-Free, Master the Market with Our Simulator!</p>
    </div>
    <form v-on:submit.prevent="register" id="signup-form">
      <h2>Create Account!</h2>
      <h3>Sign Up to Get Started</h3>
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
        <input
          type="password"
          id="confirmPassword"
          placeholder="Confirm Password"
          v-model="user.confirmPassword"
          required
        />
        <div></div>
        <div>
          <button type="submit">Register</button>
        </div>
      </div>
      <hr />
      <div class="login-instead">
        Have an account?
        <router-link v-bind:to="{ name: 'login' }">Login instead!</router-link>
      </div>
    </form>
  </div>
</template>

<style scoped>
#register {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  width: 100%;
}
#fields {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
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
  margin-bottom: 10px;
  outline: none;
}
#confirmPassword {
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
.signup-poster {
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

h2 {
  font-weight: bold;
  font-size: 45px;
  padding-bottom: 10px;
  text-align: center;
}

h3 {
  font-size: 20px;
  font-weight: 500;
  padding-bottom: 30px;
  text-align: center;
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

#signup-form {
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

@media screen and (max-width: 643px) {
  .signup-poster {
    display: none;
  }

  #signup-form {
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
    width: 95vw;
  }
}

@media screen and (max-width: 506px) {
  h2 {
    font-size: 33px;
  }
  h3 {
    font-size: 16px;
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



</style>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "user",
      },
    };
  },
  methods: {
    error(msg) {
      alert(msg);
    },
    success(msg) {
      alert(msg);
    },
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.error("Password & Confirm Password do not match");
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.success("Thank you for registering, please sign in.");
              this.$router.push({
                path: "/login",
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            if (!response) {
              this.error(error);
            } else if (response.status === 400) {
              if (response.data.errors) {
                // Show the validation errors
                let msg = "Validation error: ";
                for (let err of response.data.errors) {
                  msg += `'${err.field}':${err.defaultMessage}. `;
                }
                this.error(msg);
              } else {
                this.error(response.data.message);
              }
            } else {
              this.error(response.data.message);
            }
          });
      }
    },
  },
};
</script>
