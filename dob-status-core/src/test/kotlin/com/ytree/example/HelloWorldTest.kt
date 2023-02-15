package com.ytree.example

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HelloWorldTest {

    private val sut: HelloWorld = HelloWorld()

    @Test
    fun `can greet with hello world`() {
        Assertions.assertEquals("Hello, world!", sut())
    }
}
