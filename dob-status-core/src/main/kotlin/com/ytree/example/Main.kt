package com.ytree.example

fun main() {
    val helloWorld = HelloWorld()
    println(helloWorld())
}

class HelloWorld {
    operator fun invoke(): String = "Hello, world!"
}
