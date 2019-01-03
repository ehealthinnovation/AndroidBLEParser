package org.ehealthinnovation.android.bluetooth.idd.historydata

import android.util.EventLog
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class BolusTemplateChangedPart2EventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.BOLUS_TEMPLATE_CHANGED_PART_2_OF_2, 1, 2)
        val testData = StubDataReader(uint8(0b01), uint16(3))
        val expected = HistoryEvent(eventInfo, BolusTemplateChangedBolusDelay(3))
        Assert.assertEquals(expected, BolusTemplateChangedPart2EventParser().parseEvent(eventInfo, testData))
    }
}