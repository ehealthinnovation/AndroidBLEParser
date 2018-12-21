package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint32
import org.junit.Assert
import org.junit.Test

class EventInfoReaderTest {

    @Test
    fun readEventInfo() {
        val testData = StubDataReader(uint16(EventType.REFERENCE_TIME.key), uint32(1), uint16(2))
        val expected = EventInfo(EventType.REFERENCE_TIME, 1, 2)
        Assert.assertEquals(expected, readEventInfo(testData))
    }
}