const {createProxyMiddleware} = require("http-proxy-middleware");

module.exports = app => {
    app.use(
        createProxyMiddleware('/face_timekeep', {
            target: "https://192.168.45.45:5001",
            changeOrigin: true
        })
    )


}