package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TherapyControlState
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class TherapyControlStateChangedEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.THERAPY_CONTROL_STATE_CHANGED, 1, 2)
        val testData = StubDataReader(uint8(TherapyControlState.PAUSE.key), uint8(TherapyControlState.RUN.key))
        val expected = HistoryEvent(eventInfo, TherapyControlStateChangedData(TherapyControlState.PAUSE, TherapyControlState.RUN))
        Assert.assertEquals(expected, (TherapyControlStateChangedEventParser().parseEvent(eventInfo, testData)))
    }
}