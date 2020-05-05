import home from './views/home/master/home';

const routers = [{
    path: '/',
    meta: {
        title: ''
    },
    component: (resolve) => require(['./views/login/login.vue'], resolve)
}, {
    path: '/home',
    meta: {
        title: ''
    },
    component: home,
    children: [{
        path: 'login',
        component: (resolve) => require(['./views/user/userList.vue'], resolve)
    }]
}];
export default routers;