package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.CounterResponse
import org.ehealthinnovation.android.bluetooth.idd.CounterType
import org.ehealthinnovation.android.bluetooth.idd.CounterValueSelection
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sint32
import org.ehealthinnovation.android.bluetooth.parser.uint32
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test


class GetCounterResponseParserTest {

    @Test
    fun parseResponse() {
        val testDataReader = StubDataReader(uint8(CounterType.IDD_LIFETIME.key), uint8(CounterValueSelection.ELAPSED.key), sint32(12))
        val expected = CounterResponse(CounterType.IDD_LIFETIME, CounterValueSelection.ELAPSED, 12)
        Assert.assertEquals(expected, GetCounterResponseParser().parseResponse(testDataReader))
    }
}