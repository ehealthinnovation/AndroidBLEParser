package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Event data for reference time in base offset format
 * @property recordingReason  The Recording Reason field specifies the reason why the Reference Time Base Offset event was recorded.
 * @property baseTime The base timestamp
 * @property timeOffsetMinutes the offset from the [baseTime] in minutes.
 */
data class ReferenceTimeBaseOffsetData(
        val recordingReason: RecordingReason,
        val baseTime: BluetoothDateTime,
        val timeOffsetMinutes: Int
)
/**
 * A history event recording a reference time in base offset time format
 * @property eventInfo the info header of the event
 * @property eventData the data that stores the reference time event
 */
data class ReferenceTimeBaseOffsetEvent(override val eventInfo: EventInfo, val eventData: ReferenceTimeBaseOffsetData) : HistoryEvent(eventInfo)

internal class ReferenceTimeBaseOffsetEventParser {

    /**
     * Use this method to parse a [ReferenceTimeBaseOffsetEvent] from [DataReader]
     * @param eventInfo the event information
     */
    internal fun parseEvent(eventInfo: EventInfo, data: DataReader): ReferenceTimeBaseOffsetEvent {
        val eventData = readEventData(data)
        return ReferenceTimeBaseOffsetEvent(eventInfo, eventData)
    }

    internal fun readEventData(data: DataReader): ReferenceTimeBaseOffsetData {
        val recordingReason = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), RecordingReason::class.java, RecordingReason.RESERVED_FOR_FUTURE_USE)
        val dateTime = readDateTime(data)
        val offset = data.getNextInt(IntFormat.FORMAT_SINT16)
        return ReferenceTimeBaseOffsetData(recordingReason, dateTime, offset)
    }

}


