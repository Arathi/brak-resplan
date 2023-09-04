package com.undsf.brak.domains.metadatas.enums

enum class EquipmentCategory(
    var slot: Int
) {
    Hat(1),
    Gloves(1),
    Shoes(1),
    Bag(2),
    Badge(2),
    Hairpin(2),
    Charm(3),
    Watch(3),
    Necklace(3)
}

fun String.toEquipmentCategory(): EquipmentCategory {
    return EquipmentCategory.valueOf(this)
}