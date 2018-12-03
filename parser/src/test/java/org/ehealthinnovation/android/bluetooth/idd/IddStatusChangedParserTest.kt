package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.junit.Assert
import org.junit.Test
import java.util.*

class IddStatusChangedParserTest {

    @Test
    fun canParse() {
        val uuid = UUID.fromString("00002B20-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(uuid)
        Assert.assertTrue(IddStatusChangedParser().canParse(testPacket1))

        val featureCorruptedUuid = UUID.fromString("00002B21-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(featureCorruptedUuid)
        Assert.assertFalse(IddStatusChangedParser().canParse(testPacket2))
    }

    @Test
    fun parse() {
        val testPacketFull = MockCharacteristicPacket.mockPacketForRead(
                uint16(0xFF)
        )
        val expectedOutput1 = IddStatusChanged(
                status = EnumSet.allOf(Status::class.java)
        )
        Assert.assertEquals(expectedOutput1, IddStatusChangedParser().parse(testPacketFull))

        val testPacketNoFlags = MockCharacteristicPacket.mockPacketForRead(
                uint16(0)
        )
        val expectedOutput2 = IddStatusChanged(
                status = EnumSet.noneOf(Status::class.java)
        )
        Assert.assertEquals(expectedOutput2, IddStatusChangedParser().parse(testPacketNoFlags))

        val testPacketSomeFlags = MockCharacteristicPacket.mockPacketForRead(
                uint16(0x11)
        )
        val expectedOutput3 = IddStatusChanged(
                status = EnumSet.of(
                        Status.THERAPY_CONTROL_STATE_CHANGED,
                        Status.TOTAL_DAILY_INSULIN_STATUS_CHANGED
                )
        )
        Assert.assertEquals(expectedOutput3, IddStatusChangedParser().parse(testPacketSomeFlags))
    }
}