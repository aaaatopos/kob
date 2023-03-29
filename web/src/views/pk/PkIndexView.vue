<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"></PlayGround>
    <MatchGround v-if="$store.state.pk.status === 'matching'"></MatchGround>
    <ResultBoard v-if="$store.state.pk.loser != 'none'"></ResultBoard>
</template>

<script>

import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue'
import ResultBoard from '@/components/ResultBoard.vue'
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {  // 挂载函数，页面打开时，会执行，创建一个websocket链接
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket", socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if(data.event === "success-matching") {  // 表示匹配成功
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                    store.commit("updateGame", data.game);
                } else if (data.event === "move") {  // 接收到后端的消息为 需要移动
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes; // 取出两条蛇
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if (data.event === "result") {  // 游戏已经产生结果了
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes; // 取出两条蛇
                    
                    if (data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    }
                    if (data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
                }
            }

            socket.onclose = () => {
                console.log("disconnected!");
            }
        });

        onUnmounted(() => {  // 卸载函数，离开页面时调用，需要将websocket链接断开，避免冗余链接
            socket.close();
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
        });


    },
}

</script>

<style scoped>

</style>