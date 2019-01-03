package org.ehealthinnovation.android.bluetooth.idd.historydata

import android.util.EventLog
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class MaxBolusAmountChangedEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.MAX_BOLUS_AMOUNT_CHANGED, 1, 2)
        val testData = StubDataReader(sfloat(3f), sfloat(4f))
        val expected = HistoryEvent(eventInfo, MaxBolusAmountChanged(3f, 4f))
        Assert.assertEquals(expected, MaxBolusAmountChangedEventParser().parseEvent(eventInfo, testData))
    }
}