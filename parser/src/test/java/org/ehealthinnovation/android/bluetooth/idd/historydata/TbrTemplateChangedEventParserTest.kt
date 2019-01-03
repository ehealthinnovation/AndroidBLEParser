package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class TbrTemplateChangedEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.TBR_TEMPLATE_CHANGED, 1, 2)
        val testData = StubDataReader(uint8(3), uint8(TbrType.RELATIVE.key), sfloat(4f), uint16(5))
        val expected = HistoryEvent(eventInfo, TbrTemplateChanged(3, TbrType.RELATIVE, 4f, 5))
        Assert.assertEquals(expected, TbrTemplateChangedEventParser().parseEvent(eventInfo, testData))
    }
}