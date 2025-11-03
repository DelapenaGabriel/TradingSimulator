<template>
  <div class="trade-log">
    <div class="trade-headline">
      <h2>TRADE LOG</h2>

      <div class="filter-search">
        <label for="filter">Search: </label>
        <input type="text" v-model="filteredText" />
      </div>
    </div>
    <div class="data-table">
      <table>
        <thead>
          <tr>
            <th>Id</th>
            <th>User Id</th>
            <th>Symbol</th>
            <th>Type</th>
            <th>Shares</th>
            <th>Price</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody v-if="trades.length > 0" class="trade-record">
          <tr v-for="trade in filteredList" :key="trade.id">
            <td>{{ trade.id }}</td>
            <td>{{ trade.userId }}</td>
            <td>{{ trade.symbol }}</td>
            <td>{{ trade.tradeType }}</td>
            <td>{{ trade.quantity }}</td>
            <td>{{ trade.price }}</td>
            <td>{{ trade.tradeDate }}</td>
          </tr>
        </tbody>
        <tbody v-else class="empty">
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>
              No Trades Taken <loading-spinner id="spinner" :spin="isLoading" />
            </td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <th>Id</th>
            <th>User Id</th>
            <th>Symbol</th>
            <th>Type</th>
            <th>Shares</th>
            <th>Price</th>
            <th>Date</th>
          </tr>
        </tfoot>
      </table>
    </div>
  </div>
</template>

<script>
import tradeService from "../services/TradeService";
import LoadingSpinner from "../components/LoadingSpinner.vue";
export default {
  components: {
    LoadingSpinner,
  },
  data() {
    return {
      trades: [{}],
      isLoading: true,
      filteredText: "",
    };
  },
  created() {
    this.getTrades();
  },
  methods: {
    getTrades() {
      this.isLoading = true;
      tradeService
        .getAllTrades()
        .then((response) => {
          this.trades = response.data;
        })
        .catch((error) => {
          console.error("Error occurred retrieving trades", error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
  },
  computed: {
    filteredList() {
      this.getTrades();
      let filteredTrades = this.trades;

      if (this.filteredText != "") {
        filteredTrades = filteredTrades.filter((trade) => {
          return (
            trade.symbol
              .toLowerCase()
              .includes(this.filteredText.toLowerCase()) ||
            trade.id == this.filteredText ||
            trade.tradeType
              .toLowerCase()
              .includes(this.filteredText.toLowerCase()) ||
            trade.quantity == this.filteredText ||
            trade.price == this.filteredText ||
            trade.tradeDate
              .toLowerCase()
              .includes(this.filteredText.toLowerCase())
          );
        });
      }

      return filteredTrades;
    },
  },
};
</script>

<style scoped>
.trade-log{
  min-height: 80vh;
  overflow: hidden;
}
.empty td {
  font-size: 20px;
}
.no-trades p {
  text-align: center;
}

.filter-search label {
  font-weight: 500;
}

.filter-search input {
  outline: none;
  border-radius: 5px;
  border: none;
  padding-left: 10px;
}

.trade-headline {
  background: linear-gradient(to top, #c4c5c7 0%, #dcdddf 52%, #ebebeb 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
.trade-headline h2 {
  text-transform: uppercase;
  font-size: 30px;
  color: black;
  margin: 0;
}
table {
  width: 100%;
  height: 100%;
  border-bottom: 2px solid rgb(224, 223, 223);
}
thead > tr {
  border-bottom: 2px solid rgb(224, 223, 223);
  height: 30px;
  padding: 20px;
  padding-left: 10px;
  padding-right: 10px;
}

thead> tr > th {
  text-align: center;
  border-right: 2px solid rgb(224, 223, 223);
  border-bottom: 2px solid rgb(224, 223, 223);
  border-radius: 3px;
}

tfoot > tr > th{
  text-align: center;
  border-right: 2px solid rgb(224, 223, 223);
  border-top: 2px solid rgb(224, 223, 223);
  border-radius: 3px;
}

tr {
  padding: 20px;
  text-align: center;
}

td {
  padding: 12px;
}

@media screen and (max-width: 530px) {
  .trade-record td {
    font-size: 13px;
  }
}
@media screen and (max-width: 470px) {
  .trade-record td {
    font-size: 10px;
  }
  

}

@media screen and (max-width: 400px) {
  
  th{
    font-size: 11px;
  }

}
</style>
