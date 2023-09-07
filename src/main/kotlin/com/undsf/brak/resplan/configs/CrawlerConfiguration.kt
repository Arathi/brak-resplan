package com.undsf.brak.resplan.configs

import com.undsf.brak.crawler.Crawler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.Proxy

@Configuration
class CrawlerConfiguration {
    @Value("\${brak-resplan.crawler.proxy.type:HTTP}")
    private lateinit var proxyTypeName: String

    @Value("\${brak-resplan.crawler.proxy.host:127.0.0.1}")
    private lateinit var proxyHost: String

    @Value("\${brak-resplan.crawler.proxy.port:8118}")
    private var proxyPort: Int = 1080

    @Value("\${brak-resplan.crawler.cache.enabled:false}")
    private var useCache: Boolean = false

    @Value("\${brak-resplan.crawler.cache.path:}")
    private lateinit var cachePath: String

    @Bean
    fun buildCrawler(): Crawler {
        return Crawler(
            Proxy.Type.valueOf(proxyTypeName),
            proxyHost,
            proxyPort,
            useCache,
            cachePath,
        )
    }
}