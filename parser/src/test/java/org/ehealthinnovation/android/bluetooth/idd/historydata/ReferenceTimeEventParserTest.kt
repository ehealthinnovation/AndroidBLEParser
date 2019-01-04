package org.ehealthinnovation.android.bluetooth.idd.historydata

import com.nhaarman.mockito_kotlin.mock
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test
import kotlin.math.exp

class ReferenceTimeEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = mock<EventInfo>()

        val testData = StubDataReader(uint8(RecordingReason.DATE_TIME_LOSS.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint8(-1), uint8(2)
        )
        val expected = HistoryEvent(eventInfo, ReferenceTimeData(
                RecordingReason.DATE_TIME_LOSS, BluetoothDateTime(2018, 1, 2, 3, 4, 5),
                BluetoothTimeZone(-1), DstOffset.HALF_AN_HOUR_DAYLIGHT_TIME))

        Assert.assertEquals(expected, ReferenceTimeEventParser().parseEvent(eventInfo, testData))


    }

    @Test
    fun readEventDataTest() {
        val testData = StubDataReader(uint8(RecordingReason.DATE_TIME_LOSS.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint8(-1), uint8(2)
        )
        val expected = ReferenceTimeData(RecordingReason.DATE_TIME_LOSS, BluetoothDateTime(2018, 1, 2, 3, 4, 5),
                BluetoothTimeZone(-1), DstOffset.HALF_AN_HOUR_DAYLIGHT_TIME)
        Assert.assertEquals(expected, ReferenceTimeEventParser().readData(testData))
    }
}