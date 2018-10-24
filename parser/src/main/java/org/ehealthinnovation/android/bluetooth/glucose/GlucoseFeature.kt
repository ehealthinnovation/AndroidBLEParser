package org.ehealthinnovation.android.bluetooth.glucose

import java.util.*

/**
 * Indicate the functionality of a glucose device.
 *
 * @property supportedFeatures A set of flags that indicates functions supported by a glucose meter. This flag field is mandatory in a glucose packet
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_feature.xml
 *
 */
data class GlucoseFeature(
        val supportedFeatures: EnumSet<Feature>
)


/**
 * Enumeration of supported feature flags
 *
 * @property bitOffset The offset used to represent this flag.
 */
enum class Feature(override val bitOffset: Int) : FlagEnum {

    /**Low Battery Detection During Measurement Supported
     */
    LOW_BATTERY_DETECTION(0),
    /** Sensor Malfunction Detection Supported
     */
    SENSOR_MALFUNCTION_DETECTION(1),
    /** Sensor Sample Size Supported
     */
    SENSOR_SAMPLE_SIZE(2),
    /** Sensor Strip Insertion Error Detection Supported
     */
    SENSOR_STRIP_INSERTION_ERROR_DETECTION(3),
    /** Sensor Strip Type Error Detection Supported
     */
    SENSOR_STRIP_TYPE_ERROR_DETECTION(4),
    /** Sensor Result High Low Detection Supported
     */
    SENSOR_RESULT_HIGH_LOW_DETECTION_SUPPORTED(5),
    /** Sensor Temperature High Low Detection Supported
     */
    SENSOR_TEMPERATURE_WARNING_DETECTION(6),
    /** Sensor Read Interrupt Detection Supported
     */
    SENSOR_READ_INTERRUPT_DETECTION(7),
    /** General Device Fault Supported
     */
    GENERAL_DEVICE_FAULT_DETECTION(8),
    /** Time Fault Supported
     */
    TIME_FAULT_DETECTION(9),
    /** Multiple Bond Supported
     */
    MULTIPLE_BONDS(10),
    /**
     * Reserved for future use
     */
    RESERVED_FOR_FUTURE_USE(-1);
}