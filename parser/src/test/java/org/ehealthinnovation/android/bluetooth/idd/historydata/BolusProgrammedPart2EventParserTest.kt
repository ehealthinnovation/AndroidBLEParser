package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.BolusActivationType
import org.ehealthinnovation.android.bluetooth.parser.StubData
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class BolusProgrammedPart2EventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.BOLUS_PROGRAMMED_PART_2_OF_2, 1, 2)
        val expectedData = BolusProgrammedConfiguration(true, false, 3, null, BolusActivationType.MANUAL_BOLUS)
        val testData1 = StubDataReader(
                uint8(0b10101), uint16(3), uint8(BolusActivationType.MANUAL_BOLUS.key)
        )
        val expectedEvent = HistoryEvent(eventInfo, expectedData)
        Assert.assertEquals(expectedEvent, BolusProgrammedPart2EventParser().parseEvent(eventInfo, testData1))
    }
}