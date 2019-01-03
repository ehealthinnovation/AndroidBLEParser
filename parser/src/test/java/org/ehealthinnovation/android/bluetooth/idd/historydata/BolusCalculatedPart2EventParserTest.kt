package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

class BolusCalculatedPart2EventParserTest {

    @Test
    fun parseEvent() {
        val testData = StubDataReader(
                sfloat(3f), sfloat(4f), sfloat(5f), sfloat(6f)
        )
        val testEvent = EventInfo(EventType.BOLUS_CALCULATED_PART_2_OF_2, 1, 2)
        val expected = HistoryEvent(EventInfo(EventType.BOLUS_CALCULATED_PART_2_OF_2, 1, 2),
                BolusCalculatedData(3f, 4f, 5f, 6f))
        Assert.assertEquals(expected, BolusCalculatedEventParser().parseEvent(testEvent, testData))
    }
}