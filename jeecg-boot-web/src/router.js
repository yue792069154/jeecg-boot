import Vue from 'vue';
import VueRouter from 'vue-router';
import home from './views/home/portal/home';
import Util from './libs/util';
import ViewUI from 'view-design';
import 'vxe-table/lib/index.css';

Vue.use(VueRouter);
Vue.use(ViewUI);

const routers = [{
    path: '/',
    meta: {
        title: ''
    },
    component: (resolve) => require(['./views/login/login.vue'], resolve)
}];

var router = new VueRouter({
    mode: 'history',
    scrollBehavior: () => ({
        y: 0
    }),
    routes: routers
});

router.beforeEach((to, from, next) => {
    ViewUI.LoadingBar.start();
    Util.title(to.meta.title);
    next();
});

router.afterEach(() => {
    ViewUI.LoadingBar.finish();
    window.scrollTo(0, 0);
});

var systemRoute = {
    path: '/system',
    meta: {
        title: ''
    },
    component: home,
    children: []
};

var menuList = JSON.parse(sessionStorage.getItem('LOGIN_USER_MENU_LIST')) || [];

for (let i = 0; i < menuList.length; i++) {

    systemRoute.children.push({
        path: (menuList[i].menuRouteContent || menuList[i].id),
        component: (resolve) => require([`${menuList[i].menuEntryProtContent}`], resolve)
    });

}

router.addRoutes([systemRoute]);

export default router;