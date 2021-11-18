<template>
  <div>
  <v-btn @click="sendtest">sendtest</v-btn>
  <p>
  当前权限：{{ permission }}
  </p>
  </div>
</template>

<script>
  export default {
    name: 'Home',

    data: () => ({
      permission: this.$store.getters.user
    }),
    methods: {
      sendtest() {
        let _this = this;
        this.axios({
          method: 'get',
          url: '/user/test'
        }).then(res => {
          if (res.data.code === 200) {
            _this.$dialog.message.info('测试成功', {
              position: 'top-right',
              timeout: 2000
            })
          } else {
            _this.$dialog.error({
              title: "测试失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "测试异常",
            text: error.toString()
          });
        });
      }
    },
    computed: {
      permission: function(){
        return this.$store.getters.user.permission
      }

    }
  }
</script>
