import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'
import '@/assets/global.css'
import SlideVerify from 'vue-monoplasty-slide-verify';

Vue.use(SlideVerify);
Vue.use(ElementUI,{size:'small'}); //medium small min
Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount(  '#app')
