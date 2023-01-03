package com.simplation.net

import android.app.Application
import com.simplation.net.config.NetOptions

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化网络请求
        val options = NetOptions.Builder()
            .setLogEnable(true)
            .setLogTag("Simplation")
            .setBaseUrl("https://api.oick.cn/")
            .setParserCls(CustomParser1::class)
            .build()

        NetKit.init(options)
    }
}