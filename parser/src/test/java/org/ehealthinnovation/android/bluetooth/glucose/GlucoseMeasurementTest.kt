package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.BluetoothDateTime
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class GlucoseMeasurementTest {

    @Test
    fun testSanity() {
        val SEQUENCE = 6;
        val BASE_DATE = BluetoothDateTime(1972, 5, 4, 17, 6, 55)
        val OFFSET_MINUTES = 5

        val SAMPLE = GlucoseSample(
                5.4f,
                ConcentrationUnits.MOLL,
                SampleType.ARTERIAL_WHOLE_BLOOD,
                SampleLocation.EARLOBE
        )

        val STATUS = EnumSet.of(SensorStatus.TEMPERATURE_TOO_LOW, SensorStatus.STRIP_INSERTION_ERROR)

        val HAS_CONTEXT = true

        val glucose = GlucoseMeasurement(
                SEQUENCE,
                BASE_DATE,
                OFFSET_MINUTES,
                SAMPLE,
                STATUS,
                HAS_CONTEXT)

        assertEquals(SEQUENCE, glucose.sequenceNumber)
        assertEquals(BASE_DATE, glucose.baseTime)
        assertEquals(OFFSET_MINUTES, glucose.timeOffsetMinutes)
        assertEquals(SAMPLE, glucose.glucose)
        assertEquals(STATUS, glucose.sensorStatus)
        assertEquals(HAS_CONTEXT, glucose.hasContext)
    }
}