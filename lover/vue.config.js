module.exports = {
    devServer: {
        disableHostCheck: false,
        host: "localhost",
        port: 8080,
        https: false,
        hotOnly: false,
        proxy: null,
        //public:'192.168.3.6:8080'
    },
    publicPath: './',
    // 输出文件目录
    outputDir: 'dist',
};