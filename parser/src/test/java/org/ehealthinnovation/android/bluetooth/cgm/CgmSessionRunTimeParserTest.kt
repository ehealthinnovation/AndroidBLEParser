package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.junit.Assert
import org.junit.Test
import java.util.*

class CgmSessionRunTimeParserTest {

    @Test
    fun canParse() {

        val uuid = UUID.fromString("00002AAB-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(uuid)
        Assert.assertTrue(CgmSessionRunTimeParser().canParse(testPacket1))

        val corruptedUuid = UUID.fromString("00002AAC-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(corruptedUuid)
        Assert.assertFalse(CgmSessionRunTimeParser().canParse(testPacket2))
    }

    @Test
    fun parseSmokeTest() {
        val packetToTest = MockCharacteristicPacket.mockPacketForRead(
                uint16(0x0201))
        val expectedOutput = CgmSessionRunTime(
                0x0201
        )
        Assert.assertEquals(expectedOutput, CgmSessionRunTimeParser().parse(packetToTest))
    }
}