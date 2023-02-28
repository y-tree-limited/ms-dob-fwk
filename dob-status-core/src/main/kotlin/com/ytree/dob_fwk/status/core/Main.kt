package com.ytree.dob_fwk.status.core

fun main() {
    val helloWorld = HelloWorld()
    println(helloWorld())
}

class HelloWorld {
    operator fun invoke(): String = "Hello, world!"
}
