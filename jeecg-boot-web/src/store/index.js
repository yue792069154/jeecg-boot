import Vue from 'vue';
import Vuex from 'vuex';
import {getters} from './getters';
import {mutations} from './mutations';
import {actions} from './actions';


Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        token: "",
        userInfo:null,
        userMenuList:[],
        menuComponent:{}
    },
    getters,
    mutations,
    actions
});