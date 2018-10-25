package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test

class ParsingToolsDateTimeTest {
    
    @Test
    fun testFormatRead() {
        val dateComponentFormats = arrayOf(
                IntFormat.FORMAT_UINT16,
                IntFormat.FORMAT_UINT8,
                IntFormat.FORMAT_UINT8,
                IntFormat.FORMAT_UINT8,
                IntFormat.FORMAT_UINT8,
                IntFormat.FORMAT_UINT8
        )
        readDateTime(StubFormatReader(*dateComponentFormats))
    }

    @Test
    fun testFormatReadFull() {
        val dateTime = readDateTime(StubValueReader(1976, 11, 24, 17, 13, 14))
        Assert.assertEquals(BluetoothDateTime(1976, 11, 24, 17, 13, 14), dateTime)
    }

    @Test
    fun testFormatNoYear() {
        val dateTime = readDateTime(StubValueReader(0, 11, 24, 17, 13, 14))
        Assert.assertEquals(BluetoothDateTime(null, 11, 24, 17, 13, 14), dateTime)
    }

    @Test
    fun testFormatNoMonth() {
        val dateTime = readDateTime(StubValueReader(1976, 0, 24, 17, 13, 14))
        Assert.assertEquals(BluetoothDateTime(1976, null, 24, 17, 13, 14), dateTime)
    }

    @Test
    fun testFormatNoDay() {
        val dateTime = readDateTime(StubValueReader(1976, 11, 0, 17, 13, 14))
        Assert.assertEquals(BluetoothDateTime(1976, 11, null, 17, 13, 14), dateTime)
    }
}