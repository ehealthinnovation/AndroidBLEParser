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

class BolusProgrammedPart1EventParserTest {

    @Test
    fun parseEvent() {
          val eventInfo = EventInfo(EventType.BOLUS_PROGRAMMED_PART_1_OF_2, 1, 2)
        val expectedData = Bolus(3, BolusType.FAST, 4f, 0f, 0)
        val testData1 = StubDataReader(
                uint16(3), uint8(BolusType.FAST.key), sfloat(4f), sfloat(0f), uint16(0)
        )
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        Assert.assertEquals(expectedEvent, BolusProgrammedPart1EventParser().parseEvent(eventInfo, testData1))
    }

}