package com.undsf.brak.resplan.messages

import com.fasterxml.jackson.annotation.JsonInclude

class DataResponse<D>(
    /**
     * 状态码
     */
    var code: Int = Success,

    /**
     * 描述
     */
    var message: String = messages.getOrDefault(code, "其他错误"),

    /**
     * 数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var data: D? = null
) {
    companion object {
        const val Success = 0

        const val ErrorWikiArticle = 1001
        const val ErrorWikiTextParse = 1002

        const val ErrorWikiCharacter = 1100
        const val ErrorWikiCharacterBackground = 1101
        const val ErrorWikiCharacterStatTable = 1102
        const val ErrorWikiCharacterAffectionTable = 1103
        const val ErrorWikiCharacterSkillTable = 1104
        const val ErrorWikiCharacterUniqueWeapon = 1105
        const val ErrorWikiCharacterUniqueGear = 1106

        val messages: Map<Int, String> = mapOf(
            Success to "成功",
            ErrorWikiArticle to "维基条目获取失败",
            ErrorWikiTextParse to "维基源码解析失败",
            ErrorWikiCharacter to "角色-基本信息解析失败",
            ErrorWikiCharacterBackground to "角色-背景资料解析失败",
            ErrorWikiCharacterStatTable to "角色-状态数据解析失败",
            ErrorWikiCharacterAffectionTable to "角色-好感数据解析失败",
            ErrorWikiCharacterSkillTable to "角色-技能数据解析失败",
            ErrorWikiCharacterUniqueWeapon to "角色-专武数据解析失败",
            ErrorWikiCharacterUniqueGear to "角色-爱用品数据解析失败",
        )
    }
}

typealias BasicResponse = DataResponse<Void>
