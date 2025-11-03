<template>
  <div class="stock-tracker-container">
    <div class="headline">
      <h1>STOCKS EXPLORER</h1>
    </div>
    <div class="search-filter">
      <button @click="getSearchedStock">Search</button>
      <input type="text" v-model="searchStock" placeholder="Search a Stock"/>
    </div>

<div class="searched-container" v-if="searchStock && !isloading">
  <div class="stock-card" @click="routeToStockOverview(searchedStock.symbol)">
        <div class="logo-symbol">
          <img
            :src="searchedStock.logo ? searchedStock.logo: 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'"
          />
          <h2>{{searchedStock.symbol}}</h2>
        </div>
        <div class="price-change">
          <p class="price">${{searchedStock.price}}</p>
          <p class="change" :style="{ color: searchedStock.percentChange < 0 ? '#DC2027' : 'green' }">{{
                searchedStock.percentChange > 0
                  ? "+" + searchedStock.percentChange
                  : searchedStock.percentChange
              }}%</p>
        </div>
        <div class="price-info">
          <div class="left">
            <p>Open: ${{ searchedStock.openPrice }}</p>
            <p>Prev. Close: ${{ searchedStock.previousClose }}</p>
          </div>
          <div class="right">
            <p>High: ${{ searchedStock.highPrice }}</p>
            <p>Low: ${{searchedStock.lowPrice}}</p>
          </div>
        </div>
      </div>
</div>

    <div class="stock-container" v-if="!isloading && !searchStock">
      <div class="stock-card" v-for="stock in displayStocks" :key="stock.symbol" @click="routeToStockOverview(stock.symbol)">
        <div class="logo-symbol">
          <img
            :src="stock.logo ? stock.logo: 'https://www.shutterstock.com/image-vector/default-ui-image-placeholder-wireframes-600nw-1037719192.jpg'"
          />
          <h2>{{stock.symbol}}</h2>
        </div>
        <div class="price-change">
          <p class="price">${{stock.price}}</p>
          <p class="change" :style="{ color: stock.percentChange < 0 ? '#DC2027' : 'green' }">{{
                stock.percentChange > 0
                  ? "+" + stock.percentChange
                  : stock.percentChange
              }}%</p>
        </div>
        <div class="price-info">
          <div class="left">
            <p>Open: ${{ stock.openPrice }}</p>
            <p>Prev. Close: ${{ stock.previousClose }}</p>
          </div>
          <div class="right">
            <p>High: ${{ stock.highPrice }}</p>
            <p>Low: ${{stock.lowPrice}}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="loading" v-else>
      <loading-spinner id="spinner" :spin="isloading" />
    </div>
    <div class="load-more">
      <button @click="loadMoreStocks">Load More Stocks</button>
    </div>
  </div>
</template>

<script>
import stockService from '../services/StockService';
import LoadingSpinner from '../components/LoadingSpinner.vue';

export default{
    data(){
        return {
            stocks:[],
            searchedStock:{},
            displayStocks:[],
            displayCounter:5,
            currentPage:0,
            isloading:false,
            searchStock:null,
        }
    },
    created(){
        this.getStocks();
    },
    components:{
      LoadingSpinner
    },
    methods:{
        getStocks(){
          this.isloading = true;
            stockService.getAllStocks().then((response)=>{
                if (response.status == 200){
                    this.stocks = response.data;
                }
  
            }).catch(error =>{

              console.error("Error Occurred retrieving stocks.", error)
            }).then(()=>{
              this.loadMoreStocks();
            });
          
        },
        loadMoreStocks(){
          this.isloading = true;
          const start = this.currentPage * this.displayCounter;
          const end =  start + this.displayCounter;

          const newStocks = this.stocks.slice(start,end);


          newStocks.forEach(stock=>{
            if (stock.displaySymbol != undefined || stock.displaySymbol){
            stockService.getStock(stock.displaySymbol).then((response)=>{
              this.displayStocks.push(response.data);
            })
            .finally(()=>{
              this.isloading = false;
            })
          }
          })

          this.currentPage++;
        },
        getSearchedStock(){
          if (this.searchStock){
            this.isloading = true;
            stockService.getStock(this.searchStock.toUpperCase()).then((response)=>{
              this.searchedStock = response.data;
              this.isloading = false;
            }).catch(error =>{
              console.error("Error occurred retrieving searched stock.", error);
            });
          }
        },
        routeToStockOverview(symbol){
          this.$router.push({name:"stock-overview", params: {id: symbol}})

        }

    },
    computed:{
      hasMoreSymbols(){
        return this.currentPage * this.displayCounter < this.stocks.length;
      }
    }
}
</script>

<style scoped>
.stock-tracker-container {
  min-height: 100vh;
  width: 90%;
  margin: 10px auto;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 50px;
  transition: all 0.3 ease;
}

.headline {
  width: 100%;
  margin-bottom: 20px;
}

h1 {
  font-size: 38px;
  text-align: center;
  border-bottom: 3px solid #bbbbbb;
  padding-bottom: 10px;
}

.search-filter {
  display: flex;
  justify-content: center;
  width: 60%;
  margin-bottom: 25px;
}
.loading{
width: 100%;
height: 300px;
display: flex;
justify-content: center;
align-items: center;
}

.search-filter button {
  background-color: #0575e6;
  padding: 3px 10px;
  border: none;
  border-radius: 10px 0 0 10px;
  color: white;
  overflow: hidden;
}
.search-filter input {
  outline: none;
  border: 1px solid #bbbbbb;
  width: 50%;
  border-radius: 0 10px 10px 0;
  text-align: center;
}

.card-container {
  width: 100%;
}

.stock-container {
  width: 100%;
  display: flex;
  justify-content: space-evenly;
  flex-wrap: wrap;
  gap: 80px;
}

.stock-card {
  background-color: white;
  box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
  border-radius: 10px;
  width: 320px;
  max-width: 320px;
  height: 240px;
  display: flex;
  flex-direction: column;
  padding: 20px;
  justify-content: space-between;
  overflow: hidden;
}

.stock-card:active{
  background-color: rgb(229, 229, 229);
}

.stock-card:hover{
  transition: all 0.3 ease;
  width: 330px;
  height: 250px;
  max-width: 330px;
  cursor: pointer;
}

.logo-symbol {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h2 {
  font-size: 48px;
  font-weight: 600;
}

.logo-symbol img {
  width: 70px;
  height: 70px;
  border-radius: 50px;
  object-fit: cover;
}

.price-change {
  display: flex;
  justify-content: space-between;
}

.price-change p {
  font-size: 25px;
}

.price-info {
  display: flex;
  justify-content: space-between;
}

.load-more{
  margin-top: 20px;
}

.load-more button{
  background-color: #0575e6;
  border: none;
  color: white;
border-radius: 10px;
padding: 7px;
}

button:hover{
  background-color: #095eb3;
}

button:active{
  background-color: #0575e6;
}
</style>
