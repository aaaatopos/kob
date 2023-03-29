import { createStore } from 'vuex'
import ModulUser from '@/store/user'
import ModulPk from '@/store/pk'
import ModulRecord from '@/store/record'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModulUser,
    pk: ModulPk,
    record: ModulRecord,
  }
})
