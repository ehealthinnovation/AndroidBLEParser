package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.readDst
import org.ehealthinnovation.android.bluetooth.parser.readTimeZone
import org.ehealthinnovation.android.bluetooth.parser.*


/**
 *The Reference Time event is an absolute time stamp referenced by all event types by their Relative
 *Offset field. This event shall be used by the Server if the Insulin Delivery Device uses absolute
 * time (i.e., in that case, the Reference Time Base Offset event shall not be used).
 *
 *It should be recorded: 1. As very first event in history (e.g., factory setting) 2. If the Date
 * Time, Time Zone, or Daylight Saving Time (DST) Offset of the Insulin Delivery Device is changed.
 * 3. If the data type limit of the Relative Offset field is reached (i.e., each 65535 s at the latest)
 *
 * This event should be recorded periodically (e.g., each full hour).
 *
 * @property recordReason
 */
data class ReferenceTime(
        val recordReason: RecordingReason,
        val timeStamp: BluetoothDateTime,
        val timeZone: BluetoothTimeZone,
        val dstOffset: DstOffset
)

/**
 * This enum represent the reason for recording a [ReferenceTime] and [ReferenceTimeBaseOffset]
 */
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

/**
 * Use this parser when a IDD History Data Control Point returns data buffer containing event type
 * opcode [EventType.REFERENCE_TIME]. See [HistoryEventDataParser.parseEvent] for details of using
 * the parser.
 */
internal class ReferenceTimeEventParser: HistoryEventDataParser<ReferenceTime>() {

    override fun readData(data: DataReader): ReferenceTime {
        val recordingReason = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), RecordingReason::class.java, RecordingReason.RESERVED_FOR_FUTURE_USE)
        val dateTime = readDateTime(data)
        val timezone = readTimeZone(data)
        val dstOffset = readDst(data)
        return ReferenceTime(recordingReason, dateTime, timezone, dstOffset)
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



