import Vue from 'vue';
import {
    LOGIN_SERVICE,
    MENU_USER_LIST_SERVICE
} from "../axios/api";
import {
    ACCESS_TOKEN,
    USER_MENU_LIST
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
                    commit('SET_TOKEN', result.token);
                    resolve(response);
                } else if (response.code == '500') {
                    Notice.warning({
                        title: '系统提示',
                        desc: response.message,
                        duration: 4
                    });
                    Vue.ls.set(ACCESS_TOKEN, null);
                    reject(response);
                } else {
                    Vue.ls.set(ACCESS_TOKEN, null);
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

                var menuList = response.result;
                var sessionMenuList = []; //路由菜单

                _.forEach(menuList, function (item) {
                    if (!_.isNil(item.menuEntryProtContent) && item.menuEntryProtContent != "") {
                        sessionMenuList.push(item);
                    }
                });
                sessionStorage.setItem(USER_MENU_LIST, JSON.stringify(sessionMenuList));
                commit('SET_USER_MENU_LIST', sessionMenuList);

                reject(sessionMenuList);

            }).catch(error => {
                reject(error);
            });
        });

    }
};