module.exports = {
    devServer: {
        disableHostCheck: false,
        host: "localhost",
        port: 8080,
        https: false,
        hotOnly: false,
        proxy: null,
        //public:'240e:45f:6290:1dcc:716b:be7e:4473:aadb:8080'
    },
    publicPath: './',
    // 输出文件目录
    outputDir: 'dist',
};