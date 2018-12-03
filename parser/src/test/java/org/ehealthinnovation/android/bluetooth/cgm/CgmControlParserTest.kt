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

        val testVectors = listOf<Pair<Opcode, AlertGlucoseLevelResponse>>(
                Pair(Opcode.PATIENT_HIGH_ALERT_LEVEL_RESPONSE, PatientHighAlertResponse(54f)),
                Pair(Opcode.PATIENT_LOW_ALERT_LEVEL_RESPONSE, PatientLowAlertResponse(51f)),
                Pair(Opcode.HYPO_ALERT_LEVEL_RESPONSE, HypoAlertResponse(50f)),
                Pair(Opcode.HYPER_ALERT_LEVEL_RESPONSE, HyperAlertResponse(49f))
        )
        for (testVector in testVectors) {
            val testOpcode = testVector.first
            val testReader = StubDataReader(
                    sfloat(testVector.second.concentration)
            )
            val expectedOutput = testVector.second
            Assert.assertEquals(expectedOutput, CgmControlParser().readGetGlucoseAlertLevelResponse(testOpcode, testReader))
        }
    }


    @Test
    fun readGetRateOfChangeAlertLevelResponse() {

        val testVectors = listOf<Pair<Opcode, RateOfChangeAlertResponse>>(
                Pair(Opcode.RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE, RateOfDecreaseAlertResponse(54f)),
                Pair(Opcode.RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE, RateOfIncreaseAlertResponse(58f))
        )
        for (testVector in testVectors) {
            val testOpcode = testVector.first
            val testReader = StubDataReader(
                    sfloat(testVector.second.rateOfChange)
            )
            val expectedOutput = testVector.second
            Assert.assertEquals(expectedOutput, CgmControlParser().readRateOfChangeAlertLevelResponse(testOpcode, testReader))
        }
    }



}