package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * The Reference Time Base Offset event is a Base Offset time stamp referenced by all event types by
 * their Relative Offset field. This event shall be used by the Server if the Insulin Delivery Device uses base offset time (i.e., in that case, the Reference Time event shall not be used).
 * It should be recorded:
 *  • As very first event in history (e.g., factory setting)
 *  • If the Base Time or Time Offset of the Insulin Delivery Device is changed
 *  • If the data type limit of the Relative Offset field is reached (i.e., each 65535 s at the latest)
 *  Note: This event should
 *
 * Event data for reference time in base offset format
 * @property recordingReason  The Recording Reason field specifies the reason why the Reference Time Base Offset event was recorded.
 * @property baseTime The base timestamp
 * @property timeOffsetMinutes the offset from the [baseTime] in minutes.
 */
data class ReferenceTimeBaseOffset(
        val recordingReason: RecordingReason,
        val baseTime: BluetoothDateTime,
        val timeOffsetMinutes: Int
)

/**
 * Use this parser when a IDD History Data Control Point returns data buffer containing event type
 * opcode [EventType.REFERENCE_TIME_BASE_OFFSET]. See [HistoryEventDataParser.parseEvent] for details of using
 * the parser.
 */
internal class ReferenceTimeBaseOffsetEventParser: HistoryEventDataParser<ReferenceTimeBaseOffset>() {

    override fun readData(data: DataReader): ReferenceTimeBaseOffset {
        val recordingReason = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), RecordingReason::class.java, RecordingReason.RESERVED_FOR_FUTURE_USE)
        val dateTime = readDateTime(data)
        val offset = data.getNextInt(IntFormat.FORMAT_SINT16)
        return ReferenceTimeBaseOffset(recordingReason, dateTime, offset)
    }

}


