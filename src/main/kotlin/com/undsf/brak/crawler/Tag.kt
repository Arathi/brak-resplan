package com.undsf.brak.crawler

@Deprecated("改用Properties", replaceWith = ReplaceWith("Properties"))
class Tag(
    var name: String,
    var properties: MutableMap<String, String> = mutableMapOf()
): Node(Type.Properties) {
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