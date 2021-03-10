package com.example.eqworks

import EQMobileWork.Library
import EQMobileWork.LocationEvent
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * This test runs the library and can check for post on api endpoint with
 * static location data
 */
class TestLogRequest {
    @Test
    fun checkPOSTRequest() {
        var library = Library()
        library.setup("https://httpbin.org/post") // setup
        library.log(LocationEvent(2.3f, 3.4f)) // log called
        assertEquals(library.getStatus(), 200) //passes if POST req code is 200
    }
}