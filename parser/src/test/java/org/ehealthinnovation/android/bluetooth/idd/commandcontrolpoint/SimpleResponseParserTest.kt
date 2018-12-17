package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubData
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class SimpleResponseParserTest {

    @Test
    fun readGeneralResponseTest() {
        val testData = StubDataReader(uint16(Opcode.SNOOZE_ANNUNCIATION.key), uint8(ResponseCode.INVALID_OPERAND.key))
        val expected = GeneralResponse(Opcode.SNOOZE_ANNUNCIATION, ResponseCode.INVALID_OPERAND)
        Assert.assertEquals(expected, SimpleResponseParser().readGeneralResponse(testData))
    }
}