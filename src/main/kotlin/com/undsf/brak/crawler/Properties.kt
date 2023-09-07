package com.undsf.brak.crawler

import com.undsf.brak.crawler.exceptions.WikiTextPropertyException
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger(Properties::class.java)

class Properties(
    val name: String,
    val values: MutableMap<String, String> = mutableMapOf()
): Node(Type.Properties) {
    fun addProperty(key: String, value: String) {
        values[key] = value
    }

    @Throws(WikiTextPropertyException::class)
    fun getProperty(key: String): String {
        if (!values.containsKey(key)) {
            throw WikiTextPropertyException(name, key, "属性不存在")
        }
        return values[key]!!
    }

    @Deprecated("不建议使用非空", ReplaceWith("getProperty(key: String)"))
    fun getPropertyOrDefault(key: String, defaultValue: String? = null): String? {
        return values.getOrDefault(key, defaultValue)
    }

    @Throws(WikiTextPropertyException::class)
    fun getPropertyAsInt(key: String): Int {
        val strValue = getProperty(key)
        val intValue = strValue.toIntOrNull()
        if (intValue == null) {
            log.warn("数值转换出错：${strValue}")
            throw WikiTextPropertyException(name, key, "字符串`${strValue}`无法转换为整数")
        }
        return intValue
    }
}