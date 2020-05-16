import home from './views/home/portal/home';

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
        component: (resolve) => require(['./views/user/sysUserList.vue'], resolve)
    }]
}, {
    path: '/home',
    meta: {
        title: ''
    },
    component: home,
    children: [{
        path: 'role',
        component: (resolve) => require(['./views/role/sysRoleList.vue'], resolve)
    }]
}];
export default routers;