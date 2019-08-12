module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/task-fe/' : '/',

  pwa: {
    name: 'task-fe'
  },

  lintOnSave: process.env.NODE_ENV !== 'production'
}
