package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class ReservoirRemainingAmountChangedParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.RESERVOIR_REMAINING_AMOUNT_CHANGED, 1, 2)
        val testData = StubDataReader(sfloat(3f))
        val expected = HistoryEvent(eventInfo, ReservoirRemainingAmount(3f))
        Assert.assertEquals(expected, ReservoirRemainingAmountChangedParser().parseEvent(eventInfo, testData))
    }
}