package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.StubValueReader
import org.junit.Assert
import org.junit.Test
import java.util.*

class GlucoseFeatureParserTest {

    @Test
    fun canParseSmokeTest(){
        val featureUuid = UUID.fromString("00002A51-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(featureUuid)
        Assert.assertTrue(GlucoseFeatureParser().canParse(testPacket1))

        val featureCorruptedUuid = UUID.fromString("00002A55-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(featureCorruptedUuid)
        Assert.assertFalse(GlucoseFeatureParser().canParse(testPacket2))
    }


    @Test
    fun testReadSupportedEmptyFlag() {
        val EMPTY_FLAGS_VALUE = 0
        val EXPECTED_OUTPUT = EnumSet.noneOf(Feature::class.java)
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.LOW_BATTERY_DETECTION)
        val testDataReader = StubValueReader(EMPTY_FLAGS_VALUE, EMPTY_FLAGS_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT,
                readSupportFeatures(testDataReader))

        //Negative case to ensure the above is not false negative
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, readSupportFeatures(testDataReader))

    }

    @Test
    fun testReadSupportedFullFlags(){
        val FULL_FLAG_VALUE = 0x0FFF
        val EXPECTED_OUTPUT = EnumSet.allOf(Feature::class.java).minus(Feature.RESERVED_FOR_FUTURE_USE)
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.LOW_BATTERY_DETECTION)
        val testDataReader = StubValueReader(FULL_FLAG_VALUE, FULL_FLAG_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT,
                readSupportFeatures(testDataReader))

        //Negative case to ensure the above is not false negative
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, readSupportFeatures(testDataReader))
    }

    @Test
    fun testReadSupportedSingleFlag(){
        val SINGLE_FLAG_VALUE = 0x0001
        val EXPECTED_OUTPUT = EnumSet.of(Feature.LOW_BATTERY_DETECTION)
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.GENERAL_DEVICE_FAULT_DETECTION)
        val testDataReader = StubValueReader(SINGLE_FLAG_VALUE, SINGLE_FLAG_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT,
                readSupportFeatures(testDataReader))

        //Negative case to ensure the above is not false negative
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, readSupportFeatures(testDataReader))
    }

    @Test
    fun testReadSupportedDoubleFlag(){
        val DOUBLE_FLAG_VALUE = 0x0011
        val EXPECTED_OUTPUT = EnumSet.of(Feature.LOW_BATTERY_DETECTION, Feature.SENSOR_STRIP_TYPE_ERROR_DETECTION )
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.GENERAL_DEVICE_FAULT_DETECTION)
        val testDataReader = StubValueReader(DOUBLE_FLAG_VALUE, DOUBLE_FLAG_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT,
                readSupportFeatures(testDataReader))

        //Negative case to ensure the above is not false negative
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, readSupportFeatures(testDataReader))
    }

}