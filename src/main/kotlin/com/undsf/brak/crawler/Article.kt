package com.undsf.brak.crawler

import org.slf4j.LoggerFactory
import java.io.StringReader

private val log = LoggerFactory.getLogger(Article::class.java)

class Article(
    val title: String,
    val raw: String? = null,
) {
    private val nodes: MutableList<Node> = mutableListOf()

    fun addNode(node: Node) {
        nodes.add(node)
    }

    fun getPropertiesByName(name: String): List<Properties> {
        val results = mutableListOf<Properties>()
        nodes.forEach {
            if (it is Properties && it.name == name) {
                results.add(it)
            }
        }
        return results
    }

    companion object {
        private val propertiesStart = """^\{\{([A-Z][A-Za-z0-9_]+)$""".toRegex()
        private val property = """^\|\s*([A-Za-z][0-9A-Za-z_]*)\s*=\s*(.*)$""".toRegex()

        fun parse(title: String, wikiText: String?): Article? {
            if (wikiText == null) return null
            val article = Article(title, wikiText)

            val reader = StringReader(wikiText)
            val lines = reader.readLines()
            var properties: Properties? = null

            for (line in lines) {
                var matcher = propertiesStart.find(line)

                if (matcher != null) {
                    val name = matcher.groups[1]?.value
                    if (name != null) {
                        log.debug("获取到properties：${name}")
                        properties = Properties(name)
                        continue
                    }
                }

                if (properties != null) {
                    matcher = property.find(line)
                    if (matcher != null) {
                        val key = matcher.groups[1]?.value
                        val value = matcher.groups[2]?.value
                        properties.addProperty(key!!, value!!)
                        continue
                    }

                    if (line.startsWith("}}")) {
                        article.addNode(properties)
                        properties = null
                    }
                }
            }

            return article
        }
    }
}