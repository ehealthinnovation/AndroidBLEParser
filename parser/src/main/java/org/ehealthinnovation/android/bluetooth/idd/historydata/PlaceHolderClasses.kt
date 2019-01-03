package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader

/**
 * This event data contain no property fields. It will be used when the history event contains no
 * event data.
 *
 * Currently [EventType.DATA_CORRUPTION] and [EventType.POINTER_EVENT] use this event data type.
 *  For [EventType.DATA_CORRUPTION] the recording criteria is
 *
 *  If the Insulin Delivery Device detects corrupt events in the history memory, a Data Corruption
 *  event should be inserted (e.g., if the E2E-CRC is wrong). If this event is recorded, the Server
 *  shall set its Sequence Number to 0xFFFF FFFF.
 *
 *  Note: If the Client reads a Data Corruption event from the Server, it can detect that the data
 *  corruption (e.g., wrong E2E-CRC) occurred on the Insulin Delivery Device. If the Server does not
 *  use the Data Corruption event and just sends an event with a wrong E2E-CRC, the Collector cannot
 *  determine if the data corruption was on the Insulin Delivery Device or somewhere else (e.g., on
 *  a Bluetooth dongle connected to a PC).
 *
 *  For [EventType.POINTER_EVENT], the recording criteria is This event is a placeholder to align
 *  the history events exposed by the IDD RACP with the events of a possible proprietary manufacturer
 *  History CP in the case that the absolute time stamp of those events are equal (i.e., the aligned
 *  events can have continuous Sequence Numbers even if a proprietary History CP uses its own Sequence
 *  Numbers).
 */
class EmptyEventData

internal class EmptyEventDataParser : HistoryEventDataParser<EmptyEventData>() {
    override fun readData(dataReader: DataReader): EmptyEventData {
        return EmptyEventData()
    }
}
