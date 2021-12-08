<template>
  <div>
    <navibar subname="管理员端"></navibar>
    <v-dialog
        v-model="ifShowDetail"
        width="500"
    >
      <v-card>
        <v-card-title>详细信息</v-card-title>
        <v-card-text>
          <p>
            姓名：{{ showStudentOfRecruitment.name }}
          </p>
          <p>
            学院：{{ showStudentOfRecruitment.department }}
          </p>
          <p>
            专业班级：{{ showStudentOfRecruitment.majorAndClass }}
          </p>
          <p>
            年级：{{ showStudentOfRecruitment.grade }}
          </p>
          <p>
            性别：{{ showStudentOfRecruitment.sex }}
          </p>
          <p>
            推荐人：{{ showStudentOfRecruitment.recommender }}
          </p>
          <p>
            当前状态：{{showStudentOfRecruitment.isEnd ? '结束招新流程' : '正常'}}
          </p>
          <v-textarea label="自我介绍" v-model="showStudentOfRecruitment.introduction" readonly>
          </v-textarea>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog
        v-model="ifBatchUpdate"
        persistent
        width="500"
    >
      <v-card>
        <v-card-title>批量修改{{selected.length}}人状态</v-card-title>
        <v-card-text>
          {{batchStatus.nowStep}}
          <v-select
              :items="recruitment.steps"
              v-model="batchStatus.nowStep"
              label="步骤"
          ></v-select>
          <v-radio-group v-model="batchStatus.isEnd">
            <v-radio
                label="正常"
                :value="0"
            ></v-radio>
            <v-radio
                label="结束招新流程"
                :value="1"
            ></v-radio>
          </v-radio-group>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="green darken-1"
              text
              @click="ifBatchUpdate = false"
          >
            取消
          </v-btn>
          <v-btn
              color="green darken-1"
              text
              @click="batchUpdate"
          >
            修改
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-container  fluid>
      <v-row align="start" justify="center">
        <v-col>
          <v-card>
            <v-card-title>
              {{ recruitment.name }}
              <v-spacer>

              </v-spacer>
              <v-btn v-if="selected.length > 0" color="primary" @click="ifBatchUpdate=true">
                修改招新状态
              </v-btn>
            </v-card-title>
            <v-card-text>
              <v-container  fluid v-if="finished">
                <v-row align="start" justify="center" v-for="(step, index) in recruitment.steps" :key="index">
                  <v-col>
                    <v-card>
                      <v-card-title>{{step}}  共{{studentOfRecruitmentMap[index].length}}人</v-card-title>
                      <v-card-text>
                        <v-container fluid>
                          <v-row dense>
                            <v-col
                                v-for="sor in studentOfRecruitmentMap[index]"
                                :key="sor.id"
                                xs="12"
                                sm="6"
                                md="4"
                                lg="3"
                                xl="2"
                            >
                              <v-card>
                                <v-card-title>
                                  {{sor.name}}
                                  <v-checkbox v-model="selected" :value="sor.id"></v-checkbox>
                                </v-card-title>
                                <v-card-text>{{sor.department}}/{{sor.grade}}</v-card-text>
                                <v-card-actions>
                                  <v-spacer></v-spacer>
                                  <v-btn
                                      color="primary"
                                      text
                                      @click="showDetail(sor)"
                                  >
                                    显示详细信息
                                  </v-btn>
                                </v-card-actions>
                              </v-card>
                            </v-col>
                          </v-row>
                        </v-container>
                      </v-card-text>
                    </v-card>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Navibar from "../../components/Navibar";
export default {
  name: 'Concrete',
  components: {Navibar},
  data: () => ({
    recruitment: {},
    studentOfRecruitmentMap: [],
    finished: false,
    selected: [],
    ifShowDetail: false,
    showStudentOfRecruitment: {},
    ifBatchUpdate: false,
    batchStatus: {
      nowStep: '',
      isEnd: 0,
    }
  }),
  methods: {
    getNowRecruitmentInfo() {
      let _this = this;
      this.axios({
        method: 'get',
        url: '/recruitment/' + this.$route.params.recruitmentId
      }).then(res => {
        if (res.data.code === 200) {
          _this.recruitment = res.data.data.recruitment;
          _this.getStudentOfRecruitments();
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
    getStudentOfRecruitments() {
      let _this = this;
      this.axios({
        method: 'get',
        url: '/studentOfRecruitment/getsByRecruitmentId',
        params: {
          recruitmentId: this.recruitment.id
        }
      }).then(res => {
        if (res.data.code === 200) {
          for (let i=0; i < _this.recruitment.steps.length; i++) {
            _this.studentOfRecruitmentMap[i] = [];
          }
          for (let sor of res.data.data.sors) {
            let step = sor.nowStep;
            _this.studentOfRecruitmentMap[step].push(sor);
          }
          _this.finished = true;
        } else {
          _this.$dialog.error({
            title: "拉取当前招新具体情况失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "拉取当前招新具体情况异常",
          text: error.toString()
        });
      });
    },
    showDetail(sor) {
      this.showStudentOfRecruitment = sor;
      this.ifShowDetail = true;
    },
    async batchUpdate() {
      const res = await this.$dialog.confirm({
        text: '你确认要更新选手招新状态吗？',
        title: '更新选手招新状态'
      })
      if (res !== true) {
        return ;
      }
      this.ifBatchUpdate = false;
      this.finished = false;
      let trueStep = 0;
      for (let i = 0; i < this.recruitment.steps.length; i++) {
        if (this.recruitment.steps[i] === this.batchStatus.nowStep) {
          trueStep = i;
          break;
        }
      }
      let _this = this;
      this.axios({
        method: 'post',
        url: '/studentOfRecruitment/batchUpdate',
        data: {
          "ids": this.selected,
          "nowStep": trueStep,
          "isEnd": this.batchStatus.isEnd
        }
      }).then(res => {
        if (res.data.code === 200) {
          _this.$dialog.message.info('批量更新成功', {
            position: 'bottom-left',
            timeout: 1000
          })
          _this.getStudentOfRecruitments();
        } else {
          _this.$dialog.error({
            title: "批量更新失败",
            text: res.data.message
          });
        }
      }).catch(error => {
        _this.$dialog.error({
          title: "批量更新异常",
          text: error.toString()
        });
      });
    }
  },
  computed: {
  },
  mounted() {
    this.getNowRecruitmentInfo();
  }
}
</script>