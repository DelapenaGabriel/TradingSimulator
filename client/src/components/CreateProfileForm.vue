<template>
  <div class="profile">
    <div class="headline-background">
      <h1>CREATE YOUR PROFILE!</h1>
    </div>
    <div class="create-container">
      <form class="profile-form" @submit.prevent="createProfile(profile)">
        <div class="left-side">
          <p><span>*</span>Indicates mandatory fields</p>
          <label for="name">Name<span>*</span></label>
          <input
            type="text"
            id="name"
            maxlength="50"
            v-model="profile.name"
            required
          />

          <label for="number">Phone Number<span>*</span></label>
          <input
            type="text"
            id="number"
            maxlength="20"
            v-model="profile.phoneNumber"
            required
          />

          <label for="address">Street Address<span>*</span></label>
          <input
            type="text"
            id="address"
            maxlength="50"
            v-model="profile.address"
            required
          />

          <label for="city">City<span>*</span></label>
          <input
            type="text"
            id="city"
            maxlength="20"
            v-model="profile.city"
            required
          />

          <label for="state">State<span>*</span></label>
          <select
            class="form-select"
            aria-label="Default select example"
            v-model="profile.state"
            required
          >
            <option selected disabled value="">Select State</option>
            <option value="AL">Alabama</option>
            <option value="AK">Alaska</option>
            <option value="AZ">Arizona</option>
            <option value="AR">Arkansas</option>
            <option value="CA">California</option>
            <option value="CO">Colorado</option>
            <option value="CT">Connecticut</option>
            <option value="DE">Delaware</option>
            <option value="DC">District Of Columbia</option>
            <option value="FL">Florida</option>
            <option value="GA">Georgia</option>
            <option value="HI">Hawaii</option>
            <option value="ID">Idaho</option>
            <option value="IL">Illinois</option>
            <option value="IN">Indiana</option>
            <option value="IA">Iowa</option>
            <option value="KS">Kansas</option>
            <option value="KY">Kentucky</option>
            <option value="LA">Louisiana</option>
            <option value="ME">Maine</option>
            <option value="MD">Maryland</option>
            <option value="MA">Massachusetts</option>
            <option value="MI">Michigan</option>
            <option value="MN">Minnesota</option>
            <option value="MS">Mississippi</option>
            <option value="MO">Missouri</option>
            <option value="MT">Montana</option>
            <option value="NE">Nebraska</option>
            <option value="NV">Nevada</option>
            <option value="NH">New Hampshire</option>
            <option value="NJ">New Jersey</option>
            <option value="NM">New Mexico</option>
            <option value="NY">New York</option>
            <option value="NC">North Carolina</option>
            <option value="ND">North Dakota</option>
            <option value="OH">Ohio</option>
            <option value="OK">Oklahoma</option>
            <option value="OR">Oregon</option>
            <option value="PA">Pennsylvania</option>
            <option value="RI">Rhode Island</option>
            <option value="SC">South Carolina</option>
            <option value="SD">South Dakota</option>
            <option value="TN">Tennessee</option>
            <option value="TX">Texas</option>
            <option value="UT">Utah</option>
            <option value="VT">Vermont</option>
            <option value="VA">Virginia</option>
            <option value="WA">Washington</option>
            <option value="WV">West Virginia</option>
            <option value="WI">Wisconsin</option>
            <option value="WY">Wyoming</option>
          </select>

          <label for="zip">Zip Code<span>*</span></label>
          <input
            type="text"
            id="zip"
            maxlength="5"
            v-model="profile.zipCode"
            required
          />
        </div>

        <div class="right-side">
          <div class="upload-avatar">
            <img
              :src="profile.avatarUrl"
              width="80"
              height="80"
              alt="default avatar"
            />
            <input type="file" @change="imageToURL($event)" />
          </div>

          <div class="state-country"></div>
          <button type="submit">Continue</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import profileService from "../services/ProfileService";
import accountService from "../services/AccountService";
import axios from "axios";
export default {
  data() {
    return {
      profile: {
        name: "",
        phoneNumber: "",
        address: "",
        city: "",
        state: "",
        zipCode: "",
        avatarUrl:
          "https://static.vecteezy.com/system/resources/previews/009/292/244/non_2x/default-avatar-icon-of-social-media-user-vector.jpg",
      },
      account: {},
    };
  },
  methods: {
    createProfile(profile) {
      profileService
        .createProfile(profile)
        .then((response) => {
          if (response.status == 201) {
            this.retrieveAccount();
          }
        })
        .catch((error) => {
          console.error("Error occurred creating profile.", error);
        });
    },
    imageToURL(event) {
      const file = event.target.files[0];
      if (!file) {
        console.log("No file selected");
        return;
      }

      console.log("Uploading file:", file.name);

      const formData = new FormData();
      formData.append("file", file);
      formData.append("upload_preset", "tradesim_images"); // your unsigned preset

      axios
        .post(
          "https://api.cloudinary.com/v1_1/dujc4iuu8/image/upload",
          formData
        )
        .then((response) => {
          console.log("Cloudinary response:", response.data); // debug
          this.profile.avatarUrl = response.data.secure_url;
        })
        .catch((error) => {
          console.error(
            "Error uploading image to Cloudinary:",
            error.response?.data || error
          );
        });
    },
    retrieveAccount() {
      accountService
        .getAccount()
        .then((response) => {
          this.account = response.data;
          if (this.account.id != null || this.account.id != undefined) {
            this.$router.push({ name: "home" });
          } else {
            this.$router.push({ name: "create-account" });
          }
        })
        .catch((error) => {
          console.log("There was an error retrieving account", error);
        });
    },
  },
};
</script>

<style scoped>
.profile {
  margin: 30px 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.headline-background {
  background-image: linear-gradient(#0575e6, #02298a, #021b79);
  max-width: 1024px;
  width: 100%;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

h1 {
  font-size: 32px;
  color: white;
}

.profile-form {
  background-color: white;
  display: flex;
  justify-content: center;
  width: 100%;
  max-width: 1024px;
  height: 100%;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  padding: 50px;
}

.left-side,
.right-side {
  display: flex;
  flex-direction: column;
  width: 400px;
}

.left-side,
.right-side {
  color: black;
  font-weight: 700;
}

.left-side p {
  margin-top: 5px;
  font-weight: 500;
  color: grey;
}

.left-side label {
  padding-bottom: 15px;
  padding-top: 40px;
}

.left-side input,
.right-side input,
select {
  border: 1px solid #bbbbbb;
  border-radius: 5px;
  font-weight: 400;
  outline: none;
  text-align: center;
  padding: 3px;
}

span {
  color: rgb(229, 9, 9);
  font-weight: 600;
}

.right-side {
  justify-content: center;
  margin-left: 6vw;
}

.upload-avatar {
  display: flex;
  align-items: end;
  margin-bottom: 20px;
}

.upload-avatar img {
  border-radius: 50%;
  margin-right: 5px;
  object-fit: cover;
}

.upload-avatar input {
  width: 255px;
  border: none;
}

button {
  background-color: #0575e6;
  color: white;
  border-radius: 30px;
  padding: 15px;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

button:hover {
  background-color: #02298a;
}

button:focus {
  outline: none;
}

select {
  text-align: center;
  padding: 3px;
}

.state-country {
  display: flex;
  flex-direction: column;
  margin-bottom: 150px;
}

input:focus,
select:focus {
  border-color: #0575e6;
}

@media screen and (max-width: 1044px) {
  .headline-background,
  .profile-form {
    width: 90vw;
    height: 100%;
  }
  .left-side,
  .right-side {
    width: 100vw;
  }
  .upload-avatar {
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .profile-form,
  .headline-background {
    width: 95vw;
  }
}

@media screen and (max-width: 550px) {
  .left-side,
  .right-side {
    width: 40%;
  }

  .upload-avatar {
    margin-top: 50px;
  }

  h1 {
    font-size: 25px;
  }

  button {
    padding: 5px;
  }
}
</style>
