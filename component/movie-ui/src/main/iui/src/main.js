// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';

axios.defaults.withCredentials = true;
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.use(ElementUI);

//拦截器
axios.interceptors.response.use((response) => {
  return response;
}, function (error) {
  if (403 === error.response.status) {
    //客户端地址
    var rediectUrl = error.response.headers.location;
    //令牌
    var token = error.response.headers.token;
    if (rediectUrl && rediectUrl != '') {
      console.log('锚点', 'window.location.hash', window.location.hash);
      console.log('主机名：端口号', 'window.location.host', window.location.host);
      console.log('完整路径', 'window.location.href', window.location.href);
      console.log('协议名', 'window.location.protocol', window.location.protocol);
      console.log('服务器地址', 'window.location.hostname', window.location.hostname);
      console.log('端口号', 'window.location.port', window.location.port);
      //如果当前请求的浏览器地址包含锚点信息，带着锚点信息跳转到登录页面;如果不包含则跳转到服务端返回的浏览器地址
      //浏览器地址使用window.btoa()加密 使用atob解密
      //window.btoa这中编码方式不能直接作用于Unicode字符串 只能将ascci字符串或二进制数据转换成Base64编码过的字符串
      // if (currentRequestUrl.includes('#')) {
      //   window.location.href = rediectUrl + '?' + window.btoa('redirect_url=' + window.location.href);
      // } else {
      //   window.location.href = rediectUrl + '?' + window.btoa('redirect_url=' + clientUrl);
      // }
      //window.btoa()对Unicode字符进行编码的方法
      //可以用window.btoa(window.encodeURIComponent())双重加密用window.decodeURIComponent(window.atob())解密
      console.log('登录页面', rediectUrl.split('#')[0] + '?token=' + token + '#' + rediectUrl.split('#')[1]);
      if (rediectUrl.includes('#')) {
        window.location.href = rediectUrl.split('#')[0] + '?token=' + token + '#' + rediectUrl.split('#')[1];
      } else {
        window.location.href = rediectUrl + '?token=' + token;
      }
    }
  } else if (401 === error.response.status) {
    debugger
    window.location.href = error.response.headers.location;
  } else {
    return Promise.reject(error);
  }
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
