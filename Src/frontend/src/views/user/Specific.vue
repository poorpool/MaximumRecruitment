<template>
  <div>
    <navibar subname="选手端"></navibar>

    <v-container  fluid>
      <v-row align="start" justify="center">
        <v-col>
          <v-card>
            <v-card-title>
              <v-btn
                  icon
                  color="blue"
                  @click="$router.push('/user')"
              >
                <v-icon>mdi-arrow-left</v-icon>
              </v-btn>
              {{ recruitment.name }}
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-row>
                  <v-col cols="6">
                    <v-text-field
                        :value="this.dateFormat('yyyy-MM-dd', new Date(Date.parse(this.recruitment.startTime)))"
                        label="开始日期"
                        prepend-icon="mdi-calendar"
                        readonly
                    ></v-text-field>
                  </v-col>
                  <v-col cols="6">
                    <v-text-field
                        :value="this.dateFormat('HH:mm:SS', new Date(Date.parse(this.recruitment.startTime)))"
                        label="开始时间"
                        prepend-icon="mdi-clock-time-four-outline"
                        readonly
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>

              <v-container>
                <v-row>
                  <v-col cols="6">
                    <v-text-field
                        :value="this.dateFormat('yyyy-MM-dd', new Date(Date.parse(this.recruitment.endTime)))"
                        label="结束日期"
                        prepend-icon="mdi-calendar"
                        readonly
                    ></v-text-field>
                  </v-col>
                  <v-col cols="6">
                    <v-text-field
                        :value="this.dateFormat('HH:mm:SS', new Date(Date.parse(this.recruitment.endTime)))"
                        label="结束时间"
                        prepend-icon="mdi-clock-time-four-outline"
                        readonly
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-container>
              <ShowSteps :steps="recruitment.steps" :now-step="studentOfRecruitment.nowStep" :end-status="studentOfRecruitment.isEnd" v-if="registered"></ShowSteps>
              <v-divider></v-divider>
              <v-alert
                  type="success" v-if="registered && studentOfRecruitment.isEnd === 0"
              >你已经加入了这个招新，目前处于 {{recruitment.steps[studentOfRecruitment.nowStep]}} 步骤</v-alert>
              <v-alert
                  color="purple"
                  type="error"
                  v-else-if="registered"
              >你在本次招新流程的流程结束于 {{recruitment.steps[studentOfRecruitment.nowStep]}} 步骤</v-alert>
              <v-alert
                  type="info" v-else
              >你还没有加入这个招新</v-alert>
              <v-card>
                <v-card-title>报名信息</v-card-title>
                <v-card-text>
                  <v-form v-model="valid" lazy-validation ref="registerForm">
                    <v-text-field
                        v-model="studentOfRecruitment.name"
                        :rules="notEmptyRules"
                        label="你的名字"
                        required
                    ></v-text-field>
                    <v-text-field
                        v-model="studentOfRecruitment.department"
                        :rules="notEmptyRules"
                        label="学院"
                        placeholder="全称，例如计算机科学与技术学院"
                        required
                    ></v-text-field>
                    <v-text-field
                        v-model="studentOfRecruitment.majorAndClass"
                        :rules="notEmptyRules"
                        label="专业班级"
                        placeholder="例如ACM1901"
                        required
                    ></v-text-field>
                    <v-text-field
                        v-model="studentOfRecruitment.sex"
                        :rules="notEmptyRules"
                        label="性别"
                        required
                    ></v-text-field>
                    <v-text-field
                        v-model="studentOfRecruitment.grade"
                        :rules="notEmptyRules"
                        label="年级"
                        required
                    ></v-text-field>
                    <v-text-field
                        v-model="studentOfRecruitment.recommender"
                        label="推荐人"
                    ></v-text-field>
                    <v-textarea
                        v-model="studentOfRecruitment.introduction"
                        :rules="notEmptyRules"
                        label="自我介绍"
                    ></v-textarea>
                    <v-btn
                        :disabled="!valid"
                        color="success"
                        class="mr-4"
                        @click="registerRecruitment"
                    >
                      {{ registered ? '修改' : "报名招新"}}
                    </v-btn>
                  </v-form>
                </v-card-text>
              </v-card>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Navibar from "../../components/Navibar";
import ShowSteps from "../../components/user/ShowSteps";
export default {
  name: 'Specific',
  components: {ShowSteps, Navibar},
  data: () => ({
    notEmptyRules: [
      v => !!v || '不能为空',
    ],
    valid: false,
    availableRecruitments: [],
    recruitment: {},
    registered: false,
    studentOfRecruitment: {
      id: '',
      recruitmentId: '',
      userId: '',
      name: '',
      department: '',
      majorAndClass: '',
      sex: '',
      grade: '',
      introduction: '',
      recommender: '',
      nowStep: 0,
      isEnd: 0
    }
  }),
  methods: {
    validate () {
      this.$refs.registerForm.validate()
    },
    getNowRecruitmentInfo() {
      let _this = this;
      this.axios({
        method: 'get',
        url: '/recruitment/' + this.$route.params.recruitmentId
      }).then(res => {
        if (res.data.code === 200) {
          _this.recruitment = res.data.data.recruitment;
        } else {
          _this.$dialog.error({
            title: "拉取当前招新失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "拉取当前招新异常",
          text: error.toString()
        });
      });
    },
    getStudentOfRecruitment() {
      let _this = this;
      this.axios({
        method: 'get',
        url: '/studentOfRecruitment/getByRecruitmentAndUser',
        params: {
          recruitmentId: this.$route.params.recruitmentId,
          userId: this.$store.getters.user.id
        }
      }).then(res => {
        if (res.data.code === 200) {
          _this.registered = res.data.data.sor != null;
          if (_this.registered) {
            _this.studentOfRecruitment = res.data.data.sor;
          }
        } else {
          _this.$dialog.error({
            title: "拉取当前招新状态失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "拉取当前招新状态异常",
          text: error.toString()
        });
      });
    },
    registerRecruitment() {
      if (!this.$refs.registerForm.validate()) {
        return false;
      }
      let _this = this;
      this.axios({
        method: 'post',
        url: '/studentOfRecruitment/upsert',
        data: _this.studentOfRecruitment
      }).then(res => {
        if (res.data.code === 200) {
          _this.$dialog.message.info('提交成功', {
            position: 'bottom-left',
            timeout: 2000
          });
          this.getNowRecruitmentInfo();
          this.getStudentOfRecruitment();
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
    }
  },
  computed: {

  },
  mounted() {
    this.studentOfRecruitment.recruitmentId = this.$route.params.recruitmentId;
    this.studentOfRecruitment.userId = this.$store.getters.user.id;
    this.getNowRecruitmentInfo();
    this.getStudentOfRecruitment();
  }
}
</script>