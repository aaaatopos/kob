<!-- 编写html -->
<template>
  <NavBar/>
  <router-view/>
</template>

<!-- 编写js -->
<script>
/* 导入组件 */
import NavBar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default{
  components: {
    NavBar
  }
}

</script>


<!-- 编写css -->
<style>
  body{
    background-image: url("@/assets/images/background.png");
    background-size: 100%;
  }
</style>
