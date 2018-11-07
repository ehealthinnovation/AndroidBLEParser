package org.ehealthinnovation.android.bluetooth.parser

import java.util.*


class BluetoothDataTimeUtility {

    companion object Utility {
        /**
         * Tests if [startDate] is before [endDate]
         */
        fun isValidDateRange(startDate: Date, endDate: Date): Boolean = startDate.before(endDate)


        /**
         * Converts a [Date] into [BluetoothDateTime]
         */
        fun convertDateToBluetoothDateTime(date: Date): BluetoothDateTime {
            val calendar = Calendar.getInstance()
            calendar.time = date
            return BluetoothDateTime(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1, //calendar month starts at 0, while bluetooth datetime starts at 1
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND)
            )
        }


        /**
         * Test if the date field meets the Bluetooth DateTime Specification
         */
        fun isYearValid(year: Int): Boolean = (year in 1582..9999)
        fun isMonthValid(month: Int): Boolean = (month in 1..12)
        fun isDayValid(day: Int): Boolean = (day in 1..31)
        fun isHourValid(hour: Int): Boolean = (hour in 0..23)
        fun isMinuteValid(minute: Int): Boolean = (minute in 0..59)
        fun isSecondValid(second: Int): Boolean = (second in 0..59)


        /**
         * Create a [BluetoothDateTime] and perform sanity check on the field values.
         *
         * @see [BluetoothDateTime] for the details of each input argument field.
         */
        fun createBluetoothDateTime(year: Int, month: Int, day: Int, hours: Int, minutes: Int, seconds: Int): BluetoothDateTime {

            BluetoothDataTimeUtility.run {
                if (isYearValid(year) &&
                        isMonthValid(month) &&
                        isDayValid(day) &&
                        isHourValid(hours) &&
                        isMinuteValid(minutes) &&
                        isSecondValid(seconds)) {
                    return BluetoothDateTime(year, month, day, hours, minutes, seconds)
                } else {
                    throw IllegalArgumentException("Cannot create Bluetooth DateTime. Some field is not valid.")
                }
            }
        }

    }


}

