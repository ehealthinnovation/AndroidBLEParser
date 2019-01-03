package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class TbrAdjustmentStartedParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.TBR_ADJUSTMENT_STARTED, 1, 2)
        val expectedData = TbrAdjustmentStart(TbrType.RELATIVE, 0.9f, 10, 3)
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        val testData = StubDataReader(uint8(0b01), uint8(TbrType.RELATIVE.key), sfloat(0.9f), uint16(10), uint8(3))
        Assert.assertEquals(expectedEvent, TbrAdjustmentStartedParser().parseEvent(eventInfo, testData))
    }
}