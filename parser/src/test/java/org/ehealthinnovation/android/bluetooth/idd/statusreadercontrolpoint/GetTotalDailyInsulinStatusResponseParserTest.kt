package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TotalDailyInsulinStatusResponse
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

class GetTotalDailyInsulinStatusResponseParserTest {

    @Test
    fun parseResponse() {
        val testDataReader = StubDataReader(sfloat(1f), sfloat(2f), sfloat(3f))
        val expected = TotalDailyInsulinStatusResponse(1f, 2f, 3f)
        Assert.assertEquals(expected, GetTotalDailyInsulinStatusResponseParser().parseResponse(testDataReader))
    }
}