package com.undsf.brak.crawler.exceptions

class WikiTextPropertyException(
    val propertiesName: String,
    val propertyName: String,
    val reason: String,
): WikiTextParseException("${propertiesName}的属性${propertyName}解析出错：${reason}") {
}