package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.Bolus
import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class BolusDeliveredPart1EventParserTest {

    @Test
    fun parseEvent() {
          val eventInfo = EventInfo(EventType.BOLUS_DELIVERED_PART_1_OF_2, 1, 2)
        val expectedData = Bolus(3, BolusType.EXTENDED, 0f, 4f, 5)
        val testData1 = StubDataReader(
                uint16(3), uint8(BolusType.EXTENDED.key), sfloat(0f), sfloat(4f), uint16(5)
        )
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        Assert.assertEquals(expectedEvent, BolusProgrammedPart1EventParser().parseEvent(eventInfo, testData1))
    }

}