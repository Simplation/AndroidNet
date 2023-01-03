package com.simplation.net

import com.simplation.net.config.NetOptions
import com.simplation.net.interf.IRequest

object NetKit {
    var options: NetOptions = NetOptions.Builder().build()

    fun init(options: NetOptions) {
        this.options = options
    }

    fun getRequestManager(): IRequest {
        return RequestManagerFactory.getRequestManager()
    }
}