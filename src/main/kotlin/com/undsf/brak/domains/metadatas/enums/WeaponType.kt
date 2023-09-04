package com.undsf.brak.domains.metadatas.enums

enum class WeaponType {
    AR,
    FT,
    GL,
    HG,
    MG,
    MT,
    RG,
    RL,
    SG,
    SMG,
    SR,
    ETC,
}

fun String.toWeaponType(): WeaponType {
    return when(this) {
        else -> WeaponType.valueOf(this)
    }
}
