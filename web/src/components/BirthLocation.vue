<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.status === 'matching' && $store.state.pk.birthLocation === 'left'"> 
            你将出生在左下角
        </div>
        <div class="result-board-text" v-if="$store.state.pk.status === 'matching' && $store.state.pk.birthLocation === 'right'"> 
            你将出生在右上角
        </div>
    </div>
</template>

<script>

export default {
    setup() {
        console.log("123442342134");
    }
}

</script>

<style scoped>
    div.result-board{
        height: 20vh;
        width: 30vw;
        background-color: rgba(50, 50, 50, 0.5);
        position: absolute;
        top: 35vh;
        left: 35vw;
    }
    div.result-board-text {
        text-align: center;
        color: white;
        font-size: 50px;
        font-weight: 600;
        font-style: italic;
        padding-top: 5vh;
    }
</style>