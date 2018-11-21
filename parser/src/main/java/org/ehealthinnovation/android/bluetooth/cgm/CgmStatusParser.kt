package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Parser for the [CgmStatus] Characteristic
 */
class CgmStatusParser : CharacteristicParser<CgmStatus> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.STATUS.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmStatus {
        val data = packet.readData()
        val timeOffset = data.getNextInt(IntFormat.FORMAT_UINT16)
        val statusFlags = readStatus(data)
        return CgmStatus(
                timeOffset,
                statusFlags)
    }

    internal fun readStatus(data: DataReader): EnumSet<StatusFlag> =
            parseFlags(data.getNextInt(IntFormat.FORMAT_UINT24), StatusFlag::class.java)

}

