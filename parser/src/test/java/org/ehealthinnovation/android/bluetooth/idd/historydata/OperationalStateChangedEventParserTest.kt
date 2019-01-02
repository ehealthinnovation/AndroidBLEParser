package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.OperationalState
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class OperationalStateChangedEventParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.OPERATIONAL_STATE_CHANGED, 1, 2)
        val testData = StubDataReader(uint8(OperationalState.PRIMING.key), uint8(OperationalState.OFF.key))
        val expected = HistoryEvent(eventInfo, OperationalStateChangedData(OperationalState.PRIMING, OperationalState.OFF))
        Assert.assertEquals(expected, OperationalStateChangedEventParser().parseEvent(eventInfo, testData))
    }

}