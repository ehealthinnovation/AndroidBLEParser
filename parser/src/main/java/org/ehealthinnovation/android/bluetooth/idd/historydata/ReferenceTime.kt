package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.readDst
import org.ehealthinnovation.android.bluetooth.parser.readTimeZone
import org.ehealthinnovation.android.bluetooth.parser.*


//todo  docs
data class ReferenceTimeData(
        val recordReason: RecordingReason,
        val timeStamp: BluetoothDateTime,
        val timeZone: BluetoothTimeZone,
        val dstOffset: DstOffset
)


//todo  doc
internal class ReferenceTimeEventParser {

    /**
     * Use this method to parse a [ReferenceTimeEvent] from [DataReader]
     * @param eventInfo the event information
     */
    internal fun parseEvent(eventInfo: EventInfo, data: DataReader): HistoryEvent<ReferenceTimeData> {
        val eventData = readEventData(data)
        return HistoryEvent(eventInfo, eventData)
    }

    internal fun readEventData(data: DataReader): ReferenceTimeData {
        val recordingReason = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), RecordingReason::class.java, RecordingReason.RESERVED_FOR_FUTURE_USE)
        val dateTime = readDateTime(data)
        val timezone = readTimeZone(data)
        val dstOffset = readDst(data)
        return ReferenceTimeData(recordingReason, dateTime, timezone, dstOffset)
    }
}

/**
 * Parse the event information from [DataReader]
 */
internal fun readEventInfo(data: DataReader): EventInfo {
    val eventType = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT16), EventType::class.java, EventType.RESERVED_FOR_FUTURE_USE)
    val sequenceNumber = data.getNextInt(IntFormat.FORMAT_UINT32)
    val relativeOffset = data.getNextInt(IntFormat.FORMAT_UINT16)
    return EventInfo(eventType, sequenceNumber, relativeOffset)
}

enum class RecordingReason(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    /**The reason is undetermined. */
    UNDETERMINED(0x0F),
    /**The date time of the Insulin Delivery Device was changed (i.e., at least one of the fields – Date Time, Time Zone, or DST Offset changed). */
    SET_DATE_TIME(0x33),
    /**The Reference Time event was recorded periodically (e.g., at each full hour). */
    PERIODIC_RECORDING(0x3C),
    /**The Insulin Delivery Device lost its date time (e.g., due to battery replacement).That is, the date time of the device is set to its default value and the Relative Offset field of the Reference Time event is 0. */
    DATE_TIME_LOSS(0x55);

}
