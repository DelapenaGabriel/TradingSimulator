import axios from "axios";

export default{
    
    adminListAllAccounts(){
        return axios.get(`/api/accounts/admin`);
    },

    getAccount(){
        return axios.get(`/api/accounts`);
    },

    adminGetAccountById(id){
        return axios.get(`/api/accounts/admin/${id}`);
    },

    createAccount(account){
        return axios.post(`/api/accounts`, account);
    },

    updateAccount(id, account){
        return axios.put(`/api/accounts/${id}`, account);
    },

    adminDeleteAccount(id){
        return axios.delete(`/api/accounts/admin/${id}`);
    }


}