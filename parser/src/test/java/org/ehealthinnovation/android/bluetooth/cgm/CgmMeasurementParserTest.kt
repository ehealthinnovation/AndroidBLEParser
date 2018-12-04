package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.glucose.GlucoseMeasurementParser
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test
import java.util.*

class CgmMeasurementParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002AA7-0000-1000-8000-00805F9B34FB", CgmMeasurementParser()::canParse)
    }

    @Test
    fun parse() {
        //Normal packet
        val testPacket1 = MockCharacteristicPacket.mockPacketForRead(
                uint8(6), uint8(0), sfloat(35.4f), uint16(0x3412))
        val expectedCgmMeasurement1 = CgmMeasurement(
                35.4f, 0x3412, null, null, null)
        Assert.assertEquals(expectedCgmMeasurement1, CgmMeasurementParser().parse(testPacket1))

        //With Annunciation
        val testPacket2 = MockCharacteristicPacket.mockPacketForRead(
                uint8(7), uint8(0x40), sfloat(35.4f), uint16(0x3412), uint8(1))
        val expectedCgmMeasurement2 = CgmMeasurement(
                35.4f, 0x3412, null, null, EnumSet.of(StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED))
        Assert.assertEquals(expectedCgmMeasurement2, CgmMeasurementParser().parse(testPacket2))

        //With trend information
        val testPacket3 = MockCharacteristicPacket.mockPacketForRead(
                uint8(10), uint8(0x63), sfloat(35.4f), uint16(0x3412), uint8(1), uint8(1), sfloat(-1.3f), sfloat(98.1f))
        val expectedCgmMeasurement3 = CgmMeasurement(
                35.4f, 0x3412, -1.3f, 98.1f,
                EnumSet.of(StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED,
                        StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL))
        Assert.assertEquals(expectedCgmMeasurement3, CgmMeasurementParser().parse(testPacket3))


    }

    @Test
    fun readFlagConditions() {
        val testPacket1 = MockCharacteristicPacket.mockPacketForRead(uint8(0))
        val expectedResult1 = CgmMeasurementParser.FlagConditions(false, false, false, false, false)
        Assert.assertEquals(expectedResult1, CgmMeasurementParser().readFlagConditions(testPacket1.readData()))

        val testPacket2 = MockCharacteristicPacket.mockPacketForRead(uint8(0xFF))
        val expectedResult2 = CgmMeasurementParser.FlagConditions(true, true, true, true, true)
        Assert.assertEquals(expectedResult2, CgmMeasurementParser().readFlagConditions(testPacket2.readData()))

        val testPacket3 = MockCharacteristicPacket.mockPacketForRead(uint8(0x22))
        val expectedResult3 = CgmMeasurementParser.FlagConditions(false, true, true, false, false)
        Assert.assertEquals(expectedResult3, CgmMeasurementParser().readFlagConditions(testPacket3.readData()))
    }

    @Test
    fun readSensorStatusAnnunciationSmokeTest() {
        val testFlag1 = CgmMeasurementParser.FlagConditions(false, false, false, false, false)
        val mockPacketInput1 = MockCharacteristicPacket.mockPacketForRead(uint8(0))
        val expectedParsedFlag1 = null
        Assert.assertEquals(expectedParsedFlag1, CgmMeasurementParser().readSensorStatusAnnunciation(testFlag1, mockPacketInput1.readData()))


        val testFlag2 = CgmMeasurementParser.FlagConditions(false, false, true, false, false)
        val mockPacketInput2 = MockCharacteristicPacket.mockPacketForRead(uint8(3))
        val expectedParsedFlag2 = EnumSet.of(StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL, StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL)
        val actualResult2 = CgmMeasurementParser().readSensorStatusAnnunciation(testFlag2, mockPacketInput2.readData())
        Assert.assertEquals(expectedParsedFlag2, actualResult2)

        val testFlag3 = CgmMeasurementParser.FlagConditions(false, false, true, true, true)
        val mockPacketInput3 = MockCharacteristicPacket.mockPacketForRead(uint8(0x01), uint8(0x02), uint8(0x04))
        val expectedParsedFlag3 = EnumSet.of(StatusFlag.SESSION_STOPPED, StatusFlag.CALIBRATION_NOT_ALLOWED, StatusFlag.RESULT_UNDER_HYPO_LEVEL)
        Assert.assertEquals(expectedParsedFlag3, CgmMeasurementParser().readSensorStatusAnnunciation(testFlag3, mockPacketInput3.readData()))
    }

    @Test
    fun readSensorStatusAnnunciationByOctetSmokeTest() {
        val offset1 = 0 //the lower one octet of the status field
        val data1 = MockCharacteristicPacket.mockPacketForRead(uint8(0x01))
        val expectedOutputSet1 = EnumSet.of(StatusFlag.SESSION_STOPPED)
        Assert.assertEquals(expectedOutputSet1, CgmMeasurementParser().readSensorStatusAnnunciationByOctet(offset1, data1.readData()))


        val offset2 = 1//the middle octet of the status field
        val data2 = MockCharacteristicPacket.mockPacketForRead(uint8(0x13))
        val expectedOutputSet2 = EnumSet.of(StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED,
                StatusFlag.CALIBRATION_NOT_ALLOWED, StatusFlag.TEMPERATURE_TOO_HIGH)
        Assert.assertEquals(expectedOutputSet2, CgmMeasurementParser().readSensorStatusAnnunciationByOctet(offset2, data2.readData()))

        val offset3 = 2//the upper octet of the status field
        val data3 = MockCharacteristicPacket.mockPacketForRead(uint8(0x24))
        val expectedOutputSet3 = EnumSet.of(StatusFlag.RESULT_UNDER_HYPO_LEVEL,
                StatusFlag.RATE_OF_INCREASE_EXCEEDED)
        Assert.assertEquals(expectedOutputSet3, CgmMeasurementParser().readSensorStatusAnnunciationByOctet(offset3, data3.readData()))

    }

    @Test(expected = Exception::class)
    fun readSensorStatusAnnunciationByOctetExceptionThrownWhenOffsetOutOfRange() {
        val offset = 3
        val data = MockCharacteristicPacket.mockPacketForRead(uint8(0x01))
        CgmMeasurementParser().readSensorStatusAnnunciationByOctet(offset, data.readData())
    }
}