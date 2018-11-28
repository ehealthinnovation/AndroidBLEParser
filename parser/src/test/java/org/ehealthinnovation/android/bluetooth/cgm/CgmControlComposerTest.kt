package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Test
import java.util.*

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

    @Test(expected = Exception::class)
    fun creatingInvalidCommunicationInterval() {
        CommunicationInterval(256)
    }


    @Test
    fun creatingValidCommunicationInterval() {
        CommunicationInterval(255)
    }

    @Test
    fun composeSetCommunicationIntervalTest() {
        val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(
                uint8(Opcode.SET_CGM_COMMUNICATION_INTERVAL.key),
                uint8(123))
        CgmControlComposer().composeSetCommunicationInterval(CommunicationInterval(123), testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }

    @Test
    fun composeGetCommunicationIntervalTest() {
        val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(
                uint8(Opcode.GET_CGM_COMMUNICATION_INTERVAL.key))
        CgmControlComposer().composeGetCommunicationInterval(testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }

    @Test
    fun composeSetCalibrationTest() {
        val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(
                uint8(Opcode.SET_GLUCOSE_CALIBRATION_VALUE.key),
                sfloate(57f, 0),//concentration
                uint16(513),//calibration time
                uint8(SampleType.INTERSTITIAL_FLUID.key, SampleLocation.ALTERNATE_SITE_TEST.key),
                uint16(1027),//next calibration time
                uint16(5), //calibration record number
                uint8(4) //EnumSet of calibration pending flag
        )
        val inputRecord = CalibrationRecord(
                57f, 513, SampleType.INTERSTITIAL_FLUID, SampleLocation.ALTERNATE_SITE_TEST, 1027, 5, EnumSet.of(CalibrationStatus.PROCESS_PENDING))
        CgmControlComposer().composeSetCalibration(inputRecord, testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
    }

}