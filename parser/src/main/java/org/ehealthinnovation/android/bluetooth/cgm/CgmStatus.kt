package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import java.util.*

/**
 * The characteristic indicate the CGM sensor status
 * @property timeOffsetMinutes the relative time offset to the Cgm Session Start Time in minutes
 * @property status A group of flags that indicate any outstanding abnormality of the sensor
 */
data class CgmStatus(
        val timeOffsetMinutes: Int,
        val status: EnumSet<StatusFlag>
)

/**
 * Flags that indicate abnormal cgm sensor status
 */
enum class StatusFlag(override val bitOffset: Int) : FlagValue {
    SESSION_STOPPED(0),
    DEVICE_BATTERY_LOW(1),
    SENSOR_TYPE_INCORRECT_FOR_DEVICE(2),
    SENSOR_MALFUNCTION(3),
    DEVICE_SPECIFIC_ALERT(4),
    GENERAL_DEVICE_FAULT_HAS_OCCURRED_IN_THE_SENSOR(5),
    TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED(8),
    CALIBRATION_NOT_ALLOWED(9),
    CALIBRATION_RECOMMENDED(10),
    CALIBRATION_REQUIRED(11),
    /* Sensor temperature too high/low at the time of measurement*/
    TEMPERATURE_TOO_HIGH(12),
    TEMPERATURE_TOO_LOW(13),
    /* Sensor result higher/lower than patient defined high/low level*/
    RESULT_UNDER_PATIENT_LOW_LEVEL(16),
    RESULT_ABOVE_PATIENT_HIGH_LEVEL(17),
    /* Sensor result higher/lower than clinically defined hyperglycemia/hypoglycemia levels*/
    RESULT_UNDER_HYPO_LEVEL(18),
    RESULT_ABOVE_HYPER_LEVEL(19),
    /* Sensor senses the rate of increase/decrease exceeds predefined levels*/
    RATE_OF_DECREASE_EXCEEDED(20),
    RATE_OF_INCREASE_EXCEEDED(21),
    /* The sample result is beyond the sensor processing range*/
    RESULT_LOWER_THAN_THE_DEVICE_LIMIT(22),
    RESULT_HIGHER_THAN_THE_DEVICE_LIMIT(23);
}