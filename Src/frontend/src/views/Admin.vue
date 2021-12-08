<template>
  <div class="fill-height">
    <navibar subname="超级管理员端"></navibar>

    <v-container  fluid>
      <v-row align="start" justify="center">
        <v-col>
          <v-card>
            <v-card-title>招新系统管理</v-card-title>
            <v-card-text>

              <v-tabs vertical>
                <v-tab>
                  管理成员
                </v-tab>
                <v-tab>
                  管理招新
                </v-tab>

                <v-tab-item>
                  <AdminAccounts></AdminAccounts>
                </v-tab-item>
                <v-tab-item>
                  <AdminRecruits></AdminRecruits>
                </v-tab-item>
              </v-tabs>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
  import Navibar from "../components/Navibar";
  import AdminAccounts from "../components/admin/accounts";
  import AdminRecruits from "../components/admin/recruits";
  export default {
    name: 'Admin',
    components: {AdminRecruits, AdminAccounts, Navibar},
    data: () => ({
    }),
    watch: {
    },
    methods: {
    },
    computed: {
    }
  }
</script>