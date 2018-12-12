package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
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
    fun parseIntegrationTest(){
        val mockPacket = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.SNOOZE_ANNUNCIATION_RESPONSE.key), uint16((1)))
        val expeceted = SnoozeAnnunciationResponse(1)
        Assert.assertEquals(expeceted, IddCommandControlParser().parse(mockPacket) )
    }

    @Test
    fun parseWhiteBoxTest() {
        val mockParser = mock<IddCommandControlParser>()
        val mockPacketSnoozeResponse = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.SNOOZE_ANNUNCIATION_RESPONSE.key))
        whenever(mockParser.parse(any())).thenCallRealMethod()
        whenever(mockParser.readOpcode(any())).thenCallRealMethod()

        mockParser.parse(mockPacketSnoozeResponse)

        inOrder(mockParser){
            verify(mockParser,times(1)).readSnoozeAnnunciationResponse(mockPacketSnoozeResponse.readData())
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
}