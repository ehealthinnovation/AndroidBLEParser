package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.StubValueReader
import org.ehealthinnovation.android.bluetooth.parser.uint24
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test
import java.util.*

class CgmFeatureParserTest {


    @Test
    fun canParseTest() {
        val featureUuid = UUID.fromString("00002AA8-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(featureUuid)
        Assert.assertTrue(CgmFeatureParser().canParse(testPacket1))

        val featureCorruptedUuid = UUID.fromString("00002AA9-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(featureCorruptedUuid)
        Assert.assertFalse(CgmFeatureParser().canParse(testPacket2))
    }

    @Test
    fun parseSmokeTest() {
        val packetToTest = MockCharacteristicPacket.mockPacketForRead(
                uint24(0x010101),
                uint8(SampleType.ARTERIAL_PLASMA.key, SampleLocation.FINGER.key))
        val expectedOutput = CgmFeature(
                supportedFeature = EnumSet.of(Feature.CALIBRATION, Feature.SENSOR_RESULT_HIGH_LOW_DETECTION, Feature.CGM_QUALITY),
                sampleLocation = SampleLocation.FINGER,
                sampleType = SampleType.ARTERIAL_PLASMA
        )
        Assert.assertEquals(expectedOutput, CgmFeatureParser().parse(packetToTest))
    }

    @Test
    fun parseTestNoFlags() {
        val packetToTest = MockCharacteristicPacket.mockPacketForRead(
                uint24(0x000000),
                uint8(SampleType.ARTERIAL_PLASMA.key, SampleLocation.FINGER.key))
        val expectedOutput = CgmFeature(
                supportedFeature = EnumSet.noneOf(Feature::class.java),
                sampleLocation = SampleLocation.FINGER,
                sampleType = SampleType.ARTERIAL_PLASMA
        )
        Assert.assertEquals(expectedOutput, CgmFeatureParser().parse(packetToTest))
    }

    @Test
    fun testReadSupportedFeaturesEmptyFlag() {
        val EMPTY_FLAGS_VALUE = 0
        val EXPECTED_OUTPUT = EnumSet.noneOf(Feature::class.java)
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.LOW_BATTERY_DETECTION)
        val testDataReader = StubValueReader(EMPTY_FLAGS_VALUE, EMPTY_FLAGS_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT, CgmFeatureParser().readSupportedFeatures(testDataReader))
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, CgmFeatureParser().readSupportedFeatures(testDataReader))
    }

    @Test
    fun testReadSupportedFeaturesFullFlag() {
        val FULL_FLAGS_VALUE = 0xFFFFFF
        val EXPECTED_OUTPUT = EnumSet.allOf(Feature::class.java).minus(Feature.RESERVE_FOR_FUTURE)
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.LOW_BATTERY_DETECTION)
        val testDataReader = StubValueReader(FULL_FLAGS_VALUE, FULL_FLAGS_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT, CgmFeatureParser().readSupportedFeatures(testDataReader))
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, CgmFeatureParser().readSupportedFeatures(testDataReader))
    }

    @Test
    fun testReadSupportedTrippleFlag() {
        val TRIPPLE_FLAG_VALUE = 0x010101
        val EXPECTED_OUTPUT = EnumSet.of(Feature.CALIBRATION, Feature.SENSOR_RESULT_HIGH_LOW_DETECTION, Feature.CGM_QUALITY)
        val NEGATIVE_CHECK_OUTPUT = EnumSet.of(Feature.PATIENT_HIGH_LOW_ALERTS)
        val testDataReader = StubValueReader(TRIPPLE_FLAG_VALUE, TRIPPLE_FLAG_VALUE)

        Assert.assertEquals(EXPECTED_OUTPUT,
                CgmFeatureParser().readSupportedFeatures(testDataReader))

        //Negative case to ensure the above is not false negative
        Assert.assertNotEquals(NEGATIVE_CHECK_OUTPUT, CgmFeatureParser().readSupportedFeatures(testDataReader))
    }


    @Test
    fun readSampleTypeAndLocation() {
        val INPUT = 0x14
        val EXPECTED_VALUE = Pair(SampleType.VENOUS_PLASMA, SampleLocation.FINGER)
        val testDataReader = StubValueReader(INPUT)
        Assert.assertEquals(EXPECTED_VALUE,
                CgmFeatureParser().readSampleTypeAndLocation(testDataReader))


    }
}