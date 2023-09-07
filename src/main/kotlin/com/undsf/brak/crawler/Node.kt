package com.undsf.brak.crawler

abstract class Node(val type: Type) {
    enum class Type {
        Properties,
        Heading
    }
}