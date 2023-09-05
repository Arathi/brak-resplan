package com.undsf.brak.crawler

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.undsf.brak.crawler.tags.Character
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.net.Proxy

private val log = LoggerFactory.getLogger(Crawler::class.java)

class Crawler(
    val proxyHost: String? = null,
    val proxyPort: Int? = null,
    val useCache: Boolean = false,
    var cachePath: String = ""
) {
    var httpClient: OkHttpClient = OkHttpClient()
    var mapper: ObjectMapper = jacksonObjectMapper()

    init {
        httpClient = buildHttpClient()
        if (useCache && cachePath.isEmpty()) {
            cachePath = System.getProperty("user.dir") + "/cache"
        }
    }

    fun buildHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (proxyHost != null && proxyPort != null) {
            val proxy = Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress(proxyHost, proxyPort)
            )
            builder.proxy(proxy)
        }
        return builder.build()
    }

    @Throws(RuntimeException::class)
    private fun sendGetRequest(params: Map<String, String?>): WikiResponse {
        val builder = HttpUrl.Builder()

        builder.scheme("https")
                .host("bluearchive.wiki")
                .addPathSegments("w/api.php")

        params.forEach {
            builder.addQueryParameter(it.key, it.value)
        }

        val url = builder.build()

        val request = Request.Builder()
                .get()
                .url(url)
                .build()

        val response = httpClient.newCall(request).execute()
        if (response.code != 200) {
            log.warn("发起请求${url}，状态码：${response.code}")
            throw RuntimeException("调用MediaWiki API时出现网络异常！url=${url}，状态码=${response.code}")
        }

        val respBody = response.body?.string()
        if (respBody == null) {
            log.warn("发起请求${url}，获取响应报文为空")
            throw RuntimeException("调用MediaWiki API，响应报文为空！url=${url}")
        }

        try {
            val wikiResp: WikiResponse = mapper.readValue(respBody)
            log.debug("成功获取响应报文如下：{}", wikiResp)
            return wikiResp
        }
        catch (ex: JacksonException) {
            log.warn("解析响应报文时出现异常：${respBody}")
            throw RuntimeException("解析MediaWiki API的响应报文时出现异常！")
        }
    }

    @Throws(RuntimeException::class)
    fun getWikiText(page: String): String? {
        val wikiResp = sendGetRequest(mapOf(
                "action" to "parse",
                "prop" to "wikitext",
                "format" to "json",
                "page" to page
        ))
        val wikiText = wikiResp.parse?.wikitext?.get("*")
        if (wikiText == null) {
            log.warn("${page}的wikitext获取失败")
        }
        return wikiText
    }

    @Throws(RuntimeException::class)
    fun getCategoryMembers(category: String): List<String> {
        val wikiResp = sendGetRequest(mapOf(
                "action" to "query",
                "list" to "categorymembers",
                "format" to "json",
                "cmlimit" to "500",
                "cmtype" to "page",
                "cmtitle" to "Category:${category}"
        ))
        val members = wikiResp.query?.categorymembers
        val pages = mutableListOf<String>()
        if (members != null) {
            for (member in members) {
                if (member.title != null) {
                    pages.add(member.title!!)
                }
            }
        }
        return pages
    }

    fun getArticle(page: String): Article? {
        val wikiText = getWikiText(page)
        if (wikiText == null) {
            log.warn("获取到页面${page}的源码为空，无法解析为Article")
            return null
        }
        return Article.parse(page, wikiText)
    }

    fun getCharacter(article: Article, index: Int = 0): Character? {
        val results = article.getTagsByName("Character")
        if (index in results.indices) {
            val tag = results[index]
            return Character(
                Id = tag.getPropertyAsInt("Id"),
                Name = tag.getProperty("Name"),
                JPName = tag.getProperty("JPName"),
                JPReading = tag.getProperty("JPReading"),
                School = tag.getProperty("School"),
                Club = tag.getProperty("Club"),
                Age = tag.getPropertyAsInt("Age"),
                Birthday = tag.getProperty("Birthday"),
                Height = tag.getProperty("Height"),
                Hobbies = tag.getProperty("Hobbies"),
                WeaponType = tag.getProperty("WeaponType"),
                UsesCover = tag.getProperty("UsesCover"),
                Rarity = tag.getPropertyAsInt("Rarity"),
                CombatClass = tag.getProperty("CombatClass"),
                ArmorType = tag.getProperty("ArmorType"),
                AttackType = tag.getProperty("AttackType"),
                Position = tag.getProperty("Position"),
                Role = tag.getProperty("Role"),
                CityTownAffinity = tag.getProperty("CityTownAffinity"),
                OutdoorAffinity = tag.getProperty("OutdoorAffinity"),
                IndoorAffinity = tag.getProperty("IndoorAffinity"),
                EquipmentSlot1 = tag.getProperty("EquipmentSlot1"),
                EquipmentSlot2 = tag.getProperty("EquipmentSlot2"),
                EquipmentSlot3 = tag.getProperty("EquipmentSlot3"),
                CharacterPool = tag.getProperty("CharacterPool"),
                ReleaseDate = tag.getProperty("ReleaseDate"),
            )
        }
        return null
    }
}