package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import java.util.*

/**
 * Indicate the functionality of a cgm device.
 *
 * @property supportedFeatures A set of flags that indicates functions supported by a cgm.
 * @property sampleType The fluid sample to estimate the glucose level
 * @property sampleLocation The location a cgm is attached to
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_feature.xml
 *
 */
data class CgmFeature(
        val supportedFeature: EnumSet<Feature>,
        val sampleType: SampleType,
        val sampleLocation: SampleLocation
)

/**
 * Enumeration of supported feature flags.
 *
 * @property bitOffset The offset used to represent this flag.
 */
enum class Feature(override val bitOffset: Int) : FlagValue {
    RESERVE_FOR_FUTURE(-1),
    CALIBRATION(0),
    PATIENT_HIGH_LOW_ALERTS(1),
    HYPO_ALERTS(2),
    HYPER_ALERTS(3),
    RATE_OF_INCREASE_DECREASE_ALERTS(4),
    DEVICE_SPECIFIC_ALERT(5),
    SENSOR_MALFUNCTION_DETECTION(6),
    SENSOR_TEMPERATURE_HIGH_LOW_DETECTION(7),
    SENSOR_RESULT_HIGH_LOW_DETECTION(8),
    LOW_BATTERY_DETECTION(9),
    SENSOR_TYPE_ERROR_DETECTION(10),
    GENERAL_DEVICE_FAULT(11),
    E2E_CRC(12),
    MULTIPLE_BOND(13),
    MULTIPLE_SESSIONS(14),
    CGM_TREND_INFORMATION(15),
    CGM_QUALITY(16),

}

/**
 * The type of fluid used to sample the glucose.
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
 * The location used to sample the glucose.
 *
 * @property key The key value of the location type.
 */
enum class SampleLocation(override val key: Int) : EnumerationValue {
    FINGER(1),
    ALTERNATE_SITE_TEST(2),
    EARLOBE(3),
    CONTROL_SOLUTION(4),
    SUBCUTANEOUS_TISSUE(5),
    LOCATION_NOT_AVAILABLE(15),
    RESERVED_FOR_FUTURE(-1)
}

