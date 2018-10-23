package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test

class FloatFormatTest {
    @Test
    fun testFormatLength() {
        Assert.assertEquals(2, FloatFormat.FORMAT_SFLOAT.lengthBytes)
        Assert.assertEquals(4, FloatFormat.FORMAT_FLOAT.lengthBytes)
    }
}