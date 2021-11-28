<template>
  <v-app-bar elevation="3" color="teal" dark>
<!--    <v-app-bar-nav-icon></v-app-bar-nav-icon>-->

    <v-toolbar-title>华中科技大学极大战队招新平台 {{subname}}</v-toolbar-title>

    <v-spacer></v-spacer>

    <v-menu left bottom>
      <template v-slot:activator="{ on, attrs }">
        <v-btn text v-bind="attrs" v-on="on">
          {{nickname}}
        </v-btn>
      </template>

      <v-list>
        <v-list-item v-if="permission&1" @click="switchPage('/user')">
          <v-list-item-title>切换到选手界面</v-list-item-title>
        </v-list-item>
        <v-list-item v-if="permission&2" @click="switchPage('/manager')">
          <v-list-item-title>切换到管理员界面</v-list-item-title>
        </v-list-item>
        <v-list-item v-if="permission&4" @click="switchPage('/admin')">
          <v-list-item-title>切换到超级管理员界面</v-list-item-title>
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item @click="logout">
          <v-list-item-title>退出</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>


<script>
import axios from "axios";

export default {
  name: 'Navibar',
  props: ['subname'],
  data: () => ({
  }),
  methods: {
    switchPage(uri) {
      this.$router.push(uri);
    },
    logout() {
      this.$store.commit("SET_AUTHENTICATE", false);
      this.$store.commit("SET_USER", {});
      this.$store.commit("SET_TOKEN", null);
      axios.defaults.headers['Authorization'] = '';
      window.sessionStorage.removeItem("token")
      this.$dialog.message.info('退出成功', {
        position: 'bottom-left',
        timeout: 2000
      })
      this.$router.push('/login');
    }
  },
  computed: {
    permission: function(){
      return this.$store.getters.user.permission
    },
    nickname: function() {
      return this.$store.getters.user.nickname
    }
  }
}
</script>