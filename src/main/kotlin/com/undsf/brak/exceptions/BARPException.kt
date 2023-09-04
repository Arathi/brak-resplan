package com.undsf.brak.exceptions

open class BARPException(
    var code: Int,
    message: String
): RuntimeException(
    message
)