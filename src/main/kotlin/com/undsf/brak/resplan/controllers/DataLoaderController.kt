package com.undsf.brak.resplan.controllers

import com.undsf.brak.crawler.Crawler
import com.undsf.brak.exceptions.DataLoaderException
import com.undsf.brak.resplan.messages.BasicResponse
import com.undsf.brak.resplan.messages.DataResponse
import com.undsf.brak.resplan.services.DataLoaderService
import jakarta.websocket.RemoteEndpoint.Basic
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val log = LoggerFactory.getLogger(DataLoaderController::class.java)

@RestController
@RequestMapping("/api/data-loader")
class DataLoaderController {
    @Autowired
    lateinit var dataLoaderSvc: DataLoaderService

    @PostMapping("/character")
    fun loadCharacter(@RequestParam("name") name: String): DataResponse<*> {
        try {
            val student = dataLoaderSvc.loadStudentMetadata(name)
            return DataResponse(data = student)
        }
        catch (ex: DataLoaderException) {
            log.warn("获取角色（${name}）时出现异常：${ex}")
            return BasicResponse(ex.code)
        }
    }
}