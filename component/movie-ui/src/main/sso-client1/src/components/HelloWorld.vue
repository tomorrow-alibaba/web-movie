<template>
  <el-row>
    <el-button>默认按钮</el-button>
    <el-button type="primary">主要按钮</el-button>
    <el-button type="success">成功按钮</el-button>
    <el-button type="info">信息按钮</el-button>
    <el-button type="primary" @click.native.prevent="hello1">访问客户端1</el-button>
    <el-button type="primary" @click.native.prevent="hello2">访问客户端2</el-button>
    <el-button type="warning">警告按钮</el-button>
    <el-button type="danger">危险按钮</el-button>
      <div class="hello">
      <h1>{{ msg }}</h1>
    </div>
  </el-row>
</template>

<script>
export default {
  name: "HelloWorld",
  data() {
    return {
      msg: "",
      token: ""
    };
  },
  mounted: function() {
    this.token = this.getQueryString("token");
  },
  methods: {
    getQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    getToken(url) {
      if (this.token && this.token != "") {
        var tokenUrl = url + "?token=" + this.token;
        return tokenUrl;
      } else {
        return url;
      }
    },
    hello1() {
      var url = this.getToken("/client1/hello");
      console.log(url);
      this.$ajax
        .get(url)
        .then(resp => {
          this.msg = resp.data;
          console.log(resp.data);
        })
        .catch(err => {
          console.log(err);
          console.log("请求失败：" + err.status + "," + err.statusText);
        });
    },
    hello2() {
      var url = this.getToken("/client2/hello");
      console.log(url);
      this.$ajax
        .get(url)
        .then(resp => {
          this.msg = resp.data;
          console.log(resp.data);
        })
        .catch(err => {
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
