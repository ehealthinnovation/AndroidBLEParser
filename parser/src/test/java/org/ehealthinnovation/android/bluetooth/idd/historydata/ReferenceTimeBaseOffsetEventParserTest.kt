package org.ehealthinnovation.android.bluetooth.idd.historydata

import com.nhaarman.mockito_kotlin.mock
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Test
import org.junit.Assert

class ReferenceTimeBaseOffsetEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = mock<EventInfo>()

        val testData = StubDataReader(uint8(RecordingReason.PERIODIC_RECORDING.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint16(3)
        )
        val expected = HistoryEvent(eventInfo, ReferenceTimeBaseOffset(
                RecordingReason.PERIODIC_RECORDING, BluetoothDateTime(2018, 1, 2, 3, 4, 5),
                3))

        Assert.assertEquals(expected, ReferenceTimeBaseOffsetEventParser().parseEvent(eventInfo, testData))


    }

    @Test
    fun readEventDataTest() {
        val testData = StubDataReader(uint8(RecordingReason.DATE_TIME_LOSS.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint16(-1)
        )
        val expected = ReferenceTimeBaseOffset(RecordingReason.DATE_TIME_LOSS, BluetoothDateTime(2018, 1, 2, 3, 4, 5),
                -1)
        Assert.assertEquals(expected, ReferenceTimeBaseOffsetEventParser().readData(testData))
    }
}