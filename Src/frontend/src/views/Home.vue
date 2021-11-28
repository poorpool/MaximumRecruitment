<template>
  <div>
    <navibar></navibar>
  <v-btn @click="sendtest">测试后端权限系统</v-btn>
  <p>
  当前权限：{{ permission }}
  </p>
    <p>
      cyx：这里将来要做一个自动跳转，然后把这个页面设置为无权限页面。目前点右上角的标识跳转到各个页面去。
    </p>
  </div>
</template>

<script>
  import Navibar from "../components/Navibar";
  export default {
    name: 'Home',
    components: {Navibar},
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
              position: 'bottom-left',
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
