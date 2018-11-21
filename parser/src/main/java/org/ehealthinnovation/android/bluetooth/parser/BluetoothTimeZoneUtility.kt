package org.ehealthinnovation.android.bluetooth.parser

class BluetoothTimeZoneUtility {

    companion object {
        val TIMEZONE_UNKNOWN = -128

        /**
         * Check if a timezone meets the value range defined in the Bluetooth Specification
         * This function is invoked when [createTimeZone] is called.
         *
         * @param timezoneValue the input value
         */
        fun isTimeZoneValueValid(timezoneValue: Int): Boolean = ((timezoneValue in -48..56) || (timezoneValue == TIMEZONE_UNKNOWN))


        /**
         * Create a [BluetoothTimeZone].
         *
         * @param timezoneValue a signed integer in range from -48 to 56, which
         * represents the multiple of 15 minutes offset from UTC. Input -128 if timezone is unknown.
         *
         * @return a BluetoothTimezone object
         */
        fun createTimeZone(timezoneValue: Int): BluetoothTimeZone {
            if (isTimeZoneValueValid(timezoneValue)) {
                return BluetoothTimeZone(timezoneValue)
            } else {
                throw IllegalArgumentException("timezone value $timezoneValue not valid")
            }
        }

        /**
         * Convert a [BluetoothTimeZone] into the exact minutes offset from UTC
         * @return minutes from UTC time. null if timezone is unknown
         */
        fun getTimeZoneOffsetFromUTCInMinute(timezone: BluetoothTimeZone): Int? =
                when (timezone.timeZoneValue) {
                    BluetoothTimeZoneUtility.TIMEZONE_UNKNOWN -> null
                    else -> 15 * timezone.timeZoneValue
                }
    }
}