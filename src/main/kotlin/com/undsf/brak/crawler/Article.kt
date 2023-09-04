package com.undsf.brak.crawler

import org.slf4j.LoggerFactory
import java.io.StringReader

private val log = LoggerFactory.getLogger(Article::class.java)

class Article(
    val title: String
) {
    private val tags: MutableList<Tag> = mutableListOf()

    fun addTag(tag: Tag) {
        tags.add(tag)
    }

    fun getTagsByName(name: String): List<Tag> {
        return tags.filter { t -> t.name == name }
    }

    companion object {
        private val propertiesStart = """^\{\{([A-Z][A-Za-z0-9_]+)$""".toRegex()
        private val property = """^\|\s*([A-Za-z][0-9A-Za-z_]*)\s*=\s*(.*)$""".toRegex()

        fun parse(title: String, wikiText: String?): Article? {
            if (wikiText == null) return null
            val article = Article(title)

            val reader = StringReader(wikiText)
            val lines = reader.readLines()
            var tag: Tag? = null

            for (line in lines) {
                var matcher = propertiesStart.find(line)

                if (matcher != null) {
                    val name = matcher.groups[1]?.value
                    if (name != null) {
                        log.info("获取到properties：${name}")
                        tag = Tag(name)
                        continue
                    }
                }

                if (tag != null) {
                    matcher = property.find(line)
                    if (matcher != null) {
                        val key = matcher.groups[1]?.value
                        val value = matcher.groups[2]?.value
                        tag.addProperty(key!!, value!!)
                        continue
                    }

                    if (line.startsWith("}}")) {
                        article.addTag(tag)
                        tag = null
                    }
                }
            }

            return article
        }
    }
}