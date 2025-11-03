<template>
    <div class="account">
        <div class="balance-background">
            <h1>ACCOUNT BALANCE</h1>
        </div>

    
            <form class="account-form" @submit.prevent="createAcc(account)">
                <h2>Enter desired account balance</h2>
                <p>(Numeric values only: '<span>,</span>' or '<span>$</span>' not accepted)</p>
                <p>(Example: '50000.00' = $50,000.00)</p>
                <input type="text" id="balance" required placeholder="$" autocomplete="off" v-model="account.balance">
                <button type="submit">Continue</button>
            </form>
        

    </div>
</template>

<script>
import accountService from '../services/AccountService';
export default{
    data(){

        return{
            account:{
                balance:""
            }
        }
    },
    methods:{
        createAcc(account){
            accountService.createAccount(account).then((response)=>{
                if (response.status == 201){
                    this.$router.push({name:"home"});
                }
            }).catch(error=>{
                console.error("Error occurred creating account", error);
            });
        }
    }
}
</script>

<style scoped>
.account{
    display:flex;
    flex-direction: column;
    align-items: center;
}

span{
    color: rgb(232, 8, 8);
    font-size: larger;
    font-weight: 500;
}

.balance-background{
    background-image: linear-gradient(#0575e6, #02298a, #021b79);
    width: 100%;
    max-width: 1000px;
    height:79px;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
}

h1{
color: white;
font-size: 32px;
text-align: center;
}

.account-form{
    background-color: white;
    display:flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    max-width: 1000px;
    height: 450px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
    padding: 30px;
}

h2{
    font-size: 24px;
    text-align: center;
}

h2, p{
    margin:5px;
}

p{
    text-align: center;
    font-size: 18px;
    font-weight: 500;
}

input{
    margin-top: 30px;
    margin-bottom:50px;
    outline: none;
    border-radius: 5px;
    border: 2px solid #bbbbbb;
    padding:5px;
    color: black;
    text-align: center;
    width: 55%;
    font-size: 20px;
    font-weight: 500;
}

button{
  background-color: #0575e6;
  color: white;
  width: 40%;
  border-radius: 30px;
  padding:10px;
  border: none;
  font-size: 18px;
  cursor: pointer;
}
button:hover {
  background-color: #02298a;
}

@media screen and (max-width:679px){
    input{
        margin-bottom: 20px;
    }
    button{
        padding:5px;
        width:50%;
    }
}

@media screen and (max-width:547px){

    h2{
        font-size: 18px;
    }

    p{
        font-size: 13px;
    }
    
    input{
        margin-top: 10px;
        margin-bottom: 10px;
    }
    button{
        padding:1px;
        width:50%;
    }
}

@media screen and (min-width: 1202px) {
    h2{
        font-size: 30px;
    }
    h1{
        font-size: 40px;
    }
}

@media screen and (max-width:516px){
    h1{
        font-size: 20px;
    }
    h2{
        font-size: 13px;
    }
    p{
        font-size: 10px;
    }
    input{
        font-size: 10px;
    }
    .account-form{
        padding:15px;
    }
}

</style>