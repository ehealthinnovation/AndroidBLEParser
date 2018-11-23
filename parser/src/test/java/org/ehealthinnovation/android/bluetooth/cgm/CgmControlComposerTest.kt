package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.MockCharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

class CgmControlComposerTest {

    @Test
    fun composeStartSession() {
        val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(uint8(Opcode.START_THE_SESSION.key))
        CgmControlComposer().composeStartSession(testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }

    @Test
    fun composeStopSession() {
        val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(uint8(Opcode.STOP_THE_SESSION.key))
        CgmControlComposer().composeStopSession(testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }
}