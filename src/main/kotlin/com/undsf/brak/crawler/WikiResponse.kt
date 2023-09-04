package com.undsf.brak.crawler

class Parse(
    var title: String? = null,
    var pageid: Int? = null,
    var wikitext: Map<String, String>? = null
)

class Query(
    var categorymembers: List<CategoryMember>? = null,
)

class CategoryMember(
    var pageid: Int? = null,
    var ns: Int? = null,
    var title: String? = null,
)

class WikiResponse(
    var parse: Parse? = null,
    var query: Query? = null,
)
