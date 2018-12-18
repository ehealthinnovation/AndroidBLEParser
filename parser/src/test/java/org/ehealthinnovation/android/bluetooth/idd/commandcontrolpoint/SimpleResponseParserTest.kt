package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class SimpleResponseParserTest {

    @Test
    fun readGeneralResponseTest() {
        val testData = StubDataReader(uint16(Opcode.SNOOZE_ANNUNCIATION.key), uint8(ResponseCode.INVALID_OPERAND.key))
        val expected = GeneralResponse(Opcode.SNOOZE_ANNUNCIATION, ResponseCode.INVALID_OPERAND)
        Assert.assertEquals(expected, SimpleResponseParser().readGeneralResponse(testData))
    }

    @Test
    fun readSetTbrTemplateResponse() {
        val testData = StubDataReader(uint8(3))
        val expected = SetTbrTemplateResponse(3)
        Assert.assertEquals(expected, SimpleResponseParser().readSetTbrTemplateResponse(testData))

    @Test
    fun readSetBolusResponse() {
        val testData = StubDataReader(uint16(3))
        val expected = SetBolusResponse(3)
        Assert.assertEquals(expected, SimpleResponseParser().readSetBolusResponse(testData))
    
    @Test
    fun readGetTbrTemplateResponseTest() {
        val testData = StubDataReader(
                uint8(1),
                uint8(TbrType.RELATIVE.key),
                sfloat(0.9f),
                uint16(2)
        )
        val expected = GetTbrTemplateResponse(1, TbrType.RELATIVE, 0.9f, 2)
        Assert.assertEquals(expected, SimpleResponseParser().readGetTbrTemplateResponse(testData))
    }

    @Test
    fun readCancelBolusResponseTest() {
        val testData = StubDataReader(
                uint16(1)
        )
        val expected = CancelBolusResponse(1)
        Assert.assertEquals(expected, SimpleResponseParser().readCancelBolusResponse(testData))
    }
}