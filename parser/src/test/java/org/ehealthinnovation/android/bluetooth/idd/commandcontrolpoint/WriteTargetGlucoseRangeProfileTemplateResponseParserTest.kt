package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class WriteTargetGlucoseRangeProfileTemplateResponseParserTest {

    @Test
    fun parseWriteTargetGlucoseRangeProfileTemplateResponse() {
        val testData = StubDataReader(uint8(0b01), uint8(2), uint8(3))
        val actual = WriteTargetGlucoseRangeProfileTemplateResponseParser().parseWriteTargetGlucoseRangeProfileTemplateResponse(testData)
        val expected = WriteTargetGlucoseRangeProfileTemplateResponse(true, 2, 3)
        Assert.assertEquals(expected, actual)
    }
}