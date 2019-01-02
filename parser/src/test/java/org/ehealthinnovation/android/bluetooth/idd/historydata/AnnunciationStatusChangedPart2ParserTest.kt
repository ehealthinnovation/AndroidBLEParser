package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class AnnunciationStatusChangedPart2ParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.ANNUNCIATION_STATUS_CHANGED_PART_2_OF_2, 1, 2)
        val testData = StubDataReader(uint8(0b011), uint16(5), uint16(6))
        val expected = HistoryEvent(eventInfo, AnnunciationStatusChangeAdditionalInfo(5, 6, null))
        Assert.assertEquals(expected, AnnunciationStatusChangedPart2Parser().parseEvent(eventInfo, testData))
    }
}