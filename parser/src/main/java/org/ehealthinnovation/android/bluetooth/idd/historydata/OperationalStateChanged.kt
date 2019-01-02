package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.OperationalState
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration

/**
 * This is the history data for [EventType.OPERATIONAL_STATE_CHANGED] history event
 *
 * This event should be recorded with each change of the operational state of the Insulin Delivery Device.
 *
 * @property oldState the old operational state
 * @property newState the new operational state
 */
data class OperationalStateChangedData(
        val oldState: OperationalState,
        val newState: OperationalState
)

internal class OperationalStateChangedEventParser : HistoryEventDataParser<OperationalStateChangedData>() {

    override fun readData(dataReader: DataReader): OperationalStateChangedData{
        val oldState = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), OperationalState::class.java, OperationalState.RESERVED_FOR_FUTURE_USE)
        val newState = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), OperationalState::class.java, OperationalState.RESERVED_FOR_FUTURE_USE)
        return OperationalStateChangedData(oldState, newState)
    }
}