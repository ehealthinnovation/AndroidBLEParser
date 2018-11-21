package org.ehealthinnovation.android.bluetooth.cgm

/**
 * This is a characteristic to define the limit of time span that a CGM device is approved to run.
 *
 * When a device has been running beyond the [sessionRunTime], the reliability of the device could
 * deteriorate due to physiological effects.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_session_run_time.xml
 */
data class CgmSessionRunTime (
        val sessionRunTime: Int
)