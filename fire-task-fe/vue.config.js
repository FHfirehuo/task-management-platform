module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/fire-task-fe/' : '/',

  pwa: {
    name: 'fire-task-fe'
  },

  lintOnSave: process.env.NODE_ENV !== 'production',

  filenameHashing: false,
  /**
   * 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
   *  打包之后发现map文件过大，项目文件体积很大，设置为false就可以不输出map文件
   *  map文件的作用在于：项目打包后，代码都是经过压缩加密的，如果运行时报错，输出的错误信息无法准确得知是哪里的代码报错。
   *  有了map就可以像未加密的代码一样，准确的输出是哪一行哪一列有错。
   */
  productionSourceMap: false,
  // css相关配置
  css: {
    // 是否使用css分离插件 ExtractTextPlugin
    extract: true,
    // 开启 CSS source maps?
    sourceMap: false,
    // css预设器配置项
    loaderOptions: {

      // pass options to sass-loader
      // @/ is an alias to src/
      // so this assumes you have a file named `src/variables.sass`
      // sass: {
      //   data: `@import "~@/styles/variables.scss";`
      // }

    },
    // 启用 CSS modules for all css / pre-processor files.
    modules: false
  },

  // 构建后的文件是部署在 CDN 上的，启用该选项可以提供额外的安全性, 默认false
  integrity: true,

  configureWebpack: config => {},

  // webpack 链接 API，用于生成和修改 webapck 配置
  // https://github.com/mozilla-neutrino/webpack-chain
  chainWebpack: (config) => {
    // 因为是多页面，所以取消 chunks，每个页面只对应一个单独的 JS / CSS
    config.optimization
      .splitChunks({
        cacheGroups: {}
      });

    // 'src/lib' 目录下为外部库文件，不参与 eslint 检测
    config.module
      .rule('eslint')
      .exclude
      .add('/Users/maybexia/Downloads/FE/community_built-in/src/lib')
      .end()
  }

}
