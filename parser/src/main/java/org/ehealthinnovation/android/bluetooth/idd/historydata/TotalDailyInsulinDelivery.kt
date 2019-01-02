package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Data for [TotalDailyInsulinDeliveryEvent]
 *
 * This event should be recorded at midnight determined by the date time of the Insulin Delivery Device.
 * Note: The time stamp of this event may have another date than the day the event is recorded for
 * (i.e., the patient changed the date of the Insulin Delivery Device). If the date time was changed and this event is recorded, the Server shall set the Date Time Changed Warning bit of the Flags field to True.
 *
 * @property sumOfBasalDeliveredIU the total daily basal delivered in IU.
 * @property sumOfBolusDeliveredIU the total daily bolus delivered in IU.
 * @property year the date information of the event.
 * @property month the month information of the event.
 * @property day the day information of the event.
 * @property dateTimeChangedWarning indicate whether the user has change the device date during the day, which may affect the accuracy of the result.
 */
data class TotalDailyInsulinDelivery(
        val sumOfBolusDeliveredIU: Float,
        val sumOfBasalDeliveredIU: Float,
        val year: Int,
        val month: Int,
        val day: Int,
        val dateTimeChangedWarning: Boolean
)

internal class TotalDailyInsulinDeliveryEventParser : HistoryEventDataParser<TotalDailyInsulinDelivery>() {
    override fun readData(dataReader: DataReader): TotalDailyInsulinDelivery {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val bolus = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val basal = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val year = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val month = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val day = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        return TotalDailyInsulinDelivery(
                bolus, basal, year, month, day,
                flags.contains(Flag.DATE_TIME_CHANGED_WARNING)
        )
    }

    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the date time of the Insulin Delivery Device will have changed since the last recorded Total Daily Insulin Delivery Even*/
        DATE_TIME_CHANGED_WARNING(0);
    }
}

