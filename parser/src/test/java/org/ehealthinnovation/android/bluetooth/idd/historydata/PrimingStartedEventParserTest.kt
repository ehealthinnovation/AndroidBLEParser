package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

class PrimingStartedEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.PRIMING_STARTED, 1, 2)
        val testData = StubDataReader(sfloat(4f))
        val expected = HistoryEvent(eventInfo, PrimingStarted(4f))
        Assert.assertEquals(expected, PrimingStartedEventParser().parseEvent(eventInfo, testData))
    }
}