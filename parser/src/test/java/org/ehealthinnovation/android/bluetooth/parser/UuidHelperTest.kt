package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test
import java.util.*

@ExperimentalUnsignedTypes
class UuidHelperTest {
    @Test
    fun convert128BitTo16BitsUuid() {
        val baseUuidString = "00004021-0000-1000-8000-00805F9B34FB"
        val basedUuid = UUID.fromString(baseUuidString)
        val converted16BitsUuid = BluetoothUuidHelper.convertUuidToShortUuid(basedUuid)
        Assert.assertEquals(0x4021, converted16BitsUuid)
    }

    @Test
    fun createUuidFromShortFormSmokeTest() {
        val glucoseMeasurementShortUuid = 0x2A34
        val actualGlucoseMeasurement = BluetoothUuidHelper.createUuidFromShortUuid(glucoseMeasurementShortUuid)
        val expectedUuid = UUID.fromString("00002A34-0000-1000-8000-00805F9B34FB")
        Assert.assertEquals(expectedUuid, actualGlucoseMeasurement)
    }

    @Test(expected = Exception::class)
    fun createUuidFromShortFormOutOfRange() {
        val outOfRangeShortUuid = 0x10000
        BluetoothUuidHelper.createUuidFromShortUuid(outOfRangeShortUuid)
    }

    @Test
    fun isValidBluetoothUuidSmokeTest(){
        val uuid1 = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB")
        Assert.assertTrue(BluetoothUuidHelper.isValidBluetoothUuid(uuid1))
        val uuid2 = UUID.fromString("00000000-0000-1000-8000-00809F9B34FB")
        Assert.assertFalse(BluetoothUuidHelper.isValidBluetoothUuid(uuid2))
    }

}