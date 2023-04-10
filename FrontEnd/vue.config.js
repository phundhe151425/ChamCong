const {defineConfig} = require('@vue/cli-service')
const NodePolyfillPlugin = require("node-polyfill-webpack-plugin")
// const fs = require('fs');
module.exports = defineConfig({
    transpileDependencies: true
})
module.exports = {
    configureWebpack: {
        plugins: [
            new NodePolyfillPlugin()
        ]
    },
    devServer: {
        // disableHostCheck: true,
        // port:8100,
        // host: 'xxxxxx',
        https: true,
        // key: fs.readFileSync('privatekey.key'),
        // cert: fs.readFileSync('star_vmgmedia_vn_cert.pem'),
        // pfx: fs.readFileSync('./certs/xxxxxx.pfx'),
        // pfxPassphrase: "xxxxxx",
        // public: 'https://localhost:8000/selfie',
        // hotOnly: false,
    }
}


