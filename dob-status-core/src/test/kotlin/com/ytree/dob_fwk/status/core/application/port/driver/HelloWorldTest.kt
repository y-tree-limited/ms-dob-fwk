package com.ytree.dob_fwk.status.core.application.port.driver

import com.ytree.dob_fwk.status.core.HelloWorld
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

class HelloWorldTest {

    private val sut: HelloWorld = HelloWorld()

    @Test
    fun `can greet with hello world`() {
        assertThat(sut(), equalTo("Hello, world!"))
    }
}
