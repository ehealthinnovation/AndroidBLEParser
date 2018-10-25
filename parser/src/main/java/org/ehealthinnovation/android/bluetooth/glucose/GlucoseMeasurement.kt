package org.ehealthinnovation.android.bluetooth.glucose

import org.apache.commons.lang3.time.DateUtils
import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import java.util.*

/**
 * A glucose concentration sampled at a specific time by a glucose measuring sensor.
 *
 * @property sequenceNumber Measurements with earlier [sequenceNumber] values can be considered as
 *                          sampled prior to measurements with later values. Unknown validity duration.
 *
 * @property baseTime Combine this timestamp with [timeOffsetMinutes] to determine the originating
 *                    timestamp of this measurement.
 *
 * @property timeOffsetMinutes Optional. The offset in minutes from the [baseTime] of this measurement.
 *
 * @property timestamp The total timestamp of the measurement.
 *
 * @property glucose Optional. The sampled glucose values. Is only present if this characteristic is being
 *                  used to communicate a sample value. For example, when being used to send an explicit error status,
 *                  the glucose sample may be null. Otherwise may be missing for fun.
 *
 * @property sensorStatus Optional. A collection of one or more flags indicating the status of the sensor that made this measurement.
 *                        If null it means the device was unable to provide any sensor status.
 *                        If non-null but empty, it means no abnormal states and the sensor is reporting all ok.
 *
 * @property hasContext If true, there is a subsequent [GlucoseContext] characteristic with a matching [sequenceNumber]
 *                      that describes the context for this measurement.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_measurement.xml
 */
class GlucoseMeasurement(
        val sequenceNumber: Int,
        private val baseTime: Date,
        private val timeOffsetMinutes: Int?,
        val glucose: GlucoseSample?,
        val sensorStatus: EnumSet<SensorStatus>?,
        val hasContext: Boolean
) {
    val timestamp: Date = offsetDate(baseTime, timeOffsetMinutes)
}

/**
 * @return the final timestamp based on the base [Date] and an optional [offsetMinutes]
 */
fun offsetDate(base: Date, offsetMinutes: Int?) =
    DateUtils.addMinutes(base, offsetMinutes?: 0)

/**
 * The sampled glucose values of a [GlucoseMeasurement].
 *
 * @property glucoseConcentration The glucose concentration value of this sample in [units].
 *
 * @property units The unit of measurement for the [concentration]. Either mol/L or kg/L.
 *
 * @property sampleType The sampleType of fluid that was sampled to determine this measurement.
 *
 * @property sampleLocation The location that was sampled to determine this measurement.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_measurement.xml
 */
data class GlucoseSample(
        val glucoseConcentration: Float,
        val units: ConcentrationUnits,
        val sampleType: SampleType,
        val sampleLocation: SampleLocation
)

/**
 * Information flags for a [GlucoseMeasurement].
 */
enum class MeasurementFlag(override val bitOffset: Int) : FlagEnum {
    TIME_OFFSET_PRESENT(0),
    GLUCOSE_CONCENTRATION_TYPE_SAMPLE_LOCATION_PRESENT(1),
    GLUCOSE_CONCENTRATION_UNITS(2),
    SENSOR_STATUS_ANNUNCIATION_PRESENT(3),
    CONTEXT_INFORMATION_FOLLOWS(4)
}

/**
 * The units of measurements of [GlucoseSample.glucoseConcentration].
 */
enum class ConcentrationUnits {
    /**
     * kg/L
     */
    KGL,

    /**
     * mol/L
     */
    MOLL
}

/**
 * The type of fluid used to sample the glucose in [GlucoseSample].
 *
 * @property key the key value of the sample type.
 */
enum class SampleType(override val key: Int) : EnumerationValue {
    CAPILLARY_WHOLE_BLOOD(1),
    CAPILLARY_PLASMA(2),
    VENOUS_WHOLE_BLOOD(3),
    VENOUS_PLASMA(4),
    ARTERIAL_WHOLE_BLOOD(5),
    ARTERIAL_PLASMA(6),
    UNDETERMINED_WHOLE_BLOOD(7),
    UNDETERMINED_PLASMA(8),
    INTERSTITIAL_FLUID(9),
    CONTROL_SOLUTION(10),
    RESERVED_FOR_FUTURE(-1)
}

/**
 * The location used to sample the glucose in [GlucoseSample].
 *
 * @property key The key value of the location type.
 */
enum class SampleLocation(override val key: Int) : EnumerationValue  {
    FINGER(1),
    ALTERNATE_SITE_TEST(2),
    EARLOBE(3),
    CONTROL_SOLUTION(4),
    LOCATION_NOT_AVAILABLE(15),
    RESERVED_FOR_FUTURE(-1)
}

/**
 * Bit flags for sensor status associated with a [GlucoseMeasurement]
 *
 * @property bitOffset The offset used to represent this flag.
 */
enum class SensorStatus(override val bitOffset: Int) : FlagEnum {
    /**
     * Sensor malfunction or faulting at time of measurement
     */
    SENSOR_MALFUNCTION(1),

    /**
     * Sample size for blood or control solution insufficient at time of measurement.
     */
    SAMPLE_ISSUFFICIENT(2),

    /**
     * Strip insertion error.
     */
    STRIP_INSERTION_ERROR(3),

    /**
     * Strip type incorrect for device.
     */
    STRIP_INCORRECT(4),

    /**
     * Sensor result higher than the device can process.
     */
    RESULT_TOO_HIGH(5),

    /**
     * Sensor result lower than the device can process.
     */
    RESULT_TOO_LOW(6),

    /**
     * Sensor temperature too high for valid test/result at time of measurement.
     */
    TEMPERATURE_TOO_HIGH(7),

    /**
     * Sensor temperature too low for valid test/result at time of measurement.
     */
    TEMPERATURE_TOO_LOW(8),

    /**
     * Sensor read interrupted because strip was pulled too soon at time of measurement.
     */
    READ_INTERRUPTED(9),

    /**
     * General device fault has occurred in the sensor.
     */
    GENERAL_DEVICE_FAULT(10),

    /**
     * Time fault has occurred in the sensor and time may be inaccurate.
     */
    TIME_FAULT(11),

    /**
     * Reserved for future use.
     */
    RESERVED_FOR_FUTURE(12)
}