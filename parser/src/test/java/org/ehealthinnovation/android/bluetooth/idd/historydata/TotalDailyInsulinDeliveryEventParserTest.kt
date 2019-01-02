package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class TotalDailyInsulinDeliveryEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.TOTAL_DAILY_INSULIN_DELIVERY, 1, 2)
        val expectedData = TotalDailyInsulinDelivery(3f,4f, 2018, 12, 21, true)
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        val testData = StubDataReader(uint8(0b01), sfloat(3f), sfloat(4f), uint16(2018), uint8(12), uint8(21))
        Assert.assertEquals(expectedEvent, TotalDailyInsulinDeliveryEventParser().parseEvent(eventInfo, testData))
    }
}