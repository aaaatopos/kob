<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0">
            
        </canvas>
    </div>
</template>


<script>

import { GameMap } from '@/assets/scripts/GameMap';
import { ref, onMounted } from 'vue';  // 为了引入canvans
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => {
            store.commit(
                "updateGameObject",
                new GameMap(canvas.value.getContext('2d'), parent.value, store)  //创建一个GameMap对象
            );
        });

        return {
            parent,
            canvas
        }
    }
}

</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;  /* 居中，可以使水平和垂直都居中 */
    justify-content: center; /* 设置水平居中 */
    align-items: center;  /* 设置垂直居中 */
}

</style>