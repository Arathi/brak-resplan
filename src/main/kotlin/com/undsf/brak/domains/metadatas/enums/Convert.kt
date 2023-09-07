package com.undsf.brak.domains.metadatas.enums

/**
 * 护甲类型
 */
fun String.toArmorType(): ArmorType {
    return ArmorType.valueOf(this)
}

/**
 * 攻击类型
 */
fun String.toAttackType(): AttackType {
    return AttackType.valueOf(this)
}

/**
 * 部队类型
 */
fun String.toCombatClass(): CombatClass {
    return CombatClass.valueOf(this)
}

/**
 * 作战环境
 */
fun String.toEnvironment(): Environment {
    return when(this) {
        else -> Environment.valueOf(this)
    }
}

/**
 * 装备分类
 */
fun String.toEquipmentCategory(): EquipmentCategory {
    return EquipmentCategory.valueOf(this)
}

/**
 * 卡池类型
 */
fun String.toPoolType(): PoolType {
    // 首字母大写处理
    var name = this.lowercase()
    if (name.isNotEmpty()) {
        name = name[0].uppercase() + name.substring(1)
    }
    return PoolType.valueOf(name)
}

/**
 * 站位
 */
fun String.toPosition(): Position {
    return Position.valueOf(this)
}

/**
 * 职责
 */
fun String.toRole(): Role {
    if (this == "Tactical Support") return Role.TacticalSupport
    return Role.valueOf(this)
}

/**
 * 学院
 */
fun String.toSchool(): School {
    return when (this) {
        "Red Winter" -> School.RedWinter
        else -> School.valueOf(this)
    }
}

/**
 * 武器
 */
fun String.toWeaponType(): WeaponType {
    return when(this) {
        else -> WeaponType.valueOf(this)
    }
}
