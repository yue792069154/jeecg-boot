import Vue from 'vue';
import {
    LOGIN_SERVICE
} from "../axios/api";
import {
    ACCESS_TOKEN
} from "./mutations";

export const actions = {
    login: ({
        commit
    }, userInfo) => {
        return new Promise((resolve, reject) => {
            LOGIN_SERVICE(userInfo).then(response => {
                if (response.code == '200') {
                    const result = response.result;
                    Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000);
                    commit('SET_TOKEN', result.token);
                    resolve(response);
                } else {
                    reject(response);
                }
            }).catch(error => {
                reject(error);
            });
        });

    }
};