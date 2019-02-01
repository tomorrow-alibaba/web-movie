<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <h3 class="title">系统登录</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="ruleForm.account" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="ruleForm.checkPass" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
    <el-form-item style="width:100%;">
      <el-row style="padding:10px">
        <el-button type="primary" style="width:45%;" @click.native.prevent="loginSubmit" :loading="logining">登录</el-button>
        <el-button type="success" style="width:45%;margin:10px" @click.native.prevent="registerSubmit" :loading="logining">注册</el-button>
        <el-button type="" style="width:45%;margin:0" @click.native.prevent="handleReset">重置</el-button>
        <el-button type="warning" style="width:45%;margin:10px" @click.native.prevent="logout1">注销</el-button>
      </el-row>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  data() {
    return {
      logining: false,
      ruleForm: {
        account: "",
        checkPass: ""
      },
      redirectUrl: "",
      rules: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" }
          //{ validator: validaePass }
        ],
        checkPass: [
          { required: true, message: "请输入密码", trigger: "blur" }
          //{ validator: validaePass2 }
        ]
      },
      checked: true
    };
  },
  beforeCreate: function() {
    console.log(this);
    //this.showData("创建vue实例前", this);
  },
  created: function() {
    // this.showData("创建vue实例后", this);
  },
  beforeMount: function() {
    // this.showData("挂载到dom前", this);
  },
  //页面渲染完成后
  mounted() {
    this.redirectUrl = this.getQueryString("redirect_url");
    console.log(this.redirectUrl);
  },
  beforeUpdate: function() {
    // this.showData("数据变化更新前", this);
  },
  updated: function() {
    // this.showData("数据变化更新后", this);
  },
  beforeDestroy: function() {
    // this.showData("vue实例销毁前", this);
  },
  destroyed: function() {
    // this.showData("vue实例销毁后", this);
  },
  methods: {
    realDom() {
      console.log("真实dom结构：" + document.getElementById("app").innerHTML);
    },
    showData(process, obj) {
      console.log(process);
      console.log("data 数据：");
      console.log(obj.ruleForm);
      this.realDom();
      console.log("------------------");
      console.log("------------------");
    },
    getQueryString(name) {
      var search = window.location.search;
      var queryString = window.decodeURIComponent(window.atob(search.substr(1)));
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = queryString.match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    handleReset() {
      //this.$refs.ruleForm.resetFields();
      this.$axios
        .post("/user/hello")
        .then(resp => {
          console.log(resp.data);
          alert(resp.data);
        })
        .catch(err => {
          console.log("请求失败：" + err.status + "," + err.statusText);
        });
    },
    getRedirectUrl(url) {
      var spliceUrl = "";
      if (this.redirectUrl && this.redirectUrl != "") {
        spliceUrl = url + "?redirect_url=" + this.redirectUrl;
        if(spliceUrl.includes('#')){
          return spliceUrl.replace('#','%23');
        }
        return spliceUrl;
      } else {
        return url;
      }
    },
    loginSubmit(ev) {
      var _this = this;
      this.$refs.ruleForm.validate(valid => {
        console.log(valid);
      });
      this.login();
    },
    login() {
      console.log(this.ruleForm.account);
      console.log(this.ruleForm.checkPass);
      var url = this.getRedirectUrl("/user/login");
      console.log(url);
      this.$axios
        .post(url, {
          name: this.ruleForm.account,
          password: this.ruleForm.checkPass
        })
        .then(resp => {
          debugger
          console.log(resp.data);
          var responseUrl = resp.data;
          if (responseUrl && responseUrl!="" && responseUrl.includes("redirect:")) {
            if(responseUrl.includes("token")){
              var clientUrl = responseUrl.substr(9);
              var token = responseUrl.split('token=')[1];
              window.location.href = clientUrl;
            }
          }
        })
        .catch(err => {
          console.log("请求失败：" + err.status + "," + err.statusText);
        });
    },
    registerSubmit() {
      this.$axios
        .post("/user/register", {
          name: this.ruleForm.account,
          password: this.ruleForm.checkPass
        })
        .then(resp => {
          console.log(resp.data);
        })
        .catch(err => {
          console.log("请求失败：" + err.status + "," + err.statusText);
        });
    },
    logout1() {
      var url = "/user/logout";
      console.log(url);
      this.$axios
        .post(url, {
          name: this.ruleForm.account,
          password: this.ruleForm.checkPass
        })
        .then(resp => {
          this.msg = resp.data;
          console.log(resp.data);
        })
        .catch(err => {
          console.log(err);
          console.log("请求失败：" + err.status + "," + err.statusText);
        });
    }
  }
};
</script>
<style >
.login-container {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}
.remember {
  margin: 0px 0px 35px 0px;
}
</style>
