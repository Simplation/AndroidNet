package com.simplation.net.cipher

/**
 * 加解密器抽象接口
 */
interface ICipher {
    /**
     * 加密数据
     */
    fun encrypt(original: String?): String?

    /**
     * 解密数据
     */
    fun decrypt(original: String?): String?

    /**
     * 获取加解密字段名称
     */
    fun getParamName(): String
}