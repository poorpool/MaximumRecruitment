<template>
  <section class="section-container">
    <v-row class="signin">
      <v-col cols="6" class="left">
        <h1>华中科技大学极大战队招新平台</h1>
      </v-col>
      <v-col cols="6" class="right">
        <h2>登录</h2>
        <v-card  elevation="5">
          <v-card-text>
            <v-form v-model="valid" lazy-validation ref="loginForm">
              <v-text-field
                  v-model="loginData.email"
                  :rules="emailRules"
                  label="邮箱地址"
                  required
              ></v-text-field>
              <v-text-field
                  v-model="loginData.password"
                  type="password"
                  :rules="passwordRules"
                  label="密码"
                  required
              ></v-text-field>


              <v-btn
                  :disabled="!valid"
                  color="success"
                  class="mr-4"
                  @click="login"
              >
                登录
              </v-btn>

            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </section>
</template>

<script>
  import sha256 from 'crypto-js/sha256';
  import axios from "axios";
  export default {
    name: 'Login',
    data: () => ({
      valid: true,
      emailRules: [
        v => !!v || '请输入邮箱地址',
        v => /.+@.+\..+/.test(v) || '邮箱地址不合法',
      ],
      passwordRules: [
        v => !!v || '请输入密码',
      ],
      loginData: {
        email: '',
        password: ''
      }
    }),

    methods: {
      validate () {
        this.$refs.loginForm.validate()
      },
      isEmpty (value) {
        return(
            value ===undefined || value ===null ||
            (typeof  value === "object" && Object.keys(value).length === 0) ||
            (typeof value === "string" && value.trim().length === 0)
        );
      },
      login() {
        if (!this.$refs.loginForm.validate()) {
          return false;
        }
        let _this = this;
        this.axios({
          method: 'post',
          url: '/login',
          data: {
            email: _this.loginData.email,
            password: sha256(_this.loginData.email + _this.loginData.password).toString()
          }
        }).then(res => {
          if (res.data.code === 200) {
            const token = res.data.data.token;
            this.$store.commit("SET_AUTHENTICATE", !this.isEmpty(token));
            this.$store.commit("SET_USER", res.data.data.user);
            this.$store.commit("SET_TOKEN", token);
            axios.defaults.headers['Authorization'] = token;
            _this.$dialog.message.info('登录成功', {
              position: 'bottom-left',
              timeout: 2000
            })
            this.$router.push('/');
          } else {
            _this.$dialog.error({
              title: "登录失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "登录异常",
            text: error.toString()
          });
        });
      }
    }
  }
</script>

<style>
.section-container {
  background: #fff;
  width: 100%;
  box-shadow: 0 0 1px 1px rgba(00, 00, 00, 0.1);
  box-sizing: border-box;
  height: 100%;
}
.signin {
  padding: 0;
  margin: 0 auto;
  box-shadow: 0 0 1px 1px rgba(00, 00, 00, 0.1);
  height: 100%;
}
.left {
  padding: 30px;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  display: flex;
  color: #30ac7c;
  background-color: #f9f9f9;
}
.right {
  padding: 30px;
  box-sizing: border-box;
  background: #30ac7c;
  color: #fff;
}
h2 {
  text-align: center;
  margin: 30px 0;
}
.signin-btn {
  width: 100%;
  color: #30ac7c;
}
</style>