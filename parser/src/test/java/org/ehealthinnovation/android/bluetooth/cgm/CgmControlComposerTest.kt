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

    @Test (expected = Exception::class)
    fun creatingInvalidCommunicationInterval(){
        CommunicationInterval(256)
    }


    @Test
    fun creatingValidCommunicationInterval(){
        CommunicationInterval(255)
    }

    @Test
    fun composeSetCommunicationIntervalTest(){
        val testWriterPacket =  MockCharacteristicPacket.mockPacketWriter(
                uint8(Opcode.SET_CGM_COMMUNICATION_INTERVAL.key),
                uint8(123))
        CgmControlComposer().composeSetCommunicationInterval(CommunicationInterval(123), testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }

     @Test
    fun composeGetCommunicationIntervalTest(){
        val testWriterPacket =  MockCharacteristicPacket.mockPacketWriter(
                uint8(Opcode.GET_CGM_COMMUNICATION_INTERVAL.key))
        CgmControlComposer().composeGetCommunicationInterval(testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }

}