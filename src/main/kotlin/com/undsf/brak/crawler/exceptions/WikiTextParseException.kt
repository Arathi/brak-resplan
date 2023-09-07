package com.undsf.brak.crawler.exceptions

import com.undsf.brak.exceptions.BARPException

open class WikiTextParseException(message: String): BARPException(1, "WikiText解析错误 - $message") {
}