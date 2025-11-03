<template>
  <div class="container">
    <form class="profile-view" @submit.prevent="triggerSave">
      <div class="header-logo">
        <h2>Edit Profile</h2>
        <div class="upload-avatar">
          <img
            :src="profile.avatarUrl"
            width="80"
            height="80"
            @click="clickFileUpload"
          />
          <input
            type="file"
            ref="fileInput"
            @change="imageToURL($event)"
            style="display: none"
          />
        </div>
      </div>
      <div class="name">
        <label for="name">Full Name</label>
        <input type="text" id="name" v-model="profile.name" required/>
      </div>
      <div class="contact-number">
        <label for="number">Contact Number</label>
        <input
          type="text"
          maxlength="17"
          id="number"
          v-model="profile.phoneNumber"
          required
        />
      </div>
      <div class="address">
        <label for="address">Address</label>
        <input type="text" id="address" v-model="profile.address" required/>
      </div>
      <div class="city-state">
        <div class="city">
          <label for="city">City</label>
          <input type="text" id="city" v-model="profile.city" required/>
        </div>
        <div class="state">
          <label for="state">State</label>
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
        </div>
      </div>
      <div class="zip">
        <label for="zipcode">Zip Code</label>
        <input
          type="text"
          maxlength="5"
          id="zipcode"
          v-model="profile.zipCode"
        />
      </div>
      <div class="button">
        <button class="cancel" @click="triggerCancel">Cancel</button>
        <button class="save" type="submit">Save</button>
      </div>
      <i class='bx bxs-check-circle' style='color:#32de2d' v-if="isUpdated" > Profile Updated Successfully!</i>
    </form>
  </div>
</template>

<script>
import profileService from "../services/ProfileService";
export default {
  data() {
    return {
      profile: {
        id:"",
        name:"",
        phoneNumber:"",
        address:"",
        city:"",
        state:"",
        zipCode:"",
        avatarUrl:""
      },
      isUpdated:false,
    };
  },
  created() {
    this.getProfile();
  },
  methods: {
    getProfile() {
      profileService
        .getCurrentProfile()
        .then((response) => {
          if (response.status == 200) {
            this.profile.id = response.data.id;
            this.profile.name = response.data.name;
            this.profile.phoneNumber = response.data.phoneNumber;
            this.profile.address = response.data.address;
            this.profile.city =response.data.city;
            this.profile.state =response.data.state;
            this.profile.zipCode =response.data.zipCode;
            this.profile.avatarUrl =response.data.avatarUrl;
          }
        })
        .catch((error) => {
          console.log("Error Occurred retrieving profile.", error);
        });
    },
    imageToURL(event) {
      const file = event.target.files[0];

      if (file) {
        const imageUrl = URL.createObjectURL(file);
        this.profile.avatarUrl = imageUrl;
      }
    },
    clickFileUpload() {
      this.$refs.fileInput.click();
    },
    triggerCancel(){
        this.$router.push({name:"home"});
    },
    triggerSave(){
        const updateProfile = {
            name:this.profile.name,
            phoneNumber: this.profile.phoneNumber,
            address: this.profile.address,
            city: this.profile.city,
            state: this.profile.state,
            zipCode: this.profile.zipCode,
            avatarUrl:this.profile.avatarUrl
        }

            profileService.updateProfile(this.profile.id, updateProfile).then((response)=>{
                    this.isUpdated = true;
            }).catch(error =>{
                console.error("Error Occurred updating profile.", error);
            });
        },
  },
};
</script>

<style scoped>
.container {
 min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.profile-view {
  height: 100%;
  max-width: 1200px;
  width: 100%;
  padding: 30px 50px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  padding-bottom: 90px;
}

.header-logo {
  gap: 70px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-logo h2 {
  font-size: 35px;
  margin: 0;
}

img {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  object-fit: cover;
  margin-bottom: 10px;
}

img:hover{
    cursor: pointer;
}

.name,
.contact-number,
.address,
.zip {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

input[type="text"],
select {
  padding: 10px 20px;
  font-size: 18px;
  border-radius: 5px;
  border: 2px solid #858585;
  outline: none;
}

label {
  font-size: 20px;
  font-weight: 600;
}

.city-state {
  display: flex;
  justify-content: space-between;
}
.city,
.state {
  display: flex;
  flex-direction: column;
  gap: 5px;
  flex: 1;
}

.city input {
  margin-right: 50px;
}

.button {
  display: flex;
  align-items: center;
  gap: 30px;
}
.save,
.cancel {
  width: 180px;
  height: 55px;
}

.save:hover,
.cancel:hover {
  background-color: #172a3c;
  cursor: pointer;
  transition: all 0.3 ease;
}


.cancel:active {
    background-color: #ffffff;
}

.save:active {
    background-color: #0575e6;
}
.save {
  background-color: #0575e6;
  color: white;
  font-size: 20px;
  font-weight: 600;
  padding: 5px 20px;
  border-radius: 5px;
  border: 2px solid #0575e6;
}

.cancel {
  background-color: #ffffff;
  color: #0575e6;
  font-size: 20px;
  font-weight: 600;
  padding: 5px 20px;
  border-radius: 5px;
  border: 2px solid #0575e6;
}

.zip {
  margin-bottom: 20px;
}

i{
  margin-top: 40px;
}

@media screen and (max-width: 663px) {
  .city-state {
    flex-direction: column;
    gap: 10px;
  }
  .city input {
    margin-right: 0;
  }
}

@media screen and (max-width: 425px) {
    
  img {
    width: 60px;
    height: 60px;
  }
}
</style>
