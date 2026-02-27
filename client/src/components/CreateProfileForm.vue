<template>
  <div class="profile-wrapper">
    <!-- Animated background orb -->
    <div class="bg-orb"></div>

    <div class="form-container">
      <form
        class="profile-card glass-panel"
        @submit.prevent="createProfile(profile)"
      >
        <!-- Subtle Animated Highlight -->
        <div class="card-highlight"></div>

        <div class="card-header">
          <div class="icon-wrapper">
            <i class="bx bx-user-circle"></i>
          </div>
          <h1 class="title-primary">Create Your Profile</h1>
          <p class="subtitle">Complete your account registration</p>
        </div>

        <div class="form-grid">
          <div class="left-side">
            <p class="note"><span>*</span> Required fields</p>

            <div class="input-group">
              <label>Full Name <span>*</span></label>
              <input
                type="text"
                maxlength="50"
                v-model="profile.name"
                class="custom-input"
                placeholder="e.g. Satoshi Nakamoto"
                required
              />
            </div>

            <div class="input-group">
              <label>Phone Number <span>*</span></label>
              <input
                type="text"
                maxlength="20"
                v-model="profile.phoneNumber"
                class="custom-input"
                placeholder="(555) 123-4567"
                required
              />
            </div>

            <div class="input-group">
              <label>Street Address <span>*</span></label>
              <input
                type="text"
                maxlength="50"
                v-model="profile.address"
                class="custom-input"
                placeholder="123 Block Chain Ave"
                required
              />
            </div>

            <div class="city-state-zip">
              <div class="input-group city">
                <label>City <span>*</span></label>
                <input
                  type="text"
                  maxlength="20"
                  v-model="profile.city"
                  class="custom-input"
                  placeholder="Metropolis"
                  required
                />
              </div>

              <div class="input-group state">
                <label>State <span>*</span></label>
                <select v-model="profile.state" class="custom-select" required>
                  <option disabled value="">State</option>
                  <option v-for="s in states" :key="s" :value="s">
                    {{ s }}
                  </option>
                </select>
              </div>

              <div class="input-group zip">
                <label>Zip <span>*</span></label>
                <input
                  type="text"
                  maxlength="5"
                  v-model="profile.zipCode"
                  class="custom-input"
                  placeholder="12345"
                  required
                />
              </div>
            </div>
          </div>

          <div class="right-side">
            <div class="avatar-section">
              <div class="avatar-block">
                <img :src="profile.avatarUrl" alt="avatar" />
              </div>
              <label class="upload-btn">
                <i class="bx bx-upload"></i> Upload Image
                <input type="file" @change="imageToURL($event)" />
              </label>
            </div>

            <button type="submit" class="submit-btn">
              <span>Continue setup</span>
              <i class="bx bx-right-arrow-alt"></i>
            </button>
          </div>
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
      states: [
        "AL",
        "AK",
        "AZ",
        "AR",
        "CA",
        "CO",
        "CT",
        "DE",
        "DC",
        "FL",
        "GA",
        "HI",
        "ID",
        "IL",
        "IN",
        "IA",
        "KS",
        "KY",
        "LA",
        "ME",
        "MD",
        "MA",
        "MI",
        "MN",
        "MS",
        "MO",
        "MT",
        "NE",
        "NV",
        "NH",
        "NJ",
        "NM",
        "NY",
        "NC",
        "ND",
        "OH",
        "OK",
        "OR",
        "PA",
        "RI",
        "SC",
        "SD",
        "TN",
        "TX",
        "UT",
        "VT",
        "VA",
        "WA",
        "WV",
        "WI",
        "WY",
      ],

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
          if (response.status === 201) {
            this.retrieveAccount();
          }
        })
        .catch((err) => console.error("Error creating profile:", err));
    },

    imageToURL(event) {
      const file = event.target.files[0];
      if (!file) return;

      this.profile.avatarUrl = URL.createObjectURL(file);

      const formData = new FormData();
      formData.append("file", file);
      formData.append("upload_preset", "tradesim");

      const cloudAxios = axios.create({
        headers: { "Content-Type": "multipart/form-data" },
      });

      delete cloudAxios.defaults.headers.common["Authorization"];

      cloudAxios
        .post(
          "https://api.cloudinary.com/v1_1/dujc4iuu8/image/upload",
          formData,
        )
        .then((res) => {
          this.profile.avatarUrl = res.data.secure_url;
        })
        .catch((error) =>
          console.error("Cloudinary Upload Error:", error.response?.data),
        );
    },

    retrieveAccount() {
      accountService
        .getAccount()
        .then((res) => {
          this.account = res.data;
          if (this.account.id) {
            this.$router.push({ name: "home" });
          } else {
            this.$router.push({ name: "create-account" });
          }
        })
        .catch((error) => console.log("Error retrieving account", error));
    },
  },
};
</script>

<style scoped>
/* --- PAGE WRAPPER --- */
.profile-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 100px);
  padding: 40px 24px;
  overflow: hidden;
}

/* Background animated glow */
.bg-orb {
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  background: radial-gradient(
    circle,
    rgba(59, 130, 246, 0.1) 0%,
    rgba(59, 130, 246, 0) 70%
  );
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 0;
  animation: pulse 5s ease-in-out infinite alternate;
  pointer-events: none;
}

@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(0.9);
    opacity: 0.5;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.1);
    opacity: 1;
  }
}

/* --- CARD --- */
.form-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 900px;
}

.profile-card {
  padding: 48px;
  display: flex;
  flex-direction: column;
  gap: 32px;
  border-radius: var(--radius-lg);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(15, 23, 42, 0.6);
  backdrop-filter: blur(20px);
}

/* Subtle top border highlight */
.card-highlight {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(
    90deg,
    transparent,
    var(--accent-primary),
    transparent
  );
  opacity: 0.6;
}

/* HEADER BAR */
.card-header {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.icon-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: rgba(59, 130, 246, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  border: 1px solid rgba(59, 130, 246, 0.2);
  box-shadow: 0 0 30px rgba(59, 130, 246, 0.15);
}

.icon-wrapper i {
  font-size: 2rem;
  color: var(--accent-primary);
}

.title-primary {
  font-size: 2.2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  letter-spacing: -0.02em;
}

.subtitle {
  color: var(--text-muted);
  font-size: 1.05rem;
  margin: 0;
}

/* FORM GRID */
.form-grid {
  display: flex;
  gap: 60px;
  justify-content: space-between;
}

.left-side {
  flex: 1.5;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.right-side {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.note {
  font-size: 0.9rem;
  color: var(--text-muted);
  margin: 0 0 8px 0;
}

.note span {
  color: var(--accent-secondary);
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.input-group label {
  font-size: 0.95rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.input-group label span {
  color: var(--accent-secondary);
}

.city-state-zip {
  display: flex;
  gap: 16px;
}

.city {
  flex: 2;
}
.state {
  flex: 1.5;
}
.zip {
  flex: 1;
}

.custom-input,
.custom-select {
  width: 100%;
  padding: 14px 16px;
  border-radius: var(--radius-md);
  border: 1px solid var(--border-glass);
  background: rgba(0, 0, 0, 0.2);
  color: var(--text-primary);
  outline: none;
  font-size: 1rem;
  transition: var(--transition-smooth);
}

.custom-input::placeholder {
  color: rgba(255, 255, 255, 0.15);
}

.custom-input:focus,
.custom-select:focus {
  border-color: var(--accent-primary);
  background: rgba(0, 0, 0, 0.4);
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.custom-select option {
  color: #000;
}

/* AVATAR UPLOAD ------------------------ */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  gap: 24px;
}

.avatar-block img {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid rgba(59, 130, 246, 0.3);
  padding: 6px;
  background: rgba(0, 0, 0, 0.2);
  box-shadow: 0 0 40px rgba(59, 130, 246, 0.1);
  transition: var(--transition-smooth);
}

.avatar-block img:hover {
  transform: scale(1.02);
  border-color: rgba(59, 130, 246, 0.5);
}

.upload-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-glass);
  border-radius: var(--radius-full);
  color: var(--text-primary);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition-smooth);
  font-size: 0.95rem;
}

.upload-btn input {
  display: none;
}

.upload-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

/* SUBMIT BUTTON */
.submit-btn {
  width: 100%;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: linear-gradient(135deg, var(--accent-primary), #1d4ed8);
  color: #fff;
  border: none;
  border-radius: var(--radius-md);
  font-size: 1.15rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(59, 130, 246, 0.3);
  overflow: hidden;
  position: relative;
  margin-top: 24px;
}

.submit-btn::after {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 50%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.2),
    transparent
  );
  transition: left 0.6s ease;
  transform: skewX(-20deg);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.5);
}

.submit-btn:hover::after {
  left: 200%;
}

.submit-btn i {
  font-size: 1.5rem;
  transition: transform 0.3s ease;
}

.submit-btn:hover i {
  transform: translateX(6px);
}

@media (max-width: 900px) {
  .form-grid {
    flex-direction: column;
    gap: 48px;
  }

  .avatar-section {
    margin-bottom: 24px;
  }

  .avatar-block img {
    width: 150px;
    height: 150px;
  }
}

@media (max-width: 600px) {
  .profile-card {
    padding: 32px 24px;
  }

  .city-state-zip {
    flex-direction: column;
  }
}
</style>
