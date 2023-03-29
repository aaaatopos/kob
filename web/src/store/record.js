
export default {
    state: {
        is_record: false,
        a_steps: "",
        b_steps: "",
        record_loser: "",
    },
    getters: {
    },
    mutations: {  // 用来给state赋值，相当于set(), 但是是私有的，
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, loser) {
            state.record_loser = loser;
        }
    }, 
    actions: {  // 实现函数，公有函数，可以被外面调用，然后调用私有函数对变量进行赋值
    },
    modules: {
    }
}