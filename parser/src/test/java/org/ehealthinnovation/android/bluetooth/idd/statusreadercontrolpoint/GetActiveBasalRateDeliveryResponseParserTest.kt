package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.ActiveBasalRateDeliveryResponse
import org.ehealthinnovation.android.bluetooth.idd.BasalDeliveryContext
import org.ehealthinnovation.android.bluetooth.idd.TbrConfig
import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test
import java.util.*

class GetActiveBasalRateDeliveryResponseParserTest {

    @Test
    fun readActiveBasalRateDeliveryResponse() {
        val testValue = StubDataReader(
                uint8(0x07),
                uint8(1),
                sfloat(2.3f),
                uint8(TbrType.ABSOLUTE.key),
                sfloat(3.4f),
                uint16(5),
                uint16(6),
                uint8(7),
                uint8(BasalDeliveryContext.DEVICE_BASED.key)
        )

        val expected = ActiveBasalRateDeliveryResponse(
                1,
                2.3f,
                TbrConfig(TbrType.ABSOLUTE, 3.4f, 5, 6),
                7,
                BasalDeliveryContext.DEVICE_BASED
        )

        Assert.assertEquals(expected, GetActiveBasalRateDeliveryResponseParser().readActiveBasalRateDeliveryResponse(testValue))
    }

    @Test
    fun readFlags() {
        val testReaderSomeFlags = StubDataReader(uint8(0x01))
        val expectedOutputFlags = EnumSet.of(GetActiveBasalRateDeliveryResponseParser.Flag.TBR_PRESENT)
        Assert.assertEquals(expectedOutputFlags, GetActiveBasalRateDeliveryResponseParser().readFlags(testReaderSomeFlags))

        val testReaderAllFlags = StubDataReader(uint8(0xFF))
        val expectedOutputFlags2 = EnumSet.allOf(GetActiveBasalRateDeliveryResponseParser.Flag::class.java)
        Assert.assertEquals(expectedOutputFlags2, GetActiveBasalRateDeliveryResponseParser().readFlags(testReaderAllFlags))

        val testReaderNoFlags = StubDataReader(uint8(0x00))
        val expectedOutputFlags3 = EnumSet.noneOf(GetActiveBasalRateDeliveryResponseParser.Flag::class.java)
        Assert.assertEquals(expectedOutputFlags3, GetActiveBasalRateDeliveryResponseParser().readFlags(testReaderNoFlags))
    }

    @Test
    fun readBasalDeliveryContext() {
        val testValue = StubDataReader(uint8(BasalDeliveryContext.ARTIFICIAL_PANCREAS_CONTROLLER.key))
        val expectedOutput = BasalDeliveryContext.ARTIFICIAL_PANCREAS_CONTROLLER
        Assert.assertEquals(expectedOutput, GetActiveBasalRateDeliveryResponseParser().readBasalDeliveryContext(testValue))
    }

    @Test
    fun readTbrConfig() {
        val testValue = StubDataReader(
                uint8(TbrType.ABSOLUTE.key),
                sfloat(23.1f),
                uint16(40),
                uint16(12)
        )

        val expected = TbrConfig(
                TbrType.ABSOLUTE,
                23.1f,
                40,
                12
        )

        Assert.assertEquals(expected, GetActiveBasalRateDeliveryResponseParser().readTbrConfig(testValue))
    }
}