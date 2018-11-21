package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert.*
import org.junit.Test

class BluetoothTimeZoneUtilityTest {
    @Test
    fun testLogicForTimeZoneValidity() {
        val VALID_VALUE1 = 56
        val VALID_VALUE2 = -48
        val VALID_VALUE3 = 0
        val INVALID_VALUE1 = 254
        val INVALID_VALUE2 = 57
        val INVALID_VALUE3 = -49

        assertTrue(BluetoothTimeZoneUtility.isTimeZoneValueValid(VALID_VALUE1))
        assertTrue(BluetoothTimeZoneUtility.isTimeZoneValueValid(VALID_VALUE2))
        assertTrue(BluetoothTimeZoneUtility.isTimeZoneValueValid(VALID_VALUE3))
        assertFalse(BluetoothTimeZoneUtility.isTimeZoneValueValid(INVALID_VALUE1))
        assertFalse(BluetoothTimeZoneUtility.isTimeZoneValueValid(INVALID_VALUE2))
        assertFalse(BluetoothTimeZoneUtility.isTimeZoneValueValid(INVALID_VALUE3))
    }

    @Test
    fun convertTimeZoneIntoOffsetInMinutes() {
        val calculatedOffsetInMinutes1 = -240 //the UTC-4 timezone -240 minutes
        val inputTimezone1 = BluetoothTimeZone(-16)
        assertEquals(calculatedOffsetInMinutes1, BluetoothTimeZoneUtility.getTimeZoneOffsetFromUTCInMinute(inputTimezone1))


        val calculatedOffsetInMinutes2 = 0 //the UTC timezone
        val inputTimezone2 = BluetoothTimeZone(0)
        assertEquals(calculatedOffsetInMinutes2, BluetoothTimeZoneUtility.getTimeZoneOffsetFromUTCInMinute(inputTimezone2))

        val calculatedOffsetInMinutes3 = null //the timezone unknown
        val inputTimezone3 = BluetoothTimeZone(-128)
        assertEquals(calculatedOffsetInMinutes3, BluetoothTimeZoneUtility.getTimeZoneOffsetFromUTCInMinute(inputTimezone3))

    }
}