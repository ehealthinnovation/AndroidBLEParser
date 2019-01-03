package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class PairedValueProfileTemplateTimeBlockChangedEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED, 1,2)
        val testData = StubDataReader(uint8(3), uint8(4), uint16(5), sfloat(6f), sfloat(7f))
        val expected = HistoryEvent(eventInfo, PairedValueTimeBlockChangeData(3,4,5,6f,7f))
        Assert.assertEquals(expected, PairedValueProfileTemplateTimeBlockChangedEventParser().parseEvent(eventInfo, testData))
    }
}