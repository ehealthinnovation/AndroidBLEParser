package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import java.util.*

/**
 * A CGM measurement sample point
 *
 * @property concentration The glucose level estimate by the CGM, measure in mg/dL
 *
 * @property timeOffsetMinute The time offset from the session start time the sample is taken
 *
 * @property rateOfChange If not null, it provides the rate of change in glucose concentration,
 * measured in mg/dL/min
 *
 * @property qualityPercentage If not null, it provides the a measure of quality about the glucose
 * estimation in percentage. The statistical meaning of this indicator manufacturer specific.
 *
 * @property sensorAnnunciation If not null, it contains a set of [StatusFlag] that indicate sensor
 * status. Usually it alerts sensor abnormality. When interpreting a [CgmMeasurement] check the
 * [sensorAnnunciation] first to make sure the device is working normally.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_measurement.xml
 */
data class CgmMeasurement(
        val concentration: Float,
        val timeOffsetMinute: Int,
        val rateOfChange: Float?,
        val qualityPercentage: Float?,
        val sensorAnnunciation: EnumSet<StatusFlag>?
)

enum class MeasurementFlag(override val bitOffset: Int) : FlagValue {
    TREND_INFORMATION(0),
    QUALITY(1),
    SENSOR_STATUS_ANNUNCIATION_FIELD_WARNING_OCTET(5),
    SENSOR_STATUS_ANNUNCIATION_FIELD_CALTEMP_OCTET(6),
    SENSOR_STATUS_ANNUNCIATION_FIELD_STATUS_OCTET(7);
}



