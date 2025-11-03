<template>
  <div class="trading-container">
    <div class="trade-box">
      <div class="search">
        <button @click="getStock">SEARCH</button>
        <input type="text" placeholder="Enter a symbol" v-model="inputStock" />
      </div>
      <div class="stock-info" v-if="!isLoading">
        <div class="logo-symbol">
          <img :src="stock.logo" />
          <div class="symbol-desc">
            <p class="symbol">{{ stock.symbol }}</p>
            <p class="description">{{ stock.name }}</p>
          </div>
        </div>
        <div class="numbers-info">
          <div class="left-side-numbers">
            <p>PRICE: {{ stock.price }}</p>
            <p>HIGH: {{ stock.highPrice }}</p>
            <p>LOW: {{ stock.lowPrice }}</p>
          </div>
          <div class="right-side-numbers">
            <p
              :style="{ color: stock.percentChange < 0 ? '#DC2027' : 'green' }"
            >
              <span>CHANGE: </span>
              {{
                stock.percentChange > 0
                  ? "+" + stock.percentChange
                  : stock.percentChange
              }}%
            </p>
            <p>OPEN PRICE: {{ stock.openPrice }}</p>
            <p>PREV CLOSE: {{ stock.previousClose }}</p>
          </div>
        </div>
      </div>
      <div class="loading" v-else>
        <p>Waiting Data...</p>
      </div>
      <div class="shares-input">
        <input
          type="text"
          class="shares"
          required
          min="1"
          placeholder="Number of shares"
          v-model="inputShares"
        />
      </div>
      <div class="buy-sell-container">
        <button class="buy-bttn" @click="buyTrade">BUY</button>
        <button class="sell-bttn" @click="sellTrade">SELL</button>
      </div>
      <div class="account-info-container">
        <div class="account-number">
          <p><span>Account #: </span>{{ account.id }}</p>
        </div>
        <div class="balance">
          <p><span>Balance: $</span>{{ account.balance }}</p>
        </div>
      </div>
      <div class="order" v-if="isPlaced">

        <p>Trade executed: You've placed a trade for {{ stock.symbol }}.</p>
    </div>
    <div class="not-placed" v-else>

        <span> </span>
    </div>
    </div>
    <div class="tradingview-widget-container">
      <div id="chart" class="tradingview-widget-container"></div>
    </div>
  </div>
</template>

<script>
import accountService from "../services/AccountService";
import stockService from "../services/StockService";
import tradeService from "../services/TradeService";

export default {
  data() {
    return {
      inputStock: "",
      stock: {},
      account: {},
      isLoading: true,
      isPlaced:false,
      inputShares: "",
      overview:{}
    };
  },
  created() { //Lifecycle hook that is call before the component is rendered to the DOM.
    this.getAccount();
  },
  mounted() { //Lifecycle hook that is called after the component is mounted or rendered to the DOM.
    this.loadTradingView(); //We place the widget here and not created() hook because we need the HTML container for the TradingView widget.
  },
  methods: {
    getStock() {

      if (this.inputStock != "") {
        stockService
          .getStock(this.inputStock.toUpperCase())
          .then((response) => {

              this.stock = response.data;
              this.isLoading = false;
              this.isPlaced = false;
            
          })
          .catch((error) => {
            console.error("Error occurred fetching stock", error);
          });
      }
    },
    getAccount() {
      this.isLoading = true;
      accountService
        .getAccount()
        .then((response) => {
          if (response.status == 200) {
            this.account = response.data;
          }
        })
        .catch((error) => {
          console.error("Error occurred fetching account", error);
        });
    },
    loadTradingView() { //Window is a global object meaning any object defined in the global scope is a property of the window object.
      if (window.TradingView) { //Checking to see if TradingView library is loaded/created and is ready to use. Using "window" is important when working with external libraries.
        new window.TradingView.widget({ //We initialize the TradingView widget with desired attributes/properties
          container_id: "chart", // Set the container here. IMPORTANT why we need to use mounted() hook instead of created().
          autosize: true,
          symbol: "AAPL",
          interval: "240",
          timezone: "Etc/UTC",
          theme: "dark",
          style: "1",
          locale: "en",
          toolbar_bg: "#f1f3f6",
          enable_publishing: false,
          withdateranges: true,
          watchlist:[
            "AAPL",
            "TSLA",
            "NVDA"
          ],
          hide_side_toolbar: false,
          allow_symbol_change: true,
          details: false,
          hotlist: true,
          calendar: true,
          show_popup_button: true,
          popup_width: "1000",
          popup_height: "650",
        });
      } else {
        console.error("TradingView is not loaded");
      }
    },
    buyTrade(){
        if (this.stock.symbol && this.inputShares != ""){
            const buyOrder = {
                symbol: this.stock.symbol,
                quantity: this.inputShares
            }
            
            const totalCost = buyOrder.quantity * this.stock.price;
            const balance = this.account.balance;

            if (totalCost > balance){
                alert("Insufficient balance to complete this order. Please reduce the number of shares.")
            }
            else if ( isNaN(buyOrder.quantity) || !buyOrder.symbol || !buyOrder.quantity ){
              alert("Trade execution failed. Please try again.")
            }
            else{
                tradeService.buyStock(buyOrder).then(response =>{
                    this.isPlaced = true;
                    this.getAccount();
                }).catch(error => {

                    console.error("Error occured placing a buy order.", error);
                });
            }
        }
    },
    sellTrade(){
        if (this.stock.symbol && this.inputShares != ""){
            const sellOrder = {
                symbol: this.stock.symbol,
                quantity: this.inputShares
            }
            
            const totalCost = sellOrder.quantity * this.stock.price;
            const balance = this.account.balance;

            if(sellOrder.symbol && sellOrder.quantity && !isNaN(sellOrder.quantity)){
                tradeService.sellStock(sellOrder).then(response =>{
                    this.isPlaced = true;
                    this.getAccount();
                }).catch(error => {

                    console.error("Error occured placing a sell order.", error);
                });
              }
              else{
                alert("Trade execution failed. Please try again.")
              }
            
        }
    },
  },
};
</script>

<style scoped>
.tradingview-widget-container {
  background-color: #c9c9c9;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
}
.trading-container {
  background-color: #031224;
  display: flex;
  height: 100%;
  gap: 10px;
}

.trade-box {
  padding-left:10px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  max-width: 550px;
}

.search {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #031224;
}

.search input {
  flex: 1;
  border: none;
  border-radius: 5px;
  padding: 6px;
  text-align: center;
  font-weight: 600;
  font-size: 17px;
  outline: none;
}

.search button {
  border: none;
  border-radius: 5px;
  padding: 6px;
  margin-right: 3px;
  font-weight: 600;
  background-color: #0575e6;
  color: white;
}
.search button:hover {
  background-color: #0e5dad;
  cursor: pointer;
  transition: all 0.3 ease;
}

.search button:active{
  background-color: #0f84fa;
}

.stock-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
}

.logo-symbol {
  background-color: #031224;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  gap: 50px;
}

.logo-symbol img {
  width: 60px;
  height: 60px;
  border-radius: 50px;
  overflow: hidden;
  object-fit: cover;
}
.symbol {
  font-size: 25px;
  font-weight: 600;
  color: #e0e0e0;
}

.description {
  font-size: 20px;
  color: #e0e0e0;
}

.symbol-desc {
  display: flex;
  flex-direction: column;
}

.numbers-info {
  background-color: #031224;
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}

.numbers-info p {
  font-size: 18px;
  font-weight: 500;
  color: #e0e0e0;
}

.numbers-info span {
  font-size: 18px;
  font-weight: 600;
  color: #e0e0e0;
}

.loading {
  background-color: #031224;
  width: 100%;
  height: 250px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.loading p {
  font-size: 20px;
  font-weight: 500;
  color: #e0e0e0;
}
.shares-input {
  width: 100%;
  display: flex;
  margin-bottom: 10px;
}
.shares-input input {
  flex: 1;
  border: none;
  border-radius: 5px;
  padding: 6px;
  text-align: center;
  font-weight: 600;
  font-size: 17px;
  outline: none;
}

.buy-sell-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
  margin-bottom: 10px;
}
.buy-sell-container button {
  flex: 1;
  border-radius: 10px;
  padding: 10px;
  font-size: 20px;
  font-weight: 700;
  color: white;
  border: none;
}

.buy-bttn {
  background-color: #19bf3d;
}

.buy-bttn:hover {
  cursor: pointer;
  background-color: #189c35;
  transition: all 0.3 ease;
}

.buy-bttn:active{
  background-color: #03f638;
}
.sell-bttn {
  background-color: #d13333;
}
.sell-bttn:hover {
  cursor: pointer;
  background-color: #a62424;
  transition: all 0.3 ease;
}

.sell-bttn:active{
  background-color: #ff1313;
}
.account-info-container {
  background-color: #031224;
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.account-info-container span,
.account-info-container p {
  color: #e0e0e0;
}

.order {
display: flex;
justify-content: center;
align-items: center;
height: 100%;
max-height: 120px;
}
.order p{
    color: white;
}

#chart {
  width: 100%;
  height: 600px;
}

@media screen and (max-width:750px){
   .trading-container{
    justify-content: center;
    align-items: center;
    flex-direction: column-reverse;
   }

   .trade-box{
    padding-right: 10px;
   }

}

</style>
