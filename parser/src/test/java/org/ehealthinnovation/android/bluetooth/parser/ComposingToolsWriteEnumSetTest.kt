package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.cgm.StatusFlag
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ComposingToolsWriteEnumSetTest{
    @Test
    fun writeEnumSetToIntegerSmokeTest(){
        val testSetAllFlags = EnumSet.allOf(StatusFlag::class.java)
        val expectedValue1 = 0xFF3F3F
        assertEquals(expectedValue1, writeEnumFlagsToInteger(testSetAllFlags) )

        val testSetEmpty = EnumSet.noneOf(StatusFlag::class.java)
        val expectedValue2 = 0x0
        assertEquals(expectedValue2, writeEnumFlagsToInteger(testSetEmpty))

        val testSetSomeFlags = EnumSet.of(
                StatusFlag.SESSION_STOPPED,
                StatusFlag.SENSOR_TYPE_INCORRECT_FOR_DEVICE,
                StatusFlag.DEVICE_SPECIFIC_ALERT
        )
        val expectedValue3 = 0x15
        assertEquals(expectedValue3, writeEnumFlagsToInteger(testSetSomeFlags))
    }

    @Test
    fun writeEnumSetToBufferSmokeTest(){
        val testDataWriter = StubDataWriter(uint24(0x15))
        val inputEnumSet = EnumSet.of(
                StatusFlag.SESSION_STOPPED,
                StatusFlag.SENSOR_TYPE_INCORRECT_FOR_DEVICE,
                StatusFlag.DEVICE_SPECIFIC_ALERT
        )
        writeEnumFlags(inputEnumSet, IntFormat.FORMAT_UINT24, testDataWriter)
        testDataWriter.checkWriteComplete()
    }


}