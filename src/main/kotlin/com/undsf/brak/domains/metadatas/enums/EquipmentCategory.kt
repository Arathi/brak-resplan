package com.undsf.brak.domains.metadatas.enums

enum class EquipmentCategory(
    var slot: Int
) {
    // region 装备槽1
    /**
     * 帽子
     *
     * 加攻击
     * 加暴击伤害（T4）
     */
    Hat(1),

    /**
     * 手套
     *
     * 加攻击
     * 加暴击倍率（T4）
     * 加命中（T6）
     */
    Gloves(1),

    /**
     * 鞋子
     *
     * 加攻击
     * 加血量（T4）
     */
    Shoes(1),
    // endregion

    // region 装备槽2
    /**
     * 背包
     *
     * 加血量
     * 加防御（T4）
     */
    Bag(2),

    /**
     * 徽章
     *
     * 加血量
     * 加回血（T4）
     * 加血量（按比例）（T5）
     * 加闪避（T7）
     */
    Badge(2),

    /**
     * 发夹
     *
     * 加血量
     * 加CC抗性（T4）
     */
    Hairpin(2),
    // endregion

    // region 装备槽3
    /**
     * 护符
     *
     * 加暴击抗性
     * 加暴伤抵抗（T4）
     * 加暴击率（T5）
     */
    Charm(3),

    /**
     * 手表
     *
     * 加暴击抗性
     * 加暴击伤害（T4）
     * 加血量上限（按比例）（T5）
     */
    Watch(3),

    /**
     * 项链
     *
     * 加治疗量
     * 加CC
     * 加攻击（T5）
     */
    Necklace(3),
    // endregion
}
