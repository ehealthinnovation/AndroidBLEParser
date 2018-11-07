package org.ehealthinnovation.android.bluetooth.parser

/**
 * A Bluetooth date time.
 *
 * @property year Optional. Year as defined by the Gregorian calendar, 1582-9999. e.g. 1970.
 *                null if not provided by the device.
 *
 * @property month Optional. Integer month of the year as defined by the Gregorian calendar, 1-12 for January to December.
 *                 null if not provided by the device.
 *
 * @property day Optional. Day of the month as defined by the Gregorian calendar, 1-31.
 *               null if not provided by the device.
 *
 * @property hours Number of hours past midnight, 0-23
 *
 * @property minutes Number of minutes since the start of the hour, 0-59
 *
 * @property seconds Number of seconds since the start of the minute, 0-59
 */
data class BluetoothDateTime internal constructor(
        val year: Int?,
        val month: Int?,
        val day: Int?,
        val hours: Int,
        val minutes: Int,
        val seconds: Int
)

