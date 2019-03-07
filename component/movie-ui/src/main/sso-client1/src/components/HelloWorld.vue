<template>
  <div>
    <h1>{{ user }}</h1>
    <el-row>
      <el-button>默认按钮</el-button>
      <el-button type="primary">主要按钮</el-button>
      <el-button type="success">成功按钮</el-button>
      <el-button type="info">信息按钮</el-button>
      <el-button type="primary" @click.native.prevent="getMovies">访问影壳</el-button>
      <el-button type="warning">警告按钮</el-button>
      <el-button type="danger">危险按钮</el-button>
      <div class="hello">
        <h1>{{ msg }}</h1>
      </div>
    </el-row>
    <div class="go-back" v-show="goBackState" @click="goBack">回去</div>
    <ul>
      <li v-for="item in webAddress" :key='item'>
        <a :href="item.link" target="showHere" @click="showIframe">{{item.name}}</a>
      </li>
    </ul>
    <iframe v-show="iframeState" id="show-iframe" frameborder=0 name="showHere" scrolling=auto src=""></iframe>
  </div>
</template>

<script>
export default {
  name: "HelloWorld",
  data() {
    return {
      msg: "",
      token: "",
      user: "",
      webAddress: [{
        name: 'SSO-SERVER',
        link: 'http://sso.server.com:8088/index.html'
      }],
      iframeState: false,
      goBackState: false,
    };
  },
  mounted: function () {
    this.getUser();
    const oIframe = document.getElementById('show-iframe');
    const deviceWidth = document.documentElement.clientWidth;
    const deviceHeight = document.documentElement.clientHeight;
    oIframe.style.width = deviceWidth + 'px';
    oIframe.style.height = deviceHeight + 'px';
  },
  methods: {

    getUser() {
      var url = "/moviecase/user";
      this.$ajax.get(url).then(resp => {
        if (resp.data == "" || resp.data == null) {
          this.user = "尚未登录用户";
        } else {
          this.user = "影壳用户：" + resp.data;
        }
      }).catch(err => {
        console.log(err);
        console.log("请求失败：" + err.status + "," + err.statusText);
      });
    },

    goBack() {
      console.log('回到主页');
      this.goBackState = false;
      this.iframeState = false;
    },
    showIframe() {
      console.log('显示iframe');
      this.goBackState = true;
      this.iframeState = true;
    },

    // getQueryString(name) {
    //   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    //   var r = window.location.search.substr(1).match(reg);
    //   if (r != null) {
    //     return unescape(r[2]);
    //   }
    //   return null;
    // },

    getMovies() {
      var url = "/moviecase/movies";
      this.$ajax.get(url).then(resp => {
        this.msg = resp.data;
        console.log(resp.data);
      }).catch(err => {
        console.log(err);
        console.log("请求失败：" + err.status + "," + err.statusText);
      });
    },

  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
