package org.ehealthinnovation.android.bluetooth.idd.historydata

import net.bytebuddy.pool.TypePool
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class EmptyEventDataParserTest {

    @Test
    fun parseEventTest() {
        val eventInfo = EventInfo(EventType.DATA_CORRUPTION, 1, 2)
        val testData = StubDataReader()
        val expected = eventInfo
        Assert.assertEquals(expected, EmptyEventDataParser().parseEvent(eventInfo, testData).eventInfo)
    }
}