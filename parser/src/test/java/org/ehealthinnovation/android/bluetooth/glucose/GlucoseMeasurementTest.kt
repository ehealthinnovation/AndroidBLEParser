package org.ehealthinnovation.android.bluetooth.glucose

import org.junit.Assert
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class GlucoseMeasurementTest {

    private val BASE_TIME_MS: Long = 10000
    private val OFFSET_MINUTES = 5

    private val BASE_DATE = Date(BASE_TIME_MS)
    private val OFFSET_DATE = Date(BASE_TIME_MS + TimeUnit.MINUTES.toMillis(OFFSET_MINUTES.toLong()))

    /**
     * Test that we can calculate a timestamp from a base and an offset.
     */
    @Test
    fun testGetTimestampOffset() {
        Assert.assertEquals(OFFSET_DATE, offsetDate(BASE_DATE, OFFSET_MINUTES))
    }

    /**
     * Test that we can calculate a timestamp from a base and a null offset.
     */
    @Test
    fun testGetTimestampNoOffset() {
        Assert.assertEquals(BASE_DATE, offsetDate(BASE_DATE, null))
    }

    /**
     * Test that the glucose measure
     */
    @Test
    fun testGlucoseMeasurementOffset() {
        val glucose = GlucoseMeasurement(0, BASE_DATE, OFFSET_MINUTES, null, EnumSet.noneOf(SensorStatus::class.java))
        Assert.assertEquals(OFFSET_DATE, glucose.timestamp)
    }
}