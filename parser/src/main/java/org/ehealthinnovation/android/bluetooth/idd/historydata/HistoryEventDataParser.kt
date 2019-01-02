package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader

/**
 * A base class for any History Event Operand Data parsers. A specific history event's data parser should
 * extend this class with [T] filled with the data class that the parser can deserialize a history event
 * into.
 *
 * Each implementation of event data parser needs to override [readData] function that takes in a
 * serialized data loaded [DataReader] and decode it into a desired data structure [T].
 *
 * Any class holding an instance of [HistoryEventDataParser] can call [HistoryEventDataParser.parseEvent]
 * directly to get a [HistoryEvent] data structure.
 */
abstract class HistoryEventDataParser<T> {

    /**
     * Holder of an instance of [HistoryEventDataParser] should call [parseEvent] with a data loaded [DataReader]
     * to decode the data in data buffer. The output [HistoryEvent] is the resulting decoded data.
     *
     * Usually, before calling this method, the first 8 bytes should be read to determined the [EventInfo].
     * Based on [EventInfo.type], a general parser class calls the specific [HistoryEventDataParser].
     *
     * @see [org.ehealthinnovation.android.bluetooth.idd.IddHistoryDataParser.parse] for example on how to use this method.
     */
    internal fun parseEvent(eventInfo: EventInfo, dataReader: DataReader): HistoryEvent<T> {
        val data = readData(dataReader)
        return HistoryEvent(eventInfo, data)
    }

    /**
     * Parse history event data part of in the [DataReader]. Each event type has unique event data,
     * any parser should implement this method to meet with specific data type requirement.
     */
    internal abstract fun readData(dataReader: DataReader): T
}
