module.exports = {
    devServer: {
        disableHostCheck: false,
        host: "localhost",
        port: 8080,
        https: false,
        hotOnly: false,
        proxy: null
    },
    publicPath: './',
    // 输出文件目录
    outputDir: 'dist',
};