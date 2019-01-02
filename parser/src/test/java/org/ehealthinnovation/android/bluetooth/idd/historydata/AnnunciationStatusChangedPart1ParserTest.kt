package org.ehealthinnovation.android.bluetooth.idd.historydata

import android.util.EventLog
import org.ehealthinnovation.android.bluetooth.idd.Annunciation
import org.ehealthinnovation.android.bluetooth.idd.AnnunciationStatus
import org.ehealthinnovation.android.bluetooth.idd.AnnunciationType
import org.ehealthinnovation.android.bluetooth.parser.StubData
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class AnnunciationStatusChangedPart1ParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.ANNUNCIATION_STATUS_CHANGED_PART_1_OF_2, 1, 2)
        val testData = StubDataReader(uint8(0b01), uint16(3), uint16(AnnunciationType.BATTERY_EMPTY.key), uint8(AnnunciationStatus.CONFIRMED.key), uint16(8))
        val expected = HistoryEvent(eventInfo, AnnunciationStatusChanged(3, AnnunciationType.BATTERY_EMPTY, AnnunciationStatus.CONFIRMED, 8, null))
        Assert.assertEquals(expected, AnnunciationStatusChangedPart1Parser().parseEvent(eventInfo, testData))
    }
}
