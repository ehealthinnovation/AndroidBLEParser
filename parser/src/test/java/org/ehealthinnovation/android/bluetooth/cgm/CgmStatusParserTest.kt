package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint24
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class CgmStatusParserTest {

    @Test
    fun canParse() {
        val uuid = UUID.fromString("00002AA9-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(uuid)
        Assert.assertTrue(CgmStatusParser().canParse(testPacket1))

        val featureCorruptedUuid = UUID.fromString("00002AA7-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(featureCorruptedUuid)
        Assert.assertFalse(CgmStatusParser().canParse(testPacket2))
    }

    @Test
    fun parse() {
        val testPacket1 = MockCharacteristicPacket.mockPacketForRead(
                uint16(1234),
                uint24(0x111111)
        )
        val expectedOutput1 = CgmStatus(
                timeOffsetMinutes = 1234,
                status = EnumSet.of(StatusFlag.SESSION_STOPPED,
                        StatusFlag.DEVICE_SPECIFIC_ALERT,
                        StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED,
                        StatusFlag.TEMPERATURE_TOO_HIGH,
                        StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL,
                        StatusFlag.RATE_OF_DECREASE_EXCEEDED)
        )
        assertEquals(expectedOutput1, CgmStatusParser().parse(testPacket1))

        val testPacket2 = MockCharacteristicPacket.mockPacketForRead(
                uint16(6789),
                uint24(0x000000)
        )
        val expectedOutput2 = CgmStatus(
                timeOffsetMinutes = 6789,
                status = EnumSet.noneOf(StatusFlag::class.java)
        )
        assertEquals(expectedOutput2, CgmStatusParser().parse(testPacket2))

    }

    @Test
    fun readStatus() {
        val emptyStatusFlag = MockCharacteristicPacket.mockPacketForRead(uint24(0x000000))
        val expectedOutputSet1 = EnumSet.noneOf(StatusFlag::class.java)
        assertEquals(expectedOutputSet1, CgmStatusParser().readStatus(emptyStatusFlag.readData()))

        val fullStatusFlag = MockCharacteristicPacket.mockPacketForRead(uint24(0xFFFFFF))
        val expectedOutputSet2 = EnumSet.allOf(StatusFlag::class.java)
        assertEquals(expectedOutputSet2, CgmStatusParser().readStatus(fullStatusFlag.readData()))

        val someStatusFlag = MockCharacteristicPacket.mockPacketForRead(uint24(0x111111))
        val expectedOutputSet3 = EnumSet.of(StatusFlag.SESSION_STOPPED,
                StatusFlag.DEVICE_SPECIFIC_ALERT,
                StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED,
                StatusFlag.TEMPERATURE_TOO_HIGH,
                StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL,
                StatusFlag.RATE_OF_DECREASE_EXCEEDED)
        assertEquals(expectedOutputSet3, CgmStatusParser().readStatus(someStatusFlag.readData()))

    }
}