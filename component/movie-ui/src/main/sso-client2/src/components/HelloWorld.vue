<template>
  <el-row>
    <el-button>默认按钮</el-button>
    <el-button type="primary">主要按钮</el-button>
    <el-button type="success">成功按钮</el-button>
    <el-button type="info">信息按钮</el-button>
    <el-button type="primary" style="width:45%;" @click.native.prevent="hello2" :loading="logining">访问客户端1</el-button>
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
      msg: "Welcome to Your Vue.js"
    };
  },
  methods: {
    hello2() {
      this.$ajax
        .post("/client2/hello")
        .then(resp => {
          console.log(resp.data);
        })
        .catch(err => {
          console.log(err);
          console.log("请求失败：" + err.status + "," + err.statusText);
          console.log(err.response.data);
          console.log(err.response.status);
          console.log(err.response.headers);
        });
    }
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
