package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class PrimingDoneEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.PRIMING_DONE, 1, 2)
        val testData = StubDataReader(uint8(0b01), sfloat(3f), uint8(TerminationReason.ABORTED_BY_USER.key), uint16(4))
        val expected = HistoryEvent(eventInfo, PrimingDone(3f, TerminationReason.ABORTED_BY_USER, 4))
        Assert.assertEquals(expected, PrimingDoneEventParser().parseEvent(eventInfo, testData))
    }
}