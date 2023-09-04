package com.undsf.brak.crawler

class Tag(
    var name: String,
    var properties: MutableMap<String, String> = mutableMapOf()
): Node("tag") {
    fun addProperty(key: String, value: String) {
        properties[key] = value
    }

    fun getProperty(key: String, defaultValue: String? = null): String? {
        return properties.getOrDefault(key, defaultValue)
    }

    fun getPropertyAsInt(key: String): Int? {
        val value = getProperty(key) ?: return null
        return value.toIntOrNull()
    }
}