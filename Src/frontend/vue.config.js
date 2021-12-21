module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].title = "极大战队招新平台"
      return args
    })
  }
}
