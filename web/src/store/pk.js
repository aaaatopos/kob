
export default {
    state: {
        status: "matching", // matching 表示正在匹配，playing表示正在对战。
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        gamemap: null,
    },
    getters: {
    },
    mutations: {  // 用来给state赋值，相当于set(), 但是是私有的，
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateGamemap(state, gamemap) {
            state.gamemap = gamemap;
        }
    }, 
    actions: {  // 实现函数，公有函数，可以被外面调用，然后调用私有函数对变量进行赋值
    },
    modules: {
    }
}