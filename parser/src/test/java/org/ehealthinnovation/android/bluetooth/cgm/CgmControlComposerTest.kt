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

    @Test
    fun composeSetAlertLevelSmokeTest() {
        //Test Vector has format in the pair : first - the expected output opcode, second - the command request for setting glucose alert level
        val testVectors = listOf<Pair<Opcode, SetGlucoseAlertLevel>>(
                Pair(Opcode.SET_PATIENT_HIGH_ALERT_LEVEL, SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.PATIENT_HIGH_ALERT, 57f))),
                Pair(Opcode.SET_PATIENT_LOW_ALERT_LEVEL, SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.PATIENT_LOW_ALERT, 57f))),
                Pair(Opcode.SET_HYPO_ALERT_LEVEL, SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.HYPO_ALERT, 57f))),
                Pair(Opcode.SET_HYPER_ALERT_LEVEL, SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.HYPER_ALERT, 57f))),
                Pair(Opcode.SET_RATE_OF_DECREASE_ALERT_LEVEL, SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.RATE_OF_DECREASE, 57f))),
                Pair(Opcode.SET_RATE_OF_INCREASE_ALERT_LEVEL, SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.RATE_OF_INCREASE, 57f)))
        )
        for (testVector in testVectors) {
            val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(
                    uint8(testVector.first.key),
                    sfloate(testVector.second.operand.concentration, 0)
            )
            val setCommandRequest = testVector.second
            CgmControlComposer().composeSetGlucoseAlertLevel(setCommandRequest, testWriterPacket.writeData())
            (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()
        }
    }

    @Test
    fun composeGetGlucoseAlertLevelSmokeTest() {
        //Test Vector has format in the pair : first - the expected output opcode, second - the command request for getting glucose alert level
        val testVectors = listOf<Pair<Opcode, GetGlucoseAlertLevel>>(
                Pair(Opcode.GET_PATIENT_HIGH_ALERT_LEVEL, GetGlucoseAlertLevel(GlucoseAlertType.PATIENT_HIGH_ALERT)),
                Pair(Opcode.GET_PATIENT_LOW_ALERT_LEVEL, GetGlucoseAlertLevel(GlucoseAlertType.PATIENT_LOW_ALERT)),
                Pair(Opcode.GET_HYPO_ALERT_LEVEL, GetGlucoseAlertLevel(GlucoseAlertType.HYPO_ALERT)),
                Pair(Opcode.GET_HYPER_ALERT_LEVEL, GetGlucoseAlertLevel(GlucoseAlertType.HYPER_ALERT)),
                Pair(Opcode.GET_RATE_OF_DECREASE_ALERT_LEVEL, GetGlucoseAlertLevel(GlucoseAlertType.RATE_OF_DECREASE)),
                Pair(Opcode.GET_RATE_OF_INCREASE_ALERT_LEVEL, GetGlucoseAlertLevel(GlucoseAlertType.RATE_OF_INCREASE))
        )

        for (testVector in testVectors) {
            val testWriter = StubDataWriter(uint8(testVector.first.key))
            val inputRequest = testVector.second
            CgmControlComposer().composeGetGlucoseAlertLevel(inputRequest, testWriter)
            testWriter.checkWriteComplete()
        }
    }

    @Test
    fun integrationTestOfComposeMethod() {
        val testWriterPacket = MockCharacteristicPacket.mockPacketWriter(
                uint8(Opcode.SET_PATIENT_HIGH_ALERT_LEVEL.key),
                sfloate(57f, 0)
        )
        val setPatientHighCommand = SetGlucoseAlertLevel(GlucoseAlertLevel(GlucoseAlertType.PATIENT_HIGH_ALERT, 57f))
        CgmControlComposer().compose(setPatientHighCommand, testWriterPacket.writeData())
        (testWriterPacket.writeData() as StubDataWriter).checkWriteComplete()

    }

    @Test
    fun composeDeviceSpecificAlertReset(){
        val testWriter = StubDataWriter(uint8(Opcode.RESET_DEVICE_SPECIFIC_ALERT.key))
        CgmControlComposer().composeResetDeviceSpecificAlert(testWriter)
        testWriter.checkWriteComplete()
    }

}