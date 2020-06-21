import Vue from 'vue';
import VueRouter from 'vue-router';
import home from './views/home/portal/home';
import ViewUI from 'view-design';
import store from './store/index';
import _ from 'lodash';
import {
    MenuTypeEnum,
    MenuEntryProtEnum
} from "./libs/fw-core/fw-constant/constant";

Vue.use(VueRouter);
Vue.use(ViewUI);

//解决 Navigating to current location (XXX) is not allowed的问题
const VueRouterPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(to) {
    return VueRouterPush.call(this, to).catch(err => err);
};

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

var systemRoute = {
    path: '/system',
    meta: {
        title: ''
    },
    component: home,
    children: []
};
var rootRoute = [];
var routeComponent = {};

const requireContext = require.context("./", true, /^\.\/views\/.*\.vue$/);

router.beforeEach((to, from, next) => {

    ViewUI.LoadingBar.start();

    rootRoute = [];

    store.dispatch('menu').then(response => {
        _.forEach(response.result, function (item) {
            if (item.menuType == MenuTypeEnum.MENU) {

                systemRoute.children.push({
                    path: (item.menuRouteContent || item.id),
                    component: (resolve) => require([`${item.menuEntryProtContent}`], resolve)
                });

                rootRoute.push({
                    path: "/" + (item.menuRouteContent || item.id),
                    meta: {
                        title: ''
                    },
                    component: (resolve) => require([`${item.menuEntryProtContent}`], resolve)
                });

                if (item.menuEntryProtCode == MenuEntryProtEnum.COMPONENT) {
                    routeComponent[item.id] = requireContext(item.menuEntryProtContent);
                }

            }
        });

        rootRoute.push(systemRoute);

        router.addRoutes(rootRoute);


        store.commit({
            type: "SET_MENU_COMPONENT",
            menuComponent: routeComponent
        });

    });


    next({
        to,
        replace: true
    });

});

router.afterEach(() => {
    ViewUI.LoadingBar.finish();
    window.scrollTo(0, 0);
});

export default router;