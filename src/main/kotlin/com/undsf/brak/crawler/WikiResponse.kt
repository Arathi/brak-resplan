package com.undsf.brak.crawler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

class Parse(
    var title: String? = null,
    var pageid: Int? = null,
    var wikitext: Map<String, String>? = null
)

class Query(
    @JsonProperty("categorymembers")
    var categoryMembers: List<CategoryMember>? = null,
)

class ContinueInfo(
    @JsonProperty("cmcontinue")
    var categoryMembersContinue: String? = null,

    @JsonProperty("continue")
    var continueType: String? = null,
)

class CategoryMember(
    /**
     * 页面编号
     */
    @JsonProperty("pageid")
    var pageId: Int,

    /**
     * 命名空间？
     */
    var ns: Int,

    /**
     * 页面标题
     */
    var title: String,
) {
    override fun toString(): String {
        return "$pageId | $title"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class WikiResponse(
    /**
     * 分页
     */
    @JsonProperty("continue")
    var continueInfo: ContinueInfo? = null,

    /**
     * 解析结果
     */
    var parse: Parse? = null,

    /**
     * 查询结果
     */
    var query: Query? = null,
)
