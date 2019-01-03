package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat

/**
 * This is event data for [EventType.PRIMING_STARTED] history event
 *
 * This event should be recorded if the priming of the Insulin Delivery Device is started (e.g., by
 * executing the Start Priming procedure on the IDD Command CP
 *
 * @property value the priming amount in IU
 */
data class PrimingStarted(
        val value: Float
)

internal class PrimingStartedEventParser : HistoryEventDataParser<PrimingStarted>() {
    override fun readData(dataReader: DataReader): PrimingStarted {
        return PrimingStarted(dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT))
    }
}

