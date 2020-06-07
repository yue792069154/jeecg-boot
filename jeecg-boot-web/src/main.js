import Vue from 'vue';
import ViewUI from 'view-design';

import router from './router';
import Vuex from 'vuex';
import App from './app.vue';
import Storage from 'vue-ls';
import store from './store/index';
import 'view-design/dist/styles/iview.css';
import 'xe-utils';
import VXETable from 'vxe-table';
import 'vxe-table/lib/index.css';

Vue.use(Vuex);
Vue.use(Storage);
Vue.use(ViewUI);
Vue.use(VXETable);

Vue.prototype.$XModal = VXETable.modal;

new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App)
});