package com.undsf.brak.domains.metadatas.enums

enum class Environment {
    Urban,
    Outdoors,
    Indoors,
}

fun String.toEnvironment(): Environment {
    return when(this) {
        else -> Environment.valueOf(this)
    }
}
