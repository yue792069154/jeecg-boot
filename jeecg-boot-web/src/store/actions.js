import Vue from 'vue';
import {
    LOGIN_SERVICE,
    MENU_USER_LIST_SERVICE,
    AUTH_USER_LIST_SERVICE
} from "../axios/api";
import {
    ACCESS_TOKEN,
    USER_INFO
} from "./mutations";
import {
    Notice
} from 'view-design';
import _ from 'lodash';


export const actions = {
    login: ({
        commit
    }, userInfo) => {
        return new Promise((resolve, reject) => {
            LOGIN_SERVICE(userInfo).then(response => {
                if (response.code == '200') {
                    const result = response.result;
                    Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000);
                    Vue.ls.set(USER_INFO, result.userInfo, 7 * 24 * 60 * 60 * 1000);

                    commit({
                        type: "SET_TOKEN",
                        token: result.token
                    });

                    commit({
                        type: "SET_USER_INFO",
                        userInfo: result.userInfo
                    });

                    resolve(response);
                } else if (response.code == '500') {
                    Notice.warning({
                        title: '系统提示',
                        desc: response.message,
                        duration: 4
                    });
                    Vue.ls.remove(ACCESS_TOKEN);
                    reject(response);
                } else {
                    Vue.ls.remove(ACCESS_TOKEN);
                    reject(response);
                }
            }).catch(error => {
                reject(error);
            });
        });

    },
    menu: ({
        commit
    }) => {
        return new Promise((resolve, reject) => {
            MENU_USER_LIST_SERVICE({
                token: Vue.ls.get(ACCESS_TOKEN)
            }).then(response => {
                resolve(response);
            }).catch(error => {
                reject(error);
            });
        });

    },
    auth: ({
        commit
    }) => {
        return new Promise((resolve, reject) => {
            AUTH_USER_LIST_SERVICE({
                token: Vue.ls.get(ACCESS_TOKEN)
            }).then(response => {
                resolve(response);
            }).catch(error => {
                reject(error);
            });
        });

    }
};