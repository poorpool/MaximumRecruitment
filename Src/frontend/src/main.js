import Vue from 'vue'
import App from './App.vue'
import Vuetify from 'vuetify/lib'
// To add vuetify css file
import 'vuetify/dist/vuetify.min.css'
Vue.use(Vuetify)
import router from './router'
import store from './store'
import VuetifyDialog from 'vuetify-dialog'
import 'vuetify-dialog/dist/vuetify-dialog.css'
import axios from 'axios'
import VueAxios from 'vue-axios'
import vuetify from "./plugins/vuetify";

axios.defaults.timeout = 5000;  // 超时时间
axios.defaults.baseURL = 'http://140.210.139.46:5050/'
// axios.defaults.baseURL = 'http://localhost:5050/'; // 后端接口

axios.interceptors.request.use(
  config => {
    const token = sessionStorage.getItem("token");
    config.data = JSON.stringify(config.data);
    config.headers = {
      'Content-Type':'application/json'
    };
    if (token) {
      config.headers.Authorization = token;
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

Vue.use(VueAxios, axios)

Vue.config.productionTip = false

Vue.use(VuetifyDialog, {
  context: {
    vuetify
  }
})



new Vue({
  vuetify,
  render: h => h(App),
  router: router,
  store: store
}).$mount('#app')
