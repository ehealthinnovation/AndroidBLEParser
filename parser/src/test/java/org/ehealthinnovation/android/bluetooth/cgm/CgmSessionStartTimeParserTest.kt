package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert.assertEquals
import org.junit.Test

class CgmSessionStartTimeParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002AAA-0000-1000-8000-00805F9B34FB", CgmSessionStartTimeParser()::canParse)
    }

    @Test
    fun parseSmokeTest() {
        val mockTestPacket = MockCharacteristicPacket.mockPacketForRead(
                uint16(2018), uint8(6), uint8(6), uint8(14), uint8(53), uint8(14),
                sint8(0),
                uint8(0)
        )
        val expectedParsedResult = CgmSessionStartTime(
                BluetoothDateTime(2018, 6, 6, 14, 53, 14),
                BluetoothTimeZone(0),
                DstOffset.STANDARD_TIME
        )

        assertEquals(expectedParsedResult, CgmSessionStartTimeParser().parse(mockTestPacket))

    }


    @Test
    fun parseSmokeTest2() {
        val mockTestPacket = MockCharacteristicPacket.mockPacketForRead(
                uint16(2018), uint8(11), uint8(20), uint8(15), uint8(53), uint8(14),
                sint8(13),
                uint8(DstOffset.DOUBLE_DAYLIGHT_TIME.key)
        )
        val expectedParsedResult = CgmSessionStartTime(
                BluetoothDateTime(2018, 11, 20, 15, 53, 14),
                BluetoothTimeZone(13),
                DstOffset.DOUBLE_DAYLIGHT_TIME
        )

        assertEquals(expectedParsedResult, CgmSessionStartTimeParser().parse(mockTestPacket))
    }


    @Test
    fun readTimeZoneSmokeTest() {
        val dataReader = StubDataReader(sint8(-40), sint8(32))
        val expectValue1 = BluetoothTimeZone(-40)
        val expectValue2 = BluetoothTimeZone(32)
        assertEquals(expectValue1, readTimeZone(dataReader))
        assertEquals(expectValue2, readTimeZone(dataReader))
    }

    @Test(expected = Exception::class)
    fun readTimeZoneInvalidValid() {
        val dataReader = StubDataReader(sint8(-242))
        readTimeZone(dataReader)
    }


    @Test
    fun readDst() {
        val dataReader = StubDataReader(uint8(0), uint8(2), uint8(8), uint8(255), uint8(10))
        assertEquals(DstOffset.STANDARD_TIME, readDst(dataReader))
        assertEquals(DstOffset.HALF_AN_HOUR_DAYLIGHT_TIME, readDst(dataReader))
        assertEquals(DstOffset.DOUBLE_DAYLIGHT_TIME, readDst(dataReader))
        assertEquals(DstOffset.DST_IS_NOT_KNOWN, readDst(dataReader))
        assertEquals(DstOffset.RESERVE_FOR_FUTURE_USE, readDst(dataReader))
    }
}