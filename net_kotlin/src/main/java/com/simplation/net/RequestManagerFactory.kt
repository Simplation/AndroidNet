package com.simplation.net

import com.simplation.net.interf.IRequest
import com.simplation.net.retrofit.manager.RetrofitRequestManager

/**
 * RequestManager 工厂，提供获取 RequestManager 方法，应用层直接调用[getRequestManager]即可，无需关心内部实现逻辑
 */
internal object RequestManagerFactory {
    fun getRequestManager(): IRequest {
        return RetrofitRequestManager.INSTANCE
    }
}