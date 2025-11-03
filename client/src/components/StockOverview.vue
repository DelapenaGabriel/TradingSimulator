<template>
  <div class="container">
    <div class="stock-card" v-if="!isLoading">
      <div class="headline">
        <img
          :src="
            stock.logo
              ? stock.logo
              : 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'
          "
        />
        <div class="symbol-info">
          <h1>{{ stock.symbol }}</h1>
          <h2>{{ stock.name }}</h2>
        </div>
      </div>
      <div class="description">
        <p class="label">Description</p>
          <p class="detail" v-if="stockDescription">{{ stockDescription }}</p>
          <p class="detail" v-else>No Current Description</p>
      </div>
      <div class="info">
        <div class="company-details">
          <p class="label">Website</p>
          <a :href="stock.url" target="_blank" class="detail">{{
            stock.url
          }}</a>
          <p class="label">Country</p>
          <p class="detail">{{ stock.country }}</p>
          <p class="label">Exchange</p>
          <p class="detail">{{ stock.exchange }}</p>
          <p class="label">IPO Date</p>
          <p>{{ stock.ipo }}</p>
        </div>
        <div class="left-price">
          <p class="label">Price</p>
          <p class="detail">${{ stock.price }}</p>
          <p class="label">Change</p>
          <p class="detail" :style="{ color: stock.percentChange < 0 ? '#DC2027' : 'green' }">
            {{
              stock.percentChange > 0
                ? "+" + stock.percentChange
                : stock.percentChange
            }}%
          </p>
          <p class="label">Open</p>
          <p class="detail">${{ stock.openPrice }}</p>
        </div>
        <div class="right-price">
          <p class="label">Low</p>
          <p class="detail">${{ stock.lowPrice }}</p>
          <p class="label">High</p>
          <p class="detail">${{ stock.highPrice }}</p>
          <p class="label">Close</p>
          <p class="detail">${{ stock.previousClose }}</p>
        </div>
      </div>
    </div>
    <div class="loading" v-if="isLoading">
      <loading-spinner id="spinner" :spin="isLoading" />
    </div>
    <div class="backout">
      <router-link :to="{ name: 'stocks' }">Back to stocks</router-link>
    </div>
  </div>
</template>

<script>
import stockService from "../services/StockService";
import LoadingSpinner from "./LoadingSpinner.vue";
export default {
  data() {
    return {
      stock: {},
      isLoading: false,
      stockDescription: null,
    };
  },
  created() {
    this.getStockProfile();
  },
  methods: {
    getStockProfile(){
      this.isLoading = true;
      const stockSymbol = this.$route.params.id;
      const fetchInfo = stockService.getStock(stockSymbol);
      const fetchDescription = stockService.getStockDescription(stockSymbol);

      Promise.all([fetchInfo, fetchDescription]).then(([response1, response2])=>{
        this.stock = response1.data;
        this.stockDescription = response2.data.results.description;
        this.isLoading = false;
      }).catch((error)=>{
        console.error("Error Occurred retrieving stock and description.", error);
      });
    }

  },
  components: {
    LoadingSpinner,
  },
};
</script>

<style scoped>
.container {
  background: radial-gradient(
    circle at 18.7% 37.8%,
    rgb(250, 250, 250) 0%,
    rgb(225, 234, 238) 90%
  );
  min-height: 100vh;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.stock-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  max-width: 646px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  margin-bottom: 20px;
  margin-top: 20px;
  overflow-x: hidden;
}

.headline {
  background-color: #70a1ff;
  background-image: linear-gradient(315deg, #70a1ff 0%, #c2c0c0 74%);
  border-radius: 16px 16px 0 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  min-height: 182px;
  padding: 30px;
  gap: 90px;
}

.headline img {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  object-fit: cover;
}

.headline h1 {
  padding: 0;
  font-size: 40px;
  color: white;
  margin: 0;
}
.headline h2 {
  padding: 0;
  margin: 0;
  color: white;
  font-size: 30px;
  font-weight: 500;
}

.info {
  width: 100%;
  height: 100%;
  padding: 20px 40px;
  display: flex;
  justify-content: space-between;
  overflow: hidden;
  gap: 10px;
}
.description{
  padding: 10px 40px;
  margin-top: 10px;
}
.company-details {
  height: 100%;
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.company-details a {
  color: #27a1ed;
  text-decoration: none;
  font-size: 16px;
  margin-bottom: 10px;
}
.label {
  color: #959595;
  font-size: 18px;
}
.detail {
  color: rgb(53, 52, 52);
  margin-bottom: 10px;
}

.left-price,
.right-price {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

a {
  color: #27a1ed;
}
.loading {
  margin-bottom: 10px;
}
.backout{
  margin-bottom: 20px;
}
@media screen and (max-width: 400px) {
  .headline {
    gap: 70px;
  }
}
</style>
