package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TherapyControlState
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration

/**
 * History event data for [EventType.THERAPY_CONTROL_STATE_CHANGED] history event.
 *
 * This event should be recorded with each change of the therapy control state of the Insulin
 * Delivery Device.
 *
 * @property oldState the old therapy control state
 * @property newState the new therapy control state
 */
data class TherapyControlStateChangedData(
        val oldState : TherapyControlState,
        val newState : TherapyControlState
)

internal class TherapyControlStateChangedEventParser : HistoryEventDataParser<TherapyControlStateChangedData>() {
    override fun readData(dataReader: DataReader): TherapyControlStateChangedData{
        val oldState = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TherapyControlState::class.java, TherapyControlState.RESERVED_FOR_FUTURE_USE)
        val newState = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TherapyControlState::class.java, TherapyControlState.RESERVED_FOR_FUTURE_USE)
        return TherapyControlStateChangedData(oldState, newState)
    }
}
