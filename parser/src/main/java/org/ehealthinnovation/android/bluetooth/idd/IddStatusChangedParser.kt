package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Parser to deserialize an byte array into a [IddStatusChanged] characteristic data structure.
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.idd_status_changed.xml
 */
class IddStatusChangedParser : CharacteristicParser<IddStatusChanged> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.STATUS_CHANGED.uuid
    }

    override fun parse(packet: CharacteristicPacket): IddStatusChanged {
        val data = packet.readData()
        return IddStatusChanged(
                readStatusChangedFlags(data)
        )
    }

    internal fun readStatusChangedFlags(data: DataReader): EnumSet<Status> {
        val rawFlagValue = data.getNextInt(IntFormat.FORMAT_UINT16)
        return parseFlags(rawFlagValue, Status::class.java)
    }

}