package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.historydata.*
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser to parse the response from a Idd Command Control Point
 */
class IddHistoryDataParser : CharacteristicParser<HistoryEvent<out Any>> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.HISTORY_DATA.uuid
    }

    override fun parse(packet: CharacteristicPacket): HistoryEvent<out Any> {
        val data = packet.readData()
        val eventInfo = readEventInfo(data)
        return when (eventInfo.type) {
            EventType.REFERENCE_TIME -> readReferenceTime(eventInfo, data)
            else -> throw IllegalArgumentException("Event type not supported")
        }
    }

    internal fun readReferenceTime(eventInfo: EventInfo, data: DataReader): HistoryEvent<ReferenceTimeData> =
            ReferenceTimeEventParser().parseEvent(eventInfo, data)

}