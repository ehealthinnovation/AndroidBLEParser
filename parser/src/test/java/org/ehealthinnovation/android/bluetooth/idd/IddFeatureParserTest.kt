package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test
import java.util.*

class IddFeatureParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B23-0000-1000-8000-00805F9B34FB", IddFeatureParser()::canParse)
    }

    @Test
    fun parse() {
        val testPacketWithAllFeature = MockCharacteristicPacket.mockPacketForRead(
                uint16(0),
                uint8(0),
                sfloat(12.3f),
                uint24(0xFFFFFF)
        )
        val expectedFeature = IddFeature(12.3f, EnumSet.allOf(Feature::class.java))
        Assert.assertEquals(expectedFeature, IddFeatureParser().parse(testPacketWithAllFeature))

        val testPacketWithNoFeature = MockCharacteristicPacket.mockPacketForRead(
                uint16(0),
                uint8(0),
                sfloat(13.3f),
                uint24(0)
        )
        val expectedFeature2 = IddFeature(13.3f, EnumSet.noneOf(Feature::class.java))
        Assert.assertEquals(expectedFeature2, IddFeatureParser().parse(testPacketWithNoFeature))

        val testPacketWithSomeFeature = MockCharacteristicPacket.mockPacketForRead(
                uint16(0),
                uint8(0),
                sfloat(13.3f),
                uint24(0x111111)
        )
        val expectedFeature3 = IddFeature(13.3f,
                EnumSet.of(Feature.E2E_PROTECTION_SUPPORTED,
                        Feature.TBR_TEMPLATE_SUPPORTED,
                        Feature.BOLUS_DELAY_TIME_SUPPORTED,
                        Feature.ISF_PROFILE_TEMPLATE_SUPPORTED))
        Assert.assertEquals(expectedFeature3, IddFeatureParser().parse(testPacketWithSomeFeature))


    }
}