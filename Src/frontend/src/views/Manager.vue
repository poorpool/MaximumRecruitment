<template>
  <div>
    <navibar subname="管理员端"></navibar>
    <v-container  fluid>
      <v-row align="start" justify="center">
        <v-col>
          <v-card>
            <v-card-title>招新系统管理</v-card-title>
            <v-card-text>

              <v-data-table
                  :headers="recruitmentsHeader"
                  :items="recruitments"
                  :options.sync="recruitmentsOption"
                  :server-items-length="recruitmentsTotal"
                  :loading="recruitmentsLoading"
                  class="elevation-1"
              >
                <template v-slot:item.actions ="{item}">
                  <v-icon
                      small
                      class="mr-2"
                      @click="showRecruitment(item)"
                  >
                    mdi-information
                  </v-icon>
                </template>
              </v-data-table>
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
    name: 'Manager',
    components: {Navibar},
    data: () => ({
      recruitmentsTotal: 0,
      recruitments: [],
      recruitmentsLoading: true,
      recruitmentsOption: {},
      recruitmentsHeader: [
        {
          text: '招新名称',
          sortable: false,
          value: 'name'
        },
        {
          text: '当前状态',
          sortable: false,
          value: 'nowStatus'
        },
        {
          text: '开始时间（当天零点）',
          sortable: false,
          value: 'startTime'
        },
        {
          text: '结束时间（当天零点）',
          sortable: false,
          value: 'endTime'
        },
        {
          text: '步骤',
          sortable: false,
          value: 'steps'
        },
        {
          text: '操作',
          sortable: false,
          value: 'actions'
        }
      ]
    }),
    watch: {
      recruitmentsOption: {
        handler () {
          this.getRecruitmentsByPage();
        },
        deep: true,
      },
    },
    methods: {
      showRecruitment(item) {
        this.$router.push("/manager/concrete/" + item.id);
      },
      getRecruitmentsByPage () {
        this.recruitmentsLoading = true;
        let _this = this;
        this.axios({
          method: 'get',
          url: '/recruitment/get',
          params: {
            page: _this.recruitmentsOption.page,
            itemsPerPage: _this.recruitmentsOption.itemsPerPage
          }
        }).then(res => {
          if (res.data.code === 200) {
            _this.recruitments = res.data.data.recruitments;
            _this.recruitmentsTotal = res.data.data.total;
            _this.recruitmentsLoading = false;
            for (let i = 0; i < _this.recruitments.length; i++) {
              let rec = _this.recruitments[i];
              rec.steps = rec.steps.join('/');
              let nowDate = new Date();
              let startDate = new Date(Date.parse(rec.startTime));
              let endDate = new Date(Date.parse(rec.endTime));
              if (nowDate < startDate) {
                rec.nowStatus = "未开始";
              } else if (nowDate > endDate) {
                rec.nowStatus = "已结束";
              } else {
                rec.nowStatus = "进行中";
              }
              rec.startTime = startDate.toLocaleDateString();
              rec.endTime = endDate.toLocaleDateString();
            }
          } else {
            _this.$dialog.error({
              title: "拉取招新信息失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "拉取招新信息异常",
            text: error.toString()
          });
        });
      }
    },
    computed: {
    }
  }
</script>