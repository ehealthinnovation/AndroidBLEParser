package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class TbrAdjustmentChangedEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.TBR_ADJUSTMENT_CHANGED, 1, 2)
        val expectedData = TbrAdjustmentChanged(TbrType.ABSOLUTE, 0.9f, 10, 9 , 8)
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        val testData = StubDataReader(uint8(0b01), uint8(TbrType.ABSOLUTE.key), sfloat(0.9f), uint16(10), uint16(9), uint8(8))
        Assert.assertEquals(expectedEvent, TbrAdjustmentChangedEventParser().parseEvent(eventInfo, testData))

    }
}