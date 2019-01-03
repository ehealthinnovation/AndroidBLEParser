package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat

/**
 * This is history event data for [EventType.MAX_BOLUS_AMOUNT_CHANGED] history event
 *
 * This event should be recorded if the setting of the maximum bolus amount was changed via the
 * Insulin Delivery Device or via a Client (i.e., procedure Set Max Bolus Amount is executed on the
 * IDD Command CP).
 *
 * @property oldAmount the old amount of the max bolus amount in IU
 * @property newAmount the new amount of the max bolus amount in IU
 */
data class MaxBolusAmountChanged(
        val oldAmount: Float,
        val newAmount: Float
)

internal class MaxBolusAmountChangedEventParser : HistoryEventDataParser<MaxBolusAmountChanged>() {

    override fun readData(dataReader: DataReader): MaxBolusAmountChanged {
        return MaxBolusAmountChanged(
                dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT),
                dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        )
    }

}