package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.CgmUuid
import org.ehealthinnovation.android.bluetooth.parser.CharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.CharacteristicParser
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Parser for [CgmSessionRunTime] Characteristic
 */
class CgmSessionRunTimeParser : CharacteristicParser<CgmSessionRunTime>{

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.SESSION_RUN_TIME.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmSessionRunTime {
        val data = packet.readData()
        return CgmSessionRunTime(data.getNextInt(IntFormat.FORMAT_UINT16))
    }

}