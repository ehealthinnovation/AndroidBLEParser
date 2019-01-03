package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class SingleValueProfileTemplateTimeBlockChangedEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.BASAL_RATE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED, 1, 2)
        val expectedData = SingleValueTimeBlockChangeData(3, 4, 5, 5.1f)
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        val testData = StubDataReader(uint8(3), uint8(4), uint16(5), sfloat(5.1f))
        Assert.assertEquals(expectedEvent, SingleValueProfileTemplateTimeBlockChangedEventParser().parseEvent(eventInfo, testData))
    }
}