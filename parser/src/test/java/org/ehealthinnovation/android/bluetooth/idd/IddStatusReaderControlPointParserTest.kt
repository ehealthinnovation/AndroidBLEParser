package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class IddStatusReaderControlPointParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B24-0000-1000-8000-00805F9B34FB", IddStatusReaderControlPointParser()::canParse)
    }

    @Test
    fun parse() {
        val testPacket = MockCharacteristicPacket.mockPacketForRead(
                uint16(StatusReaderControlOpcode.RESPONSE_CODE.key),
                uint16(StatusReaderControlOpcode.RESET_STATUS.key),
                uint8(StatusReaderControlResponseCode.INVALID_OPERAND.key)
        )
        val expectedOutput = StatusReaderControlGeneralResponse(StatusReaderControlOpcode.RESET_STATUS, StatusReaderControlResponseCode.INVALID_OPERAND)
        Assert.assertEquals(expectedOutput, IddStatusReaderControlPointParser().parse(testPacket))
    }

    @Test
    fun readGeneralResponse() {
        val testReader = StubDataReader(
                uint16(StatusReaderControlOpcode.RESET_STATUS.key),
                uint8(StatusReaderControlResponseCode.SUCCESS.key)
        )
        val expectedOutputResponse = StatusReaderControlGeneralResponse(StatusReaderControlOpcode.RESET_STATUS, StatusReaderControlResponseCode.SUCCESS)
        val actualOutput = IddStatusReaderControlPointParser().readGeneralResponse(testReader)
        Assert.assertEquals(actualOutput, expectedOutputResponse)
    }
}