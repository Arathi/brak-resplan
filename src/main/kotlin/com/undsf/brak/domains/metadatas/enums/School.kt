package com.undsf.brak.domains.metadatas.enums

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(School::class.java)

/**
 * 学院
 */
enum class School {
    Abydos, // 阿拜多斯
    Arius, // 阿里乌斯
    Gehenna, // 歌赫娜
    Hyakkiyako, // 百鬼夜行
    Millennium, // 千禧年 千年科技
    RedWinter, // 红冬 / 赤冬
    Shanhaijing, // 山海经
    SRT, // SRT
    Trinity, // 崔尼蒂 / 圣三一
    Valkyrie, // 瓦尔基丽 / 女武神
    ETC; // 其他（目前只有初音未来）
}
