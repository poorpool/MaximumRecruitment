<template>
  <div>
    <navibar></navibar>

    <v-container  fluid>
      <v-row align="start" justify="center">
        <v-col cols="10">
          <v-card>
            <v-card-title>
              点击右上角的昵称选择具体招新页面，或者退出登录。
            </v-card-title>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
  import Navibar from "../components/Navibar";
  export default {
    name: 'Home',
    components: {Navibar},
    data: () => ({
    }),
    methods: {
    },
    computed: {
    },
    mounted() {
      let permission = this.$store.getters.user.permission;
      // console.log()
      if ((permission & 4) === 4) {
        this.$router.push("/admin");
      } else if ((permission & 2) === 2) {
        this.$router.push("/manager");
      } else if ((permission & 1) === 1) {
        this.$router.push("/user");
      }
    }
  }
</script>
