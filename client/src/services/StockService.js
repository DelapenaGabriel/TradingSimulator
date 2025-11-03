import axios from 'axios'

const http = axios.create({
    baseURL:"https://api.polygon.io/v3/reference/tickers/"
})

export default{
    getAllStocks(){
        return axios.get(`/api/stocks`);
    },
    getStock(symbol){
        return axios.get(`/api/stocks/${symbol}`);
    },
    getStockDescription(symbol){
        return http.get(`${symbol}?apiKey=pKppWqJO8q0t_M3zjRJo2zdQU2xcTlHR`);
    }
}