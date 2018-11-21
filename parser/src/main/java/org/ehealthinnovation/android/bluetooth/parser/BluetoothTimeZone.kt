package org.ehealthinnovation.android.bluetooth.parser

/**
 * The timezone with [timeZoneValue] defined as the offset from UTC in 15 minutes increments. As an
 * example [timeZoneValue] = 4 means UCT+1 (4 * 15 min = 60 min)
 *
 * Create a [BluetoothTimeZone] by invoking [BluetoothTimeZoneUtility.createTimeZone] which performs
 * a validity test on the input argument.
 *
 * Note that the DST offset is not included in the time zone offset calculation.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.time_zone.xml
 */
data class BluetoothTimeZone internal constructor(
    val timeZoneValue: Int
)
