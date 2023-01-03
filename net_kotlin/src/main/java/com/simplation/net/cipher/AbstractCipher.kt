package com.simplation.net.cipher

/**
 * 抽象的加解密器
 *
 * 如果需要自定义加解密器，继承此[AbstractCipher]，实现[encrypt]及[decrypt]方法即可
 */
abstract class AbstractCipher : ICipher