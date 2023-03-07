<!-- 编写html -->
<template>
  <div>
    <div>Bot的昵称：{{ bot_name }}</div>
    <div>Bot的战力：{{ bot_rating }}</div>
  </div>
  <router-view/>
</template>

<!-- 编写js -->
<script>
import $ from 'jquery';
import { ref } from 'vue';

export default{
  name: "App",
  setup: () => {
    let bot_name = ref("");
    let bot_rating = ref("");

    $.ajax({
      url: "http://127.0.0.1:3000/pk/getinfo",
      type: "get",
      success: resp => {
        bot_name.value = resp.name;
        bot_rating.value = resp.rating;
      }
    });


    return {
      bot_name,
      bot_rating
    };
  }
}

</script>


<!-- 编写css -->
<style>
  body{
    background-image: url("@/assets/background.png");
    background-size: 100%;
  }
</style>
