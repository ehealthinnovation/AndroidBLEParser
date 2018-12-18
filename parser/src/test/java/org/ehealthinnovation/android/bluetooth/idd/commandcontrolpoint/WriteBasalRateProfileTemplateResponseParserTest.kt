package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class WriteBasalRateProfileTemplateResponseParserTest {

    @Test
    fun parseWriteBasalRateProfileTemplateResponseTest() {
        val testData = StubDataReader(uint8(0b01), uint8(2), uint8(3))
        val actual = WriteBasalRateProfileTemplateResponseParser().parseWriteBasalRateProfileTemplateResponse(testData)
        val expected = WriteBasalRateProfileTemplateResponse(true, 2, 3)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun readTransactionCompletedTest() {
        val testData = StubDataReader(uint8(0b01))
        val actual = WriteBasalRateProfileTemplateResponseParser().readTransactionCompleted(testData)
        Assert.assertEquals(true, actual)

        val testData2 = StubDataReader(uint8(0b00))
        val actual2 = WriteBasalRateProfileTemplateResponseParser().readTransactionCompleted(testData2)
        Assert.assertEquals(false, actual2)
    }
}