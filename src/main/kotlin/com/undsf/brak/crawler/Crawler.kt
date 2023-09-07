package com.undsf.brak.crawler

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.undsf.brak.crawler.exceptions.WikiTextParseException
import com.undsf.brak.crawler.tags.Character
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Proxy
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory

private val log = LoggerFactory.getLogger(Crawler::class.java)
private val UTF_8 = StandardCharsets.UTF_8

open class Crawler(
    val proxyType: Proxy.Type = Proxy.Type.HTTP,
    val proxyHost: String? = "127.0.0.1",
    val proxyPort: Int? = 8118,
    val useCache: Boolean = false,
    var cachePath: String = ""
) {
    private var httpClient: OkHttpClient
    private var mapper: ObjectMapper = jacksonObjectMapper()

    init {
        httpClient = buildHttpClient()
        if (useCache && cachePath.isEmpty()) {
            cachePath = System.getProperty("user.dir") + "/cache"
        }
    }

    private fun buildHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (proxyType != Proxy.Type.DIRECT && proxyHost != null && proxyPort != null) {
            val proxy = Proxy(
                proxyType,
                InetSocketAddress(proxyHost, proxyPort)
            )
            log.info("使用代理服务器：${proxyType.name} ${proxyHost}:${proxyPort}")
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
        var wikiText: String? = null

        if (useCache) {
            val path = Paths.get(cachePath, "/${page}.wtx")
            try {
                if (Files.exists(path)) {
                    wikiText = Files.readString(path, UTF_8)
                }
            }
            catch (ex: Exception) {
                log.warn("缓存文件加载失败：", ex)
            }

            if (!wikiText.isNullOrEmpty()) {
                log.info("从缓存成功加载页面`${page}`的wikitext，长度：${wikiText.length}")
                return wikiText
            }
        }

        val wikiResp = sendGetRequest(mapOf(
                "action" to "parse",
                "prop" to "wikitext",
                "format" to "json",
                "page" to page
        ))
        wikiText = wikiResp.parse?.wikitext?.get("*")
        if (wikiText != null) {
            if (useCache) {
                val path = Paths.get(cachePath, "/${page}.wtx")
                path.parent.createDirectories()
                Files.writeString(path, wikiText, UTF_8)
            }
        }
        else {
            log.warn("从${SiteName}加载wikitext失败")
        }
        return wikiText
    }

    @Throws(RuntimeException::class)
    fun getPagedCategoryMembers(category: String, cmContinue: String? = null): Pair<List<String>, String?> {
        val params = mutableMapOf<String, String?>(
            "action" to "query",
            "list" to "categorymembers",
            "format" to "json",
            "cmlimit" to "500",
            "cmtype" to "page",
            "cmtitle" to "Category:${category}"
        )
        if (cmContinue != null) {
            params["cmcontinue"] = cmContinue
        }

        val wikiResp = sendGetRequest(params)
        val nextCmContinue = wikiResp.continueInfo?.categoryMembersContinue
        val members = wikiResp.query?.categoryMembers
        val results = mutableListOf<String>()
        if (members != null) {
            results.addAll(members.map { it.title })
        }
        return Pair(results, nextCmContinue)
    }

    fun getCategoryMembers(category: String): List<String> {
        var cmContinue: String? = null
        val results = mutableListOf<String>()
        do {
            val pagedResults = getPagedCategoryMembers(category, cmContinue)
            results.addAll(pagedResults.first)
            cmContinue = pagedResults.second
        }
        while (cmContinue != null)
        return results
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
        try {
            val results = article.getPropertiesByName("Character")
            if (index in results.indices) {
                val properties = results[index]
                return Character(
                    Id = properties.getPropertyAsInt("Id"),
                    Name = properties.getProperty("Name"),
                    JPName = properties.getProperty("JPName"),
                    JPReading = properties.getProperty("JPReading"),
                    School = properties.getProperty("School"),
                    Club = properties.getProperty("Club"),
                    Age = properties.getProperty("Age"),
                    Birthday = properties.getProperty("Birthday"),
                    Height = properties.getProperty("Height"),
                    Hobbies = properties.getProperty("Hobbies"),
                    WeaponType = properties.getProperty("WeaponType"),
                    UsesCover = properties.getProperty("UsesCover"),
                    Rarity = properties.getPropertyAsInt("Rarity"),
                    CombatClass = properties.getProperty("CombatClass"),
                    ArmorType = properties.getProperty("ArmorType"),
                    AttackType = properties.getProperty("AttackType"),
                    Position = properties.getProperty("Position"),
                    Role = properties.getProperty("Role"),
                    CityTownAffinity = properties.getProperty("CityTownAffinity"),
                    OutdoorAffinity = properties.getProperty("OutdoorAffinity"),
                    IndoorAffinity = properties.getProperty("IndoorAffinity"),
                    EquipmentSlot1 = properties.getProperty("EquipmentSlot1"),
                    EquipmentSlot2 = properties.getProperty("EquipmentSlot2"),
                    EquipmentSlot3 = properties.getProperty("EquipmentSlot3"),
                    CharacterPool = properties.getProperty("CharacterPool"),
                    ReleaseDate = properties.getProperty("ReleaseDate"),
                )
            }
        }
        catch (ex: WikiTextParseException) {
            log.warn("WikiText解析出错，页面：${article.title}，WikiText如下：${article.raw}")
        }
        return null
    }

    companion object {
        const val SiteName = "bluearchive.wiki"
    }
}