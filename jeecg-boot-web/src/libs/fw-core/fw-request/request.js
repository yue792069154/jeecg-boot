import Vue from 'vue';
import axios from 'axios';
import {
    Notice
} from 'view-design';

const ACCESS_TOKEN = 'Access-Token';

const service = axios.create({
    timeout: 60000
});

const error = (error) => {
    if (error.response) {
        let data = error.response.data;
        const token = Vue.ls.get(ACCESS_TOKEN);
        console.log("------异常响应------", token);
        console.log("------异常响应------", error.response.status);
        switch (error.response.status) {
            case 403:
                Notice.error({
                    title: '系统提示',
                    desc: '拒绝访问',
                    duration: 4
                });
                break;
            case 500:
                if (data.message == "Token失效，请重新登录") {
                    Notice.error({
                        title: '登录已过期',
                        desc: '很抱歉，登录已过期，请重新登录',
                        duration: 2,
                        onClose: () => {
                            
                        }
                    });
                }
                break;
            case 404:
                Notice.error({
                    title: '系统提示',
                    desc: '很抱歉，资源未找到',
                    duration: 4
                });
                break;
            case 504:
                Notice.error({
                    title: '系统提示',
                    desc: '网络超时'
                });
                break;
            case 401:
                Notice.error({
                    title: '系统提示',
                    desc: '未授权，请重新登录',
                    duration: 4
                });
                if (token) {
                    store.dispatch('Logout').then(() => {
                        setTimeout(() => {
                            window.location.reload();
                        }, 1500);
                    });
                }
                break;
            default:
                Notice.error({
                    title: '系统提示',
                    desc: data.message,
                    duration: 4
                });
                break;
        }
    }
    return Promise.reject(error);
};

service.interceptors.request.use(config => {

    const token = Vue.ls.get(ACCESS_TOKEN);
    if (token) {
        config.headers['X-Access-Token'] = token;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});


service.interceptors.response.use((response) => {
    return response.data;
}, error);

export {
    service as axios
};