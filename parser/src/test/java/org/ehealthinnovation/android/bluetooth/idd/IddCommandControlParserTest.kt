package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.ConfirmAnnunciationResponse
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationResponse
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class IddCommandControlParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B25-0000-1000-8000-00805F9B34FB", IddCommandControlParser()::canParse)
    }

    @Test
    fun parseIntegrationTest() {
        val mockPacket = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.SNOOZE_ANNUNCIATION_RESPONSE.key), uint16((1)))
        val expeceted = SnoozeAnnunciationResponse(1)
        Assert.assertEquals(expeceted, IddCommandControlParser().parse(mockPacket))
    }

    @Test
    fun parseWhiteBoxTest() {
        val mockParser = mock<IddCommandControlParser>()
        whenever(mockParser.parse(any())).thenCallRealMethod()
        whenever(mockParser.readOpcode(any())).thenCallRealMethod()

        val mockPacketSnoozeResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.SNOOZE_ANNUNCIATION_RESPONSE.key))
        mockParser.parse(mockPacketSnoozeResponse)

        val mockPacketWriteBasalRateProfileTemplateResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.WRITE_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE.key))
        mockParser.parse(mockPacketWriteBasalRateProfileTemplateResponse)

        val mockPacketConfirmResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.CONFIRM_ANNUNCIATION_RESPONSE.key))
        mockParser.parse(mockPacketConfirmResponse)

        val mockPacketGeneralResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.RESPONSE_CODE.key))
        mockParser.parse(mockPacketGeneralResponse)

        val mockPacketSetTbrTemplateResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.SET_TBR_TEMPLATE_RESPONSE.key))
        mockParser.parse(mockPacketSetTbrTemplateResponse)

        val mockPacketSetBolusResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.SET_BOLUS_RESPONSE.key))
        mockParser.parse(mockPacketSetBolusResponse)

        val mockPacketGetTbrTemplateResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.GET_TBR_TEMPLATE_RESPONSE.key))
        mockParser.parse(mockPacketGetTbrTemplateResponse)


        val mockPacketCancelBolusResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.CANCEL_BOLUS_RESPONSE.key))
        mockParser.parse(mockPacketCancelBolusResponse)

        val mockPacketGetAvailableBolusesResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.GET_AVAILABLE_BOLUSES_RESPONSE.key))
        mockParser.parse(mockPacketGetAvailableBolusesResponse)

        val mockPacketGetBolusTemplateResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.GET_BOLUS_TEMPLATE_RESPONSE.key))
        mockParser.parse(mockPacketGetBolusTemplateResponse)

        inOrder(mockParser){
            verify(mockParser,times(1)).readSnoozeAnnunciationResponse(mockPacketSnoozeResponse.readData())
            verify(mockParser, times(1)).readWriteBasalProfileTemplateResponse(mockPacketWriteBasalRateProfileTemplateResponse.readData())
            verify(mockParser,times(1)).readConfirmAnnunciationResponse(mockPacketConfirmResponse.readData())
            verify(mockParser, times(1)).readGeneralResponse(mockPacketGeneralResponse.readData())
            verify(mockParser, times(1)).readSetTbrTemplateResponse(mockPacketSetTbrTemplateResponse.readData())
            verify(mockParser, times(1)).readSetBolusResponse(mockPacketSetBolusResponse.readData())
            verify(mockParser, times(1)).readTbrTemplateResponse(mockPacketGetTbrTemplateResponse.readData())
            verify(mockParser, times(1)).readCancelBolusResponse(mockPacketCancelBolusResponse.readData())
            verify(mockParser, times(1)).readAvailableBolusesResponse(mockPacketGetAvailableBolusesResponse.readData())
            verify(mockParser, times(1)).readGetBolusTemplateResponse(mockPacketGetBolusTemplateResponse.readData())
        }
    }

    @Test
    fun readOpcode() {
        val testData = StubDataReader(uint16(Opcode.SNOOZE_ANNUNCIATION.key))
        val expectedOpcode = Opcode.SNOOZE_ANNUNCIATION
        Assert.assertEquals(expectedOpcode, IddCommandControlParser().readOpcode(testData))
    }

    @Test
    fun readSnoozeAnnunciationResponse() {
        val testData = StubDataReader(uint16(1))
        val expected = SnoozeAnnunciationResponse(1)
        Assert.assertEquals(expected, IddCommandControlParser().readSnoozeAnnunciationResponse(testData))
    }

    @Test
    fun readConfirmAnnunciationResponse() {
        val testData = StubDataReader(uint16(1))
        val expected = ConfirmAnnunciationResponse(1)
        Assert.assertEquals(expected, IddCommandControlParser().readConfirmAnnunciationResponse(testData))
    }
}