package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class GetBolusTemplateResponseParserTest {

    @Test
    fun readGetBolusTemplateResponseTest() {
        val expected = GetBolusTemplateResponse(BolusTemplate(1,Bolus(BolusType.EXTENDED, 0f, 1f, 2), false, true, 5))
        val testReader = StubDataReader(
                uint8(1),
                uint8(0b101),
                uint8(BolusType.EXTENDED.key),
                sfloat(0f),
                sfloat(1f),
                uint16(2),
                uint16(5)
        )
        val actual = GetBolusTemplateResponseParser().readGetBolusTemplateResponse(testReader)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun readBolusTest(){
        val expected = Bolus(BolusType.MULTIWAVE, 1f, 2f, 3)
        val testData = StubDataReader(uint8(BolusType.MULTIWAVE.key), sfloat(1f), sfloat(2f), uint16(3))
        val actual = readBolus(testData)
        Assert.assertEquals(expected, actual)
    }
}