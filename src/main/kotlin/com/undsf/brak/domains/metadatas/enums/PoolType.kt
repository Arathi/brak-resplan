package com.undsf.brak.domains.metadatas.enums

enum class PoolType {
    Regular,
    Event,
    Limited,
    Anniversary
}

fun String.toPoolType(): PoolType {
    // 首字母大写处理
    var name = this.lowercase()
    if (name.isNotEmpty()) {
        name = name[0].uppercase() + name.substring(1)
    }
    return PoolType.valueOf(name)
}
