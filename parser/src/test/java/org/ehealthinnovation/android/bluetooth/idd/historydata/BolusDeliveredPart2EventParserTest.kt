package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.BolusActivationType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint32
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class BolusDeliveredPart2EventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.BOLUS_DELIVERED_PART_2_OF_2, 1, 2)
        val expectedData = BolusDeliveredStatus(3, BolusActivationType.COMMANDED_BOLUS, BolusEndReason.CANCELED, 44)
        val testDataReader = StubDataReader(
                uint8(0b0111), uint32(3), uint8(BolusActivationType.COMMANDED_BOLUS.key), uint8(BolusEndReason.CANCELED.key), uint16(44)
        )
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        Assert.assertEquals(expectedEvent, BolusDeliveredPart2EventParser().parseEvent(eventInfo, testDataReader))
    }
}