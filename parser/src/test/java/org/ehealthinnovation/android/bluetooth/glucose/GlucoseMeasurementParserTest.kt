package org.ehealthinnovation.android.bluetooth.glucose

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class GlucoseMeasurementParserTest {


    @Test
    fun canParseSmokeTest(){
        val glucoseUuid = UUID.fromString("00002A18-0000-1000-8000-00805F9B34FB")
        val testPacket1 = MockCharacteristicPacket.mockPacketWithUuid(glucoseUuid)
        Assert.assertTrue(GlucoseMeasurementParser().canParse(testPacket1))

        val glucoseCorruptedUuid = UUID.fromString("00002A35-0000-1000-8000-00805F9B34FB")
        val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(glucoseCorruptedUuid)
        Assert.assertFalse(GlucoseMeasurementParser().canParse(testPacket2))
    }

    @Test
    fun testReadConditions() {
        assertReadConditions(0b00000, false, false, true, false, false)
        assertReadConditions(0b11111, true, true, false, true, true)
        assertReadConditions(0b00001, true, false, true, false, false)
        assertReadConditions(0b00010, false, true, true, false, false)
        assertReadConditions(0b00100, false, false, false, false, false)
        assertReadConditions(0b01000, false, false, true, true, false)
        assertReadConditions(0b10000, false, false, true, false, true)
    }

    fun assertReadConditions(raw8: Int, C1: Boolean, C2: Boolean, C3: Boolean, C5:Boolean, context:Boolean) {
        val EXPECTED = FlagConditions(C1, C2, C3, C5, context)
        val conditions = readFlagConditions(StubDataReader(uint8(raw8)))
        assertEquals(EXPECTED, conditions)
    }

    @Test
    fun testSequenceNumber() {
        val SEQUENCE = 17
        assertEquals(SEQUENCE, readSequenceNumber(StubDataReader(uint16(SEQUENCE))))
    }

    @Test
    fun testReadDate() {
        assertEquals(getSampleDateTime(), readDate(getSampleDateTimeData()))
    }

    @Test
    fun testReadTimeOffset() {
        val OFFSET = 123
        assertEquals(OFFSET, readTimeOffset(StubDataReader(sint16(OFFSET))))
    }

    @Test
    fun testReadUnits() {
        assertEquals(ConcentrationUnits.KGL, readGlucoseUnit(true))
        assertEquals(ConcentrationUnits.MOLL, readGlucoseUnit(false))
    }

    @Test
    fun testReadGlucoseSample() {

        val CONCENTRATION = 5.4f
        val UNITS = ConcentrationUnits.KGL
        val SAMPLE_TYPE = SampleType.CAPILLARY_PLASMA
        val SAMPLE_LOCATION = SampleLocation.EARLOBE

        val sample = org.ehealthinnovation.android.bluetooth.glucose.readGlucoseSample(
                UNITS,
                StubDataReader(sfloat(CONCENTRATION), uint8(SAMPLE_TYPE.key, SAMPLE_LOCATION.key))
        )

        assertEquals(CONCENTRATION, sample.glucoseConcentration)
        assertEquals(UNITS, sample.units)
        assertEquals(SAMPLE_TYPE, sample.sampleType)
        assertEquals(SAMPLE_LOCATION, sample.sampleLocation)
    }

    @Test
    fun testReadSensorFlags() {
        val TEST_FLAGS = EnumSet.of(SensorStatus.BATTERY_LOW, SensorStatus.SAMPLE_ISSUFFICIENT)
        val flags = readSensorFlags(StubDataReader(uint16(0x5)))

        assertEquals(TEST_FLAGS, flags)
    }
}