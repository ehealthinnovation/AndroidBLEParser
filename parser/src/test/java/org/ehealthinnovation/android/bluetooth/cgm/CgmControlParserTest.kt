package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test
import java.util.*

class CgmControlParserTest {

    @Test
    fun canParse() {
        val uuid = UUID.fromString("00002AAC-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(uuid)
        Assert.assertTrue(CgmControlParser().canParse(testPacket1))

        val contextCorruptedUuid = UUID.fromString("00002AAD-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(contextCorruptedUuid)
        Assert.assertFalse(CgmControlParser().canParse(testPacket2))
    }

    @Test
    fun parseSmokeTest() {
        val mockPackage = MockCharacteristicPacket.mockPacketForRead(
                uint8(Opcode.RESPONSE_CODE.key),
                uint8(Opcode.START_THE_SESSION.key),
                uint8(ResponseCode.SUCCESS.key))
        val expectedResult = CgmControlResponse(Opcode.START_THE_SESSION, ResponseCode.SUCCESS)
        Assert.assertEquals(expectedResult, CgmControlParser().parse(mockPackage))
    }

    @Test
    fun readResponse() {
        val mockPackage = MockCharacteristicPacket.mockPacketForRead(
                uint8(Opcode.RESPONSE_CODE.key)
        )
        //no return value, just need to read one byte from the buffer
        CgmControlParser().readResponseOpcode(mockPackage.readData())
    }


    @Test(expected = Exception::class)
    fun readResponseInvalidCase() {
        val mockPackage = MockCharacteristicPacket.mockPacketForRead(
                uint8(Opcode.GET_CGM_COMMUNICATION_INTERVAL.key)
        )
        //no return value, just need to read one byte from the buffer
        CgmControlParser().readResponseOpcode(mockPackage.readData())
    }
}