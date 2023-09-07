package com.undsf.brak.crawler

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.net.Proxy

private val log = LoggerFactory.getLogger(CrawlerTests::class.java)

class CrawlerTests {
    private val crawler = Crawler(
        proxyType = Proxy.Type.SOCKS,
        proxyHost ="127.0.0.1",
        proxyPort = 41080,
        useCache = true,
        cachePath = "D:\\Temp\\brak-cache"
    )

    @Test
    fun testGetCategoryMembers() {
        val characters = crawler.getCategoryMembers("Characters")
        log.info("获取 Character ${characters.size} 个")

        val missions = crawler.getCategoryMembers("Missions")
        log.info("获取 Mission ${missions.size} 个")
    }
}