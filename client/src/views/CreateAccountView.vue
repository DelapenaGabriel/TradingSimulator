<template>
    <div class="account-balance"><create-account-form/></div>
</template>

<script>
import CreateAccountForm from '../components/CreateAccountForm.vue';
export default{
    components:{
        CreateAccountForm
    }
}
</script>

<style scoped>
.account-balance{
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    
}
</style>