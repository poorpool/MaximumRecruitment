<template>
  <v-card>
    <v-dialog
        v-model="ifEditRecruitment"
        persistent
        max-width="1200px"
    >
      <v-card>
        <v-card-title>
          {{ nowEdit ? '编辑招新' : '新建招新'}}
        </v-card-title>
        <v-card-text>
          <v-text-field
              v-model="editData.name"
              :rules="nameRules"
              label="招新名称"
              required
          ></v-text-field>

<!--          <v-text-field-->
<!--              v-model="editData.startTime"-->
<!--              :rules="timeRules"-->
<!--              type="date"-->
<!--              label="开始时间（当天零点）"-->
<!--              required-->
<!--          ></v-text-field>-->

          <v-container>
            <v-row>
              <v-col cols="6">
                <v-menu
                    v-model="menuStartDate"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="startDate"
                        label="开始日期"
                        prepend-icon="mdi-calendar"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker
                      v-model="startDate"
                      @input="menuStartDate = false"
                  ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="6">
                <v-menu
                    ref="menuStartTime"
                    v-model="menuStartTime"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    :return-value.sync="startTime"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="startTime"
                        label="开始时间"
                        prepend-icon="mdi-clock-time-four-outline"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker
                      v-if="menuStartTime"
                      v-model="startTime"
                      full-width
                      format="24hr"
                      use-seconds
                      @click:second="$refs.menuStartTime.save(startTime)"
                  ></v-time-picker>
                </v-menu>
              </v-col>
            </v-row>
          </v-container>

          <v-container>
            <v-row>
              <v-col cols="6">
                <v-menu
                    v-model="menuEndDate"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="endDate"
                        label="结束日期"
                        prepend-icon="mdi-calendar"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                    ></v-text-field>
                  </template>
                  <v-date-picker
                      v-model="endDate"
                      @input="menuEndDate = false"
                  ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="6">
                <v-menu
                    ref="menuEndTime"
                    v-model="menuEndTime"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    :return-value.sync="endTime"
                    transition="scale-transition"
                    offset-y
                    max-width="290px"
                    min-width="290px"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <v-text-field
                        v-model="endTime"
                        label="结束时间"
                        prepend-icon="mdi-clock-time-four-outline"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker
                      v-if="menuEndTime"
                      v-model="endTime"
                      full-width
                      format="24hr"
                      use-seconds
                      @click:second="$refs.menuEndTime.save(endTime)"
                  ></v-time-picker>
                </v-menu>
              </v-col>
            </v-row>
          </v-container>

<!--          <v-text-field-->
<!--              v-model="editData.endTime"-->
<!--              :rules="timeRules"-->
<!--              type="date"-->
<!--              label="结束时间（当天零点）"-->
<!--              required-->
<!--          ></v-text-field>-->
          <v-text-field
              v-model="editData.steps"
              :rules="stepsRules"
              label="步骤（以/分隔）"
              required
              placeholder="报名/笔试/一面/二面/通过"
              :readonly="nowEdit"
          ></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="blue darken-1"
              text
              @click="ifEditRecruitment=false"
          >
            取消
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="submitUpsert"
          >
            确认
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-card-title>
      招新列表
      <v-spacer></v-spacer>
      <v-btn
          color="primary"
          dark
          @click="addRecruitment"
      >
        新建招新
      </v-btn>
    </v-card-title>
    <v-card-text>
      <p>当前时间：{{this.dateFormat("yyyy-MM-dd HH:mm:SS", new Date())}}</p>
      <v-data-table
          :headers="recruitmentsHeader"
          :items="recruitments"
          :options.sync="recruitmentsOption"
          :server-items-length="recruitmentsTotal"
          :loading="recruitmentsLoading"
          class="elevation-1"
      >
        <template v-slot:item.nowStatus ="{item}">
          <v-chip v-if="item.nowStatus==='未开始'" color="blue" dark>未开始</v-chip>
          <v-chip v-if="item.nowStatus==='进行中'" color="green" dark>进行中</v-chip>
          <v-chip v-if="item.nowStatus==='已结束'" color="red" dark>已结束</v-chip>
        </template>
        <template v-slot:item.actions ="{item}">
          <v-icon
              small
              class="mr-2"
              @click="editRecruitment(item)"
          >
            mdi-pencil
          </v-icon>
          <v-icon
              small
              @click="deleteRecruitment(item)"
          >
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>
<script>

export default {
  name: 'AdminRecruits',
  components: {},
  data: () => ({
    nameRules: [
      v => !!v || '请输入招新名称'
    ],
    timeRules: [
      v => !!v || '请输入日期'
    ],
    stepsRules: [
      v => !!v || '请输入招新步骤'
    ],
    startDate: new Date().toISOString().substr(0, 10),
    startTime: "00:00:00",
    menuStartDate: false,
    menuStartTime: false,
    endDate: new Date().toISOString().substr(0, 10),
    endTime: "00:00:00",
    menuEndDate: false,
    menuEndTime: false,
    ifEditRecruitment: false,
    nowEdit: false, // true 编辑，false 新建
    editData: {
      id: "",
      name: "",
      startTime: {},
      endTime: {},
      steps: ""
    },
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
        text: '开始时间（北京时间）',
        sortable: false,
        value: 'startTime'
      },
      {
        text: '结束时间（北京时间）',
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
    addRecruitment() {
      this.editData = {
        id: "",
        name: "",
        startTime: {},
        endTime: {},
        steps: ""
      };
      this.nowEdit = false;
      this.ifEditRecruitment = true;
    },
    dateFormat(fmt, date) {
      let ret;
      const opt = {
        "y+": date.getFullYear().toString(),        // 年
        "M+": (date.getMonth()+1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "m+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      };
      for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length === 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
      }
      return fmt;
    },
    editRecruitment(item) {
      this.editData = item;
      this.startTime = item.startTime.substr(11, 8);
      this.startDate = item.startTime.substr(0, 10);
      this.endTime = item.endTime.substr(11, 8);
      this.endDate = item.endTime.substr(0, 10);
      this.nowEdit = true;
      this.ifEditRecruitment = true;
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
            rec.startTime = this.dateFormat("yyyy-MM-dd HH:mm:SS", startDate);
            rec.endTime = this.dateFormat("yyyy-MM-dd HH:mm:SS", endDate);
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
    },
    async deleteRecruitment(item) {
      let _this = this;
      const res = await this.$dialog.confirm({
        text: '你确认要删除招新 ' + item.name + ' 吗？',
        title: '删除招新'
      })
      if (res !== true) {
        return ;
      }
      this.axios({
        method: 'post',
        url: '/recruitment/delete',
        params: {
          id: item.id
        }
      }).then(res => {
        if (res.data.code === 200) {
          _this.$dialog.message.info('删除招新成功', {
            position: 'bottom-left',
            timeout: 1000
          })
          _this.getRecruitmentsByPage();
        } else {
          _this.$dialog.error({
            title: "删除招新失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "删除招新异常",
          text: error.toString()
        });
      });
    },
    submitUpsert() {
      console.log(this.editData);
      this.editData.steps = this.editData.steps.split('/')
      for (let i=0; i < this.editData.steps.length; i++) {
        this.editData.steps[i] = this.editData.steps[i].trim();
      }
      this.editData.startTime = this.startDate + "T" + this.startTime + "+08";
      this.editData.endTime = this.endDate + "T" + this.endTime + "+08";
      let _this = this;
      this.axios({
        method: 'post',
        url: '/recruitment/upsert',
        data: _this.editData
      }).then(res => {
        if (res.data.code === 200) {
          _this.$dialog.message.info('提交成功', {
            position: 'bottom-left',
            timeout: 1000
          });
          _this.ifEditRecruitment = false;
          _this.getRecruitmentsByPage();
        } else {
          _this.$dialog.error({
            title: "提交失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "提交异常",
          text: error.toString()
        });
      });
    }
  },
  computed: {
  }
}
</script>