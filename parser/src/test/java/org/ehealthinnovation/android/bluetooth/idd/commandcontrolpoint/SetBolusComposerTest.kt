package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.BolusActivationType
import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.idd.SetBolus
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.sfloate
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class SetBolusComposerTest {

    @Test
    fun composeTest() {
        val testCase = BolusConfig(Bolus(BolusType.EXTENDED, 0f, 1f, 2), false, true, 3, 4, BolusActivationType.MANUAL_BOLUS)
        val testWriter = StubDataWriter(
                uint8(0b10111),
                uint8(BolusType.EXTENDED.key),
                sfloate(0f, -1),
                sfloate(1f, -1),
                uint16(2),
                uint16(3),
                uint8(4),
                uint8(BolusActivationType.MANUAL_BOLUS.key)
        )
        SetBolusComposer().compose(testCase, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun getFlagsTest() {
        val allFlagsCase = BolusConfig(Bolus(BolusType.FAST, 1f, 0f, 0), true, true,2, 3, BolusActivationType.COMMANDED_BOLUS)
        val expectedAllFlags = EnumSet.allOf(SetBolusComposer.Flag::class.java)
        Assert.assertEquals(expectedAllFlags, SetBolusComposer().getFlags(allFlagsCase))

        val noFlagCase = BolusConfig(Bolus(BolusType.FAST, 1f, 0f, 0), false, false)
        val expectedNoFlag = EnumSet.noneOf(SetBolusComposer.Flag::class.java)
        Assert.assertEquals(expectedNoFlag, SetBolusComposer().getFlags(noFlagCase))

        val someFlags = BolusConfig(Bolus(BolusType.FAST, 1f, 0f, 0), true, false, 2)
        val expectedSomeFlags = EnumSet.of(SetBolusComposer.Flag.BOLUS_DELIVERY_REASON_CORRECTION, SetBolusComposer.Flag.BOLUS_DELAY_TIME_PRESENT)
        Assert.assertEquals(expectedSomeFlags, SetBolusComposer().getFlags(someFlags))
    }

    @Test
    fun writeBolusTest() {
        val bolusTestCase = Bolus(BolusType.MULTIWAVE, 1f, 2f, 3)
        val testWriter = StubDataWriter(uint8(BolusType.MULTIWAVE.key), sfloate(1f, -1), sfloate(2f, -1), uint16(3))
        SetBolusComposer().writeBolus(bolusTestCase, testWriter)
        testWriter.checkWriteComplete()
    }
}