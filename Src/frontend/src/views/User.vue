<template>
  <div>
    <navibar subname="选手端"></navibar>

    <v-container  fluid>
      <v-row align="start" justify="center">
        <v-col>
          <v-card>
            <v-card-title>
              选择或加入招新
              <v-spacer></v-spacer>
              <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn color="primary" dark @click="getAvailableRecruitments"
                         v-bind="attrs"
                         v-on="on">
                    加入招新
                  </v-btn>
                </template>
                <v-list>
                  <v-list-item
                      v-for="item in availableRecruitments"
                      :key="item.id"
                      @click="toSpecificRecruitment(item.id)"
                  >
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </v-card-title>
            <v-card-text>
              <v-alert type="info" v-if="studentOfRecruitments.length === 0">还没有加入任何招新</v-alert>
              <div v-else>
                <p>
                  点击任何卡片查看招新详情
                </p>
                <p>
                  cyx：这里要加上所有状态、现在状态、招新的状态的展示
                </p>
                <v-card v-for="(sor,index) in studentOfRecruitments" :key="sor.id" @click="toSpecificRecruitment(sor.recruitmentId)">
                  <v-card-title>{{recruitments[index].name}}</v-card-title>
                </v-card>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
  import Navibar from "../components/Navibar";
  export default {
    name: 'User',
    components: {Navibar},
    data: () => ({
      availableRecruitments: [],
      studentOfRecruitments: [],
      recruitments: {}
    }),
    methods: {
      getAvailableRecruitments() {
        let _this = this;
        this.axios({
          method: 'get',
          url: '/recruitment/getAvailable'
        }).then(res => {
          if (res.data.code === 200) {
            _this.availableRecruitments = res.data.data.recruitments;
          } else {
            _this.$dialog.error({
              title: "拉取可用招新失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "拉取可用招新异常",
            text: error.toString()
          });
        });
      },
      toSpecificRecruitment(rid) {
        this.$router.push("/user/specific/" + rid);
      },
      getJoinedRecruitments() {
        let _this = this;
        this.axios({
          method: 'get',
          url: '/studentOfRecruitment/getsByUserId',
          params: {
            userId: this.$store.getters.user.id
          }
        }).then(res => {
          if (res.data.code === 200) {
            _this.studentOfRecruitments = res.data.data.sors;
            _this.recruitments = res.data.data.recruitments;
          } else {
            _this.$dialog.error({
              title: "拉取加入的招新失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "拉取加入的招新异常",
            text: error.toString()
          });
        });
      }
    },
    computed: {
    },
    mounted() {
      this.getJoinedRecruitments();
    }
  }
</script>