package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.idd.historydata.*
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class IddHistoryDataParserTest {

    lateinit var mockParser: IddHistoryDataParser

    @Before
    fun setupMockParser() {
        mockParser = mock()
        whenever(mockParser.parse(any())).thenCallRealMethod()
    }

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B28-0000-1000-8000-00805F9B34FB", IddHistoryDataParser()::canParse)
    }

    @Test
    fun parseReferenceTimeEventIntegrationTest() {
        val mockReferenceTimeEvent = MockCharacteristicPacket.mockPacketForRead(
                uint16(EventType.REFERENCE_TIME.key), uint32(10), uint16(11),
                uint8(RecordingReason.DATE_TIME_LOSS.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint8(-1), uint8(2)
        )
        val expected = HistoryEvent(EventInfo(EventType.REFERENCE_TIME, 10, 11),
                ReferenceTimeData(
                        RecordingReason.DATE_TIME_LOSS, BluetoothDateTime(2018, 1, 2, 3, 4, 5),
                        BluetoothTimeZone(-1), DstOffset.HALF_AN_HOUR_DAYLIGHT_TIME)
        )

        Assert.assertEquals(expected, IddHistoryDataParser().parse(mockReferenceTimeEvent))
    }

    @Test
    fun parseReferenceTimeEventWhiteBoxTest() {

        val mockPacket = MockCharacteristicPacket.mockPacketForRead(uint16(EventType.REFERENCE_TIME.key), uint32(1), uint16(2))
        mockParser.parse(mockPacket)
        verify(mockParser, times(1)).readReferenceTime(any(), any())
    }


}