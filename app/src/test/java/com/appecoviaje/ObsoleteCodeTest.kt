package com.appecoviaje

import org.junit.Test
import org.junit.Assert.fail

class ObsoleteCodeTest {

    @Test
    fun `test obsolete MainActivity is removed`() {
        try {
            Class.forName("com.ecoviaje.MainActivity")
            fail("Obsolete MainActivity class should not exist")
        } catch (e: ClassNotFoundException) {
            // Expected exception
        }
    }
}
