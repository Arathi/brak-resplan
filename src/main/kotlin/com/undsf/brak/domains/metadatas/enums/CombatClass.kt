package com.undsf.brak.domains.metadatas.enums

/**
 * 部队类型
 */
enum class CombatClass {
    Striker, // 突击
    Special; // 支援
}

fun String.toCombatClass(): CombatClass {
    return CombatClass.valueOf(this)
}