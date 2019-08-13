module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/fire-task-fe/' : '/',

  pwa: {
    name: 'task-fe'
  },

  lintOnSave: process.env.NODE_ENV !== 'production'
}
