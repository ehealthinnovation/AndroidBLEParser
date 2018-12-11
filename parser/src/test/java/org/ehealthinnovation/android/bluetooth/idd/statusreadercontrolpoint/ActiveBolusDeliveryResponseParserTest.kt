package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.*
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class ActiveBolusDeliveryResponseParserTest {

    @Test
    fun readGetActiveBolusDeliveryResponse() {
        val testReadData = StubDataReader(
                uint8(0x13),
                uint16(1234),
                uint8(BolusType.FAST.key),
                sfloat(5.6f),
                sfloat(0f),
                uint16(0),
                uint16(78),
                uint8(11)
        )

        val expectedBolusResponse = ActiveBolusDeliveryResponse(
                BolusConfiguration(
                        Bolus(1234, BolusType.FAST, 5.6f, 0f, 0),
                        78,
                        11,
                        null,
                        EnumSet.of(BolusDeliveryReason.MEAL)))

        Assert.assertEquals(expectedBolusResponse, ActiveBolusDeliveryResponseParser().readGetActiveBolusDeliveryResponse(testReadData))
    }



    @Test
    fun readBolusFlags() {
        val testReaderSomeFlags = StubDataReader(uint8(0x11))
        val expectedOutputFlags = EnumSet.of(BolusFlag.DELAY_TIME_PRESENT, BolusFlag.DELIVERY_REASON_MEAL)
        Assert.assertEquals(expectedOutputFlags, ActiveBolusDeliveryResponseParser().readBolusFlags(testReaderSomeFlags))

        val testReaderAllFlags = StubDataReader(uint8(0xFF))
        val expectedOutputFlags2 = EnumSet.allOf(BolusFlag::class.java)
        Assert.assertEquals(expectedOutputFlags2, ActiveBolusDeliveryResponseParser().readBolusFlags(testReaderAllFlags))

        val testReaderNoFlags = StubDataReader(uint8(0x00))
        val expectedOutputFlags3 = EnumSet.noneOf(BolusFlag::class.java)
        Assert.assertEquals(expectedOutputFlags3, ActiveBolusDeliveryResponseParser().readBolusFlags(testReaderNoFlags))
    }

    @Test
    fun readBolusReason() {
        val testReasonNoReasonPresent = EnumSet.noneOf(BolusFlag::class.java)
        val expectedReasonSet = EnumSet.noneOf(BolusDeliveryReason::class.java)
        Assert.assertEquals(expectedReasonSet, ActiveBolusDeliveryResponseParser().readBolusReason(testReasonNoReasonPresent))

        val testReasonAllReasonPresent = EnumSet.of(BolusFlag.DELIVERY_REASON_MEAL, BolusFlag.DELIVERY_REASON_CORRECTION)
        val expectedReasonSet2 = EnumSet.allOf(BolusDeliveryReason::class.java)
        Assert.assertEquals(expectedReasonSet2, ActiveBolusDeliveryResponseParser().readBolusReason(testReasonAllReasonPresent))
    }

    @Test
    fun readBolus() {
        val testReadData = StubDataReader(
                uint16(1234),
                uint8(BolusType.MULTIWAVE.key),
                sfloat(5.6f),
                sfloat(7.8f),
                uint16(90)
        )
        val expectedBolus = Bolus(1234, BolusType.MULTIWAVE, 5.6f, 7.8f, 90)
        Assert.assertEquals(expectedBolus, ActiveBolusDeliveryResponseParser().readBolus(testReadData))
    }
}