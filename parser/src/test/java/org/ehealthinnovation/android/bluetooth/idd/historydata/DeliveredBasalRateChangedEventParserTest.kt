package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.BasalDeliveryContext
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class DeliveredBasalRateChangedEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.DELIVERED_BASAL_RATE_CHANGED, 1, 2)
        val expectedData = BasalRateChanged(1.2f, 2.3f, BasalDeliveryContext.DEVICE_BASED)
        val testData = StubDataReader(
                uint8(0b01), sfloat(1.2f), sfloat(2.3f), uint8(BasalDeliveryContext.DEVICE_BASED.key)
        )
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        Assert.assertEquals(expectedEvent, DeliveredBasalRateChangedEventParser().parseEvent(eventInfo, testData))

    }
}