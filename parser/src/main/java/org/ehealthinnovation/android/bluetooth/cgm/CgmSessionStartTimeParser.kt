package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser for the [CgmSessionStartTime]
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_session_start_time.xml
 */
class CgmSessionStartTimeParser : CharacteristicParser<CgmSessionStartTime> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.SESSION_START_TIME.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmSessionStartTime {
        val data = packet.readData()
        val startTime = readDateTime(data)
        val timeZone = readTimeZone(data)
        val dstOffset = readDst(data)

        return CgmSessionStartTime(
                time = startTime,
                timeZone = timeZone,
                dstOffset = dstOffset
        )
    }


}

