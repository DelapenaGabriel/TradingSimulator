<template>
  <div class="overview">
    <div class="balance-container">
      <div class="balance-account-number">
        <h2>Balance</h2>
        <h3>Account #:{{ account.id }}</h3>
      </div>
      <div class="total-balance">
        <span>$</span
        ><input
          type="text"
          id="balance"
          @change="updateBalance"
          v-model="editBalance.balance"
        />
      </div>
    </div>
    <div class="market-container">
      <div class="market-headline">
        <h2>MARKET SNAPSHOT</h2>
      </div>
      <div class="market-cards-container" v-if="!isloading">
        <div class="cards" v-for="stock in fetchStockCards" :key="stock.symbol" @click="routeToStockOverview(stock.symbol)">
          <div class="symbol">
            <div class="logo-symbol">
              <img :src="stock.logo" />
              <p>{{ stock.symbol }}</p>
            </div>
          </div>
          <div class="price-change">
            <p class="price">${{ stock.price }}</p>
            <p
              class="change"
              :style="{ color: stock.percentChange < 0 ? '#DC2027' : 'green' }"
            >
              {{
                stock.percentChange > 0
                  ? "+" + stock.percentChange
                  : stock.percentChange
              }}%
            </p>
          </div>
        </div>
      </div>
      <div v-else class="loading">
        <loading-spinner id="spinner" v-bind:spin="isloading" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.overview {
  background-color: #031224;
  display: flex;
  height: 100%;
  padding: 10px;
  gap: 20px;
}
.balance-container {
  display: flex;
  gap: 20px;
  width: 100%;
  max-width: 600px;
}
.balance-account-number {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
h2 {
  text-transform: uppercase;
  font-size: 30px;
  color: #e0e0e0;
  margin: 0;
}
h3 {
  color: #e0e0e0;
  font-size: 15px;
  font-weight: 200;
}

.total-balance {
  display: flex;
  align-items: center;
  justify-content: center;
}
.total-balance span {
  color: #e0e0e0;
  font-size: 41px;
  font-weight: 500;
}

input#balance {
  background-color: #031224;
  color: #e0e0e0;
  font-weight: 500;
  font-size: 35px;
  border: none;
  outline: none;
  text-align: start;
  margin: 0;
  width: 90%;
}

.market-container {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.market-headline {
  margin-bottom: 10px;
  display: flex;
}
.market-headline h2 {
  margin-right: 10px;
}
.loading {
  color: #e0e0e0;
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;
}
.logo-symbol img {
  width: 30px;
  height: 30px;
  border-radius: 50px;
  overflow: hidden;
  object-fit: cover;
}
.market-cards-container {
  color: #e0e0e0;
  display: flex;
  align-items: center;
  gap: 50px;
}
.cards {
  border: 3px solid white;
  background-color: #2e3d50;
  width: 30%;
  border-radius: 13px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 5px;
}

.cards:hover{
  cursor: pointer;
}
.symbol {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.logo-symbol {
  width: 100%;
  display: flex;
  flex: 100%;
  align-items: center;
  justify-content: center;
  gap: 5px;
  margin-bottom: 50px;
}
.logo-symbol p {
  font-size: 38px;
  font-weight: 500;
  margin: 0;
}
.price {
  font-size: 25px;
}

.price-change {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

@media screen and (max-width: 1200px) {
  .logo-symbol p {
    font-size: 30px;
  }
}

@media screen and (max-width: 1200px) {
  .logo-symbol p {
    font-size: 25px;
  }
}

@media screen and (max-width: 875px) {
  .overview {
    flex-direction: column;
    gap: 40px;
  }

  .balance-container {
    width: 100%;
  }

  h2 {
    font-size: 25px;
  }

  .balance-account-number {
    width: 50%;
  }
}

@media screen and (max-width: 580px) {
  .logo-symbol p {
    font-size: 30px;
  }
}

@media screen and (max-width: 510px) {
  .logo-symbol p {
    font-size: 25px;
  }
  .price {
    font-size: 20px;
  }

  .market-cards-container {
    gap: 20px;
  }
}
</style>

<script>
import accountService from "../services/AccountService";
import stockService from "../services/StockService";
import LoadingSpinner from "../components/LoadingSpinner.vue";
export default {
  created() {
    this.getAccount();
    this.getSnapshotStocks();
  },
  computed:{
    fetchStockCards(){
      this.getSnapshotStocks;
      const cards = this.stockCards;
      return cards
    }
  },
  data() {
    return {
      account: {},
      stockCards: [],
      symbols: ["AAPL", "TSLA", "NVDA"],
      isloading: true,
      toggleSymbols:false,

      editBalance: {
        balance: "",
      },
    };
  },
  methods: {
    getAccount() {
      accountService.getAccount().then((response) => {
        if (response.status == 200) {
          this.account = response.data;
          this.editBalance.balance = response.data.balance;
        }
      }).catch(error =>{
        console.error("There was an error retrieving account.", error);
      });
    },
    getSnapshotStocks() {

    this.isloading = true;
    const promises = this.symbols.map((symbol)=>{
        return stockService.getStock(symbol);
    });
   
    Promise.all(promises).then((responses)=>{
        this.stockCards = responses.map(response=>{
            return response.data;
        })
    }).catch((error) => {
            console.error("Error fetching stocks", error);
          })
          .finally(() => {
            this.isloading = false;
          });
          
    },
    updateBalance() {
      accountService
        .updateAccount(this.account.id, this.editBalance)
        .then()
        .catch((error) => {
          console.log("Error updating balance", error);
        });
    },
    routeToStockOverview(symbol){
          this.$router.push({name:"stock-overview", params: {id: symbol}})
        }
  },
  components: {
    LoadingSpinner,
  },
};
</script>
