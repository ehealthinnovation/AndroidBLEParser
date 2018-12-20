package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import net.bytebuddy.dynamic.ClassFileLocator
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
    fun readGetAvailableBolusesResponse() {
        val testDataReaderAll = StubDataReader(uint8(0b111))
        val expectedAll = GetAvailableBolusesResponse(true, true, true)
        Assert.assertEquals(expectedAll, SimpleResponseParser().readGetAvailableBolusesResponse(testDataReaderAll) )

        val testDataReaderNone = StubDataReader(uint8(0b000))
        val expectedNone = GetAvailableBolusesResponse(false, false, false)
        Assert.assertEquals(expectedNone, SimpleResponseParser().readGetAvailableBolusesResponse(testDataReaderNone) )

    }

    @Test
    fun readSetTbrTemplateResponse() {
        val testData = StubDataReader(uint8(3))
        val expected = SetTbrTemplateResponse(3)
        Assert.assertEquals(expected, SimpleResponseParser().readSetTbrTemplateResponse(testData))
    }

    @Test
    fun readSetBolusResponse() {
        val testData = StubDataReader(uint16(3))
        val expected = SetBolusResponse(3)
        Assert.assertEquals(expected, SimpleResponseParser().readSetBolusResponse(testData))
    }
    
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

    @Test
    fun readSetBolusTemplateResponse() {
        val testData = StubDataReader(uint8(3))
        val expected = SetBolusTemplateResponse(3)
        Assert.assertEquals(expected, SimpleResponseParser().readSetBolusTemplateResponse(testData))
    }

    @Test
    fun readResetTemplateStatusResponseTest() {
        val testData = StubDataReader(uint8(3), uint8(1), uint8(2), uint8(4))
        val expected = ResetTemplateStatusResponse(TemplatesOperationResults(3, listOf(1,2,4)))
        Assert.assertEquals(expected, SimpleResponseParser().readResetTemplateStatusResponse(testData))
    }


}