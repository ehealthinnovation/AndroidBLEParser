package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert.*
import org.junit.Test

class IntFormatTest {
    @Test
    fun testFormatLength() {
        assertEquals(1, IntFormat.FORMAT_SINT8.lengthBytes)
        assertEquals(2, IntFormat.FORMAT_SINT16.lengthBytes)
        assertEquals(4, IntFormat.FORMAT_SINT32.lengthBytes)
        assertEquals(1, IntFormat.FORMAT_UINT8.lengthBytes)
        assertEquals(2, IntFormat.FORMAT_UINT16.lengthBytes)
        assertEquals(4, IntFormat.FORMAT_UINT32.lengthBytes)
    }
}