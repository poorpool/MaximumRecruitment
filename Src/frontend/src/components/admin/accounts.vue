<template>
  <v-card>
    <v-dialog
        v-model="ifEditAccount"
        persistent
        max-width="600px"
    >
      <v-card>
        <v-card-title>
          编辑账户信息
        </v-card-title>
        <v-card-text>
          <v-text-field
              v-model="editData.email"
              label="邮箱地址"
              readonly
          ></v-text-field>
          <v-text-field
              v-model="editData.nickname"
              :rules="nicknameRules"
              label="昵称"
              required
          ></v-text-field>
          <v-text-field
              v-model="editData.password"
              type="password"
              label="密码（留空代表不修改）"
          ></v-text-field>
          <v-text-field
              v-model="editData.permission"
              :rules="permissionrules"
              type="number"
              label="权限"
              required
              readonly
          ></v-text-field>
          <v-checkbox v-model="selectPermission" label="管理员" value="2"></v-checkbox>
          <v-checkbox v-model="selectPermission" label="用户" value="1"></v-checkbox>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="blue darken-1"
              text
              @click="ifEditAccount = false"
          >
            取消
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="submitEdit"
          >
            提交修改
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-card-text>
      <p>
        cyx：加上多选框
      </p>
      <v-data-table
          :headers="accountsHeader"
          :items="accounts"
          :options.sync="accountsOption"
          :server-items-length="accountsTotal"
          :loading="accountsLoading"
          class="elevation-1"
      >
        <template v-slot:item.permission ="{item}">
          <v-chip v-if="(item.permission&4)===4" color="purple" dark>超级管理员</v-chip>
          <v-chip v-if="(item.permission&2)===2" color="blue" dark>管理员</v-chip>
          <v-chip v-if="(item.permission&1)===1" color="green" dark>用户</v-chip>
        </template>
        <template v-slot:item.actions ="{item}">
          <v-icon
              small
              class="mr-2"
              @click="editAccount(item)"
              v-if="(item.permission & 4)!==4"
          >
            mdi-pencil
          </v-icon>
          <v-icon
              small
              @click="deleteAccount(item)"
              v-if="(item.permission & 4)!==4"
          >
            mdi-delete
          </v-icon>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>
<script>
  import sha256 from "crypto-js/sha256";

  export default {
    name: 'AdminAccounts',
    components: {},
    data: () => ({
      nicknameRules: [
        v => !!v || '请输入昵称'
      ],
      permissionrules: [
        v => v >= 0 && v <= 3 || '权限应在0至3之间'
      ],
      selectPermission: [],
      ifEditAccount: false,
      editData: {},
      accountsTotal: 0,
      accounts: [],
      accountsLoading: true,
      accountsOption: {},
      accountsHeader: [
        {
          text: '电子邮件',
          sortable: false,
          value: 'email'
        },
        {
          text: '昵称',
          sortable: false,
          value: 'nickname'
        },
        {
          text: '权限',
          sortable: false,
          value: 'permission'
        },
        {
          text: '操作',
          sortable: false,
          value: 'actions'
        }
      ]
    }),
    watch: {
      accountsOption: {
        handler () {
          this.getAccountsByPage();
        },
        deep: true,
      },
      selectPermission: {
        handler () {
          this.editData.permission = 0;
          for (let x of this.selectPermission) {
            this.editData.permission |= x;
          }
        },
        deep: true,
      }
    },
    methods: {
      editAccount(item) {
        this.editData = item;
        let permission = item.permission;
        this.selectPermission = [];
        if ((permission & 2) === 2) {
          this.selectPermission.push("2");
        }
        if ((permission & 1) === 1) {
          this.selectPermission.push("1");
        }
        this.ifEditAccount = true;
      },
      getAccountsByPage () {
        this.accountsLoading = true;
        let _this = this;
        this.axios({
          method: 'get',
          url: '/user/get',
          params: {
            page: _this.accountsOption.page,
            itemsPerPage: _this.accountsOption.itemsPerPage
          }
        }).then(res => {
          if (res.data.code === 200) {
            _this.accounts = res.data.data.accounts;
            _this.accountsTotal = res.data.data.total;
            _this.accountsLoading = false;
          } else {
            _this.$dialog.error({
              title: "拉取账户信息失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "拉取账户信息异常",
            text: error.toString()
          });
        });
      },
      async deleteAccount(item) {
        let _this = this;
        const res = await this.$dialog.confirm({
          text: '你确认要删除用户 ' + item.email + ' 吗？',
          title: '删除用户'
        })
        if (res !== true) {
          return ;
        }
        this.axios({
          method: 'delete',
          url: '/user/' + item.id
        }).then(res => {
          if (res.data.code === 200) {
            _this.$dialog.message.info('删除账户成功', {
              position: 'bottom-left',
              timeout: 1000
            })
            _this.getAccountsByPage();
          } else {
            _this.$dialog.error({
              title: "删除账户失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "删除账户异常",
            text: error.toString()
          });
        });
      },
      submitEdit() {
        let _this = this;
        if (_this.editData.password !== '') {
          _this.editData.password = sha256(_this.editData.email + _this.editData.password).toString();
        }
        this.axios({
          method: 'put',
          url: '/user/update',
          data: _this.editData
        }).then(res => {
          if (res.data.code === 200) {
            _this.$dialog.message.info('更新账户成功', {
              position: 'bottom-left',
              timeout: 1000
            });
            _this.ifEditAccount = false;
            _this.getAccountsByPage();
          } else {
            _this.$dialog.error({
              title: "更新账户失败",
              text: res.data.message
            });
          }
        }).catch(error => {
          _this.$dialog.error({
            title: "更新账户异常",
            text: error.toString()
          });
        });
      }
    },
    computed: {
    },
  }
</script>