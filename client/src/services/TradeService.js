import axios from 'axios'

export default{
    getAllTrades(){
        return axios.get(`/api/trades`);
    },

    getTradeById(id){
        return axios.get(`/api/trades/${id}`);
    },

    createTrade(trade){
        return axios.post(`/api/trades`, trade);
    },

    buyStock(trade){
        return axios.post(`/api/trades/buy`, trade);
    },

    sellStock(trade){
        return axios.post(`/api/trades/sell`, trade);
    },

}