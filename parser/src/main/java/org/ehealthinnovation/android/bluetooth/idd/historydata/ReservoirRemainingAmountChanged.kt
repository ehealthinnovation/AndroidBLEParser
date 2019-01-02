package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat

/**
 * The history event data for [EventType.RESERVOIR_REMAINING_AMOUNT_CHANGED]
 *
 * This event should be recorded with each change of the remaining amount of the reservoir. The
 * frequency of recording this event is implementation specific.
 *
 * @property amount the amount of reservoir remaining amount in IU
 */
data class ReservoirRemainingAmount(
        val amount: Float
)

internal class ReservoirRemainingAmountChangedParser : HistoryEventDataParser<ReservoirRemainingAmount>() {
    override fun readData(dataReader: DataReader): ReservoirRemainingAmount {
        val amount = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        return ReservoirRemainingAmount(amount)
    }

}