package com.simplation.net.cipher

/**
 * 默认的加解密器
 *
 * 示例实现
 * 在[encrypt]中实现加密逻辑
 * 在[decrypt]中实现解密逻辑
 * [getParamName]配置与服务端协商好的加密字段 key
 */
class DefaultCipher : AbstractCipher() {
    override fun encrypt(original: String?): String? {
        return original
    }

    override fun decrypt(original: String?): String? {
        return original
    }

    override fun getParamName(): String {
        return "params"
    }

}