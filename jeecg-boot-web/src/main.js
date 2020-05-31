import Vue from 'vue';
import ViewUI from 'view-design';
import VueRouter from 'vue-router';
import Routers from './router';
import Vuex from 'vuex';
import Util from './libs/util';
import App from './app.vue';
import Storage from 'vue-ls';
import store from './store/index';
import 'view-design/dist/styles/iview.css';
import 'xe-utils';
import VXETable from 'vxe-table';
import 'vxe-table/lib/index.css';


Vue.use(VueRouter);
Vue.use(Vuex);
Vue.use(Storage);
Vue.use(ViewUI);
Vue.use(VXETable);

Vue.prototype.$XModal = VXETable.modal;

// 路由配置
const RouterConfig = {
    mode: 'history',
    routes: Routers
};
const router = new VueRouter(RouterConfig);

router.beforeEach((to, from, next) => {
    ViewUI.LoadingBar.start();
    Util.title(to.meta.title);
    next();
});

router.afterEach(() => {
    ViewUI.LoadingBar.finish();
    window.scrollTo(0, 0);
});

new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App)
});