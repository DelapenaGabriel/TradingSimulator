<template>
<div class="form">
  <create-profile-form />
</div>
</template>

<script>
import CreateProfileForm from "../components/CreateProfileForm.vue"
export default{
  components: {
    CreateProfileForm
  }
}

</script>

<style scoped>
.form{
  width: 100%;
  min-height:100vh;
  display:flex;
  justify-content: center;
  align-items: center;
}

</style>
