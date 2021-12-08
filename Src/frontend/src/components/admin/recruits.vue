<template>
  <v-card>
    <v-dialog
        v-model="ifEditRecruitment"
        persistent
        max-width="600px"
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
          <v-text-field
              v-model="editData.startTime"
              :rules="timeRules"
              type="date"
              label="开始时间（当天零点）"
              required
          ></v-text-field>
          <v-text-field
              v-model="editData.endTime"
              :rules="timeRules"
              type="date"
              label="结束时间（当天零点）"
              required
          ></v-text-field>
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
      <p>
        cyx: 这里应该倒序显示，以及现在状态更显眼一些，加上人数等统计
      </p>
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
      this.editData.startTime = this.dateFormat("yyyy-MM-dd", new Date(Date.parse(this.editData.startTime)));
      this.editData.endTime = this.dateFormat("yyyy-MM-dd", new Date(Date.parse(this.editData.endTime)));
      console.log(this.editData);
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
        method: 'delete',
        url: '/recruitment/' + item.id
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