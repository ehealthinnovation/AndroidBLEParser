package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class TbrAdjustmentEndedEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.TBR_ADJUSTMENT_ENDED, 1, 2)
        val expectedData = TbrAdjustmentEnded(TbrType.ABSOLUTE, 4, TbrEndReason.PROGRAMMED_DURATION_OVER, 5, 6)
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        val testData = StubDataReader(uint8(0b11), uint8(TbrType.ABSOLUTE.key), uint16(4), uint8(TbrEndReason.PROGRAMMED_DURATION_OVER.key), uint8(5), uint16(6))
        Assert.assertEquals(expectedEvent, TbrAdjustmentEndedEventParser().parseEvent(eventInfo,testData))
    }
}