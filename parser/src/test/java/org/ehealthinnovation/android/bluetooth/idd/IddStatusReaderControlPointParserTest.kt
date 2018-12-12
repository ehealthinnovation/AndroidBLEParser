package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class IddStatusReaderControlPointParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B24-0000-1000-8000-00805F9B34FB", IddStatusReaderControlPointParser()::canParse)
    }

    @Test
    fun parseIntegrationTest() {
        val testPacket = MockCharacteristicPacket.mockPacketForRead(
                uint16(StatusReaderControlOpcode.RESPONSE_CODE.key),
                uint16(StatusReaderControlOpcode.RESET_STATUS.key),
                uint8(StatusReaderControlResponseCode.INVALID_OPERAND.key)
        )
        val expectedOutput = StatusReaderControlGeneralResponse(StatusReaderControlOpcode.RESET_STATUS, StatusReaderControlResponseCode.INVALID_OPERAND)
        Assert.assertEquals(expectedOutput, IddStatusReaderControlPointParser().parse(testPacket))
    }

    @Test
    fun parseWriteBoxTest(){
        val mockParser = mock<IddStatusReaderControlPointParser>()
        val mockPacketGeneralResponse = MockCharacteristicPacket.mockPacketForRead(uint16(StatusReaderControlOpcode.RESPONSE_CODE.key))
        whenever(mockParser.parse(any())).thenCallRealMethod()

        mockParser.parse(mockPacketGeneralResponse)

        val mockPacketGetActiveBolusIds = MockCharacteristicPacket.mockPacketForRead(uint16(StatusReaderControlOpcode.GET_ACTIVE_BOLUS_IDS_RESPONSE.key))
        mockParser.parse(mockPacketGetActiveBolusIds)

        val mockPacketGetInsulinDeliveredResponse = MockCharacteristicPacket.mockPacketForRead(uint16(StatusReaderControlOpcode.GET_DELIVERED_INSULIN_RESPONSE.key))
        mockParser.parse(mockPacketGetInsulinDeliveredResponse)

        val mockGetActiveBasalResponse = MockCharacteristicPacket.mockPacketForRead(uint16(StatusReaderControlOpcode.GET_ACTIVE_BASAL_RATE_DELIVERY_RESPONSE.key))
        mockParser.parse(mockGetActiveBasalResponse)

        val mockPacketGetTotalDailyInsulinStatus = MockCharacteristicPacket.mockPacketForRead(uint16(StatusReaderControlOpcode.GET_TOTAL_DAILY_INSULIN_STATUS_RESPONSE.key))
        mockParser.parse(mockPacketGetTotalDailyInsulinStatus)

        inOrder(mockParser) {
            verify(mockParser, times(1)).readGeneralResponse(mockPacketGeneralResponse.readData())
            verify(mockParser, times(1)).readActiveBolusIdsResponse(mockPacketGetActiveBolusIds.readData())
            verify(mockParser, times(1)).readGetInsulinDeliveredResponse(mockPacketGetInsulinDeliveredResponse.readData())
            verify(mockParser, times(1)).readGetActiveBasalDeliveryRespoonse(mockGetActiveBasalResponse.readData())
            verify(mockParser, times(1)).readGetTotalDailyInsuinStatusResponse(mockPacketGetTotalDailyInsulinStatus.readData())
        }
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

    @Test
    fun readActiveBolusIdsResponse() {
        val testReader = StubDataReader(
                uint8(4),
                uint16(1), uint16(2), uint16(3), uint16(4)
        )
        val expectedOutput = ActiveBolusIds(arrayListOf(1,2,3,4))
        val actualOUtput = IddStatusReaderControlPointParser().readActiveBolusIdsResponse(testReader)
        Assert.assertEquals(expectedOutput, actualOUtput)
    }
}