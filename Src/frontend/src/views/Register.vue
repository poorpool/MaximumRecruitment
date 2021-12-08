<template>
  <section class="section-container">
    <v-row class="signin">
      <v-col cols="6" class="left">
        <h1>华中科技大学极大战队招新平台</h1>
      </v-col>
      <v-col cols="6" class="right">
        <h2>注册</h2>
        <v-card  elevation="5">
          <v-card-text>
            <v-form v-model="valid" lazy-validation ref="registerForm">
              <v-text-field
                  v-model="registerData.nickname"
                  :rules="nicknameRules"
                  label="昵称"
                  required
              ></v-text-field>
              <v-text-field
                  v-model="registerData.email"
                  :rules="emailRules"
                  label="邮箱地址"
                  required
              ></v-text-field>
              <v-text-field
                  v-model="registerData.password"
                  type="password"
                  :rules="passwordRules"
                  label="密码"
                  required
              ></v-text-field>
              <a href="/login">
                <p>
                  已有帐号？登录
                </p>
              </a>

              <v-btn
                  :disabled="!valid"
                  color="success"
                  class="mr-4"
                  @click="register"
              >
                注册
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
export default {
  name: 'Register',
  data: () => ({
    valid: true,
    emailRules: [
      v => !!v || '请输入邮箱地址',
      v => /.+@.+\..+/.test(v) || '邮箱地址不合法',
    ],
    passwordRules: [
      v => !!v || '请输入密码',
    ],
    nicknameRules: [
      v => !!v || '请输入昵称',
    ],
    registerData: {
      email: '',
      password: '',
      nickname: ''
    }
  }),

  methods: {
    validate () {
      this.$refs.registerForm.validate()
    },
    isEmpty (value) {
      return(
          value ===undefined || value ===null ||
          (typeof  value === "object" && Object.keys(value).length === 0) ||
          (typeof value === "string" && value.trim().length === 0)
      );
    },
    register() {
      if (!this.$refs.registerForm.validate()) {
        return false;
      }
      let _this = this;
      this.axios({
        method: 'post',
        url: '/register',
        data: {
          email: _this.registerData.email,
          password: sha256(_this.registerData.email + _this.registerData.password).toString(),
          nickname: _this.registerData.nickname
        }
      }).then(res => {
        if (res.data.code === 200) {
          _this.$dialog.message.info('注册成功', {
            position: 'bottom-left',
            timeout: 2000
          })
          this.$router.push('/login');
        } else {
          _this.$dialog.error({
            title: "注册失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "注册异常",
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