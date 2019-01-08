package org.ehealthinnovation.android.bluetooth.idd.commanddata

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.RangedTimeBlock
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class ReadRangedValueTimeBlockProfileTemplateResponseParserTest {
    @Test
    fun parseResponseOperandTargetGlucoseRangeProfileTemplateResponse() {
        val expectedTargetGlucoseRangeProfileTemplateResponse = ReadTargetGlucoseRangeProfileTemplateResponse(
                ReadRangedValuesProfileTemplateResponseOperand(
                        1, 2,
                        RangedTimeBlock(3, 4f, 5f),
                        RangedTimeBlock(6, 7f, 8f)))
        val testData = StubDataReader(uint8(0b001), uint8(1), uint8(2),
                uint16(3), sfloat(4f), sfloat(5f),
                uint16(6), sfloat(7f), sfloat(8f))

        Assert.assertEquals(expectedTargetGlucoseRangeProfileTemplateResponse, ReadTargetGlucoseRangeProfileTemplateResponse(ReadRangedValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(testData)))

    }

}