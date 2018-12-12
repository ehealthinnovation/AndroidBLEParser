package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.DeliveredInsulinResponse
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.float
import org.junit.Assert
import org.junit.Test

class GetDeliveredInsulinResponseParserTest {

    @Test
    fun parseResponse() {
        val testDataReader = StubDataReader(float(1f), float(2f))
        val expected = DeliveredInsulinResponse(1f, 2f)
        Assert.assertEquals(expected, GetDeliveredInsulinResponseParser().parseResponse(testDataReader))
    }
}