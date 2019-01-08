package org.ehealthinnovation.android.bluetooth.idd.commanddata

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SingleValueTimeBlock
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class ReadSingleValueTimeBlockProfileTemplateResponseParserTest {

    @Test
    fun parseResponseOperandBasalRateProfileTemplateResponse() {
        val expectedBasalProfileTemplateResponse = ReadBasalProfileTemplateResponse(ReadSingleValueProfileTemplateResponseOperand(1, 2,
                SingleValueTimeBlock(3, 4f),
                SingleValueTimeBlock(5, 6f),
                SingleValueTimeBlock(7, 8f)
        ))
        val testData = StubDataReader(uint8(0b011), uint8(1), uint8(2),
                uint16(3), sfloat(4f),
                uint16(5), sfloat(6f),
                uint16(7), sfloat(8f))

        Assert.assertEquals(expectedBasalProfileTemplateResponse, ReadBasalProfileTemplateResponse(ReadSingleValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(testData)))
    }

    @Test
    fun parseResponseOperandI2CHORateProfileTemplateResponse() {
        val expectedI2CHOProfileTemplateResponse = ReadI2CHOProfileTemplateResponse(
                ReadSingleValueProfileTemplateResponseOperand(
                1, 2,
                SingleValueTimeBlock(3, 4f),
                SingleValueTimeBlock(5, 6f),
                null))
        val testData = StubDataReader(uint8(0b001), uint8(1), uint8(2),
                uint16(3), sfloat(4f),
                uint16(5), sfloat(6f))

        Assert.assertEquals(expectedI2CHOProfileTemplateResponse, ReadI2CHOProfileTemplateResponse (ReadSingleValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(testData)))
    }

    @Test
    fun parseResponseOperandISFProfileTemplateResponse() {
        val expectedISFProfileTemplateResponse = ReadISFProfileTemplateResponse(
                ReadSingleValueProfileTemplateResponseOperand(
                1, 2,
                SingleValueTimeBlock(3, 4f),
                null,
                null))
        val testData = StubDataReader(uint8(0b000), uint8(1), uint8(2),
                uint16(3), sfloat(4f))

        Assert.assertEquals(expectedISFProfileTemplateResponse, ReadISFProfileTemplateResponse(ReadSingleValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(testData)))
    }
}