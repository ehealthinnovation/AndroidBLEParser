package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TotalInsulinDeliveredResponse
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

class GetTotalInsulinDeliveredResponseParserTest {

    @Test
    fun parseResponse() {
        val testDataReader = StubDataReader(sfloat(1f), sfloat(2f), sfloat(3f))
        val expected = TotalInsulinDeliveredResponse(1f, 2f, 3f)
        Assert.assertEquals(expected, GetTotalInsulinDeliveredResponseParser().parseResponse(testDataReader))
    }
}