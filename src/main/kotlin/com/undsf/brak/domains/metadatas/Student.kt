package com.undsf.brak.domains.metadatas

import com.fasterxml.jackson.annotation.JsonIgnore
import com.undsf.brak.domains.metadatas.enums.*

/**
 * 学生基本信息
 */
class Student(
    /**
     * ID
     */
    val id: Int,

    /**
     * Key
     */
    val wikiPage: String,

    /**
     * 学院
     */
    val school: School,

    /**
     * 武器类型
     */
    val weaponType: WeaponType,

    /**
     * 是否使用掩体
     */
    val usesCover: Boolean,

    /**
     * 稀有度，初始星级
     */
    val rarity: Int,

    /**
     * 部队类型
     */
    val combatClass: CombatClass,

    /**
     * 攻击类型
     */
    val attackType: AttackType,

    /**
     * 护甲类型
     */
    val armorType: ArmorType,

    /**
     * 站位
     */
    val position: Position,

    /**
     * 职责
     */
    val role: Role,

    /**
     * 街区战
     */
    val urban: String,

    /**
     * 野外战
     */
    val outdoors: String,

    /**
     * 室内战
     */
    val indoors: String,

    /**
     * 装备类型-装备槽1
     */
    val equipmentSlot1: EquipmentCategory,

    /**
     * 装备类型-装备槽2
     */
    val equipmentSlot2: EquipmentCategory,

    /**
     * 装备类型-装备槽3
     */
    val equipmentSlot3: EquipmentCategory,

    /**
     * 卡池类型
     */
    val pool: PoolType,

    /**
     * 实装日期
     */
    val releaseDate: String,
) {
    @get:JsonIgnore
    val versionName: String? get() {
        val bracketStartAt = wikiPage.indexOf("(")
        if (bracketStartAt > 0 && wikiPage.endsWith(")")) {
            return wikiPage.substring(bracketStartAt + 1, wikiPage.length - 1)
        }
        return null
    }
}

class StudentIntroduction(
    val name: String,
    val club: String,
    val age: Int?,
    val birthday: String?,
    val height: Int?,
    val hobbies: String,
    val designer: String,
    val illustrator: String,
    val castVoice: String,
) {
}
