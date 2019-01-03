package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Bolus
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class BolusTemplateChangedPart1EventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.BOLUS_TEMPLATE_CHANGED_PART_1_OF_2, 1, 2)
        val testData = StubDataReader(uint8(3), uint8(BolusType.EXTENDED.key), sfloat(0f), sfloat(4f), uint16(5))
        val expected = HistoryEvent(eventInfo, BolusTemplateChangedBolusPart(3, Bolus(BolusType.EXTENDED, 0f, 4f, 5)))
        Assert.assertEquals(expected, BolusTemplateChangedPart1EventParser().parseEvent(eventInfo, testData))
    }
}