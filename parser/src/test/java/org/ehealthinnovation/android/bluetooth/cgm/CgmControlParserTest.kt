package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*
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
        val expectedResult = CgmControlGenericResponse(Opcode.START_THE_SESSION, ResponseCode.SUCCESS)
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

    @Test
    fun readCalibrationRecordResponse() {
        val testReader = StubDataReader(
                sfloat(57f),//concentration
                uint16(513),//calibration time
                uint8(SampleType.INTERSTITIAL_FLUID.key, SampleLocation.ALTERNATE_SITE_TEST.key),
                uint16(1027),//next calibration time
                uint16(5), //calibration record number
                uint8(4) //EnumSet of calibration pending flag
        )
        val expectedResponseRecord = CalibrationRecordResponse(
                57f,
                513,
                SampleType.INTERSTITIAL_FLUID,
                SampleLocation.ALTERNATE_SITE_TEST,
                1027,
                5,
                EnumSet.of(CalibrationStatus.PROCESS_PENDING))
        Assert.assertEquals(expectedResponseRecord, CgmControlParser().readCalibrationRecordResponse(testReader))
    }

    @Test
    fun readGetGlucoseAlertLevelResponse() {
        val testOpcode1 = Opcode.PATIENT_HIGH_ALERT_LEVEL_RESPONSE
        val testReader1 = StubDataReader(
                sfloat(56f)
        )
        val expectedOutput = PatientHighAlertResponse(56f)
        Assert.assertEquals(expectedOutput, CgmControlParser().readGetGlucoseAlertLevelResponse(testOpcode1, testReader1))

        val testOpcode2 = Opcode.PATIENT_LOW_ALERT_LEVEL_RESPONSE
        val testReader2 = StubDataReader(
                sfloat(51f)
        )
        val expectedOutput2 = PatientLowAlertResponse(51f)
        Assert.assertEquals(expectedOutput2, CgmControlParser().readGetGlucoseAlertLevelResponse(testOpcode2, testReader2))

    }


}