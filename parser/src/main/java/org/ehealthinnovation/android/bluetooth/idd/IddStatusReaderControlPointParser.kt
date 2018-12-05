package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*

class IddStatusReaderControlPointParser : CharacteristicParser<StatusReaderControlResponse> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.STATUS_READER_CONTROL_POINT.uuid
    }

    override fun parse(packet: CharacteristicPacket): StatusReaderControlResponse {
        val data = packet.readData()
        val opcode = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT16), StatusReaderControlOpcode::class.java, StatusReaderControlOpcode.RESERVED_FOR_FUTURE_USE)
        return when (opcode) {
            StatusReaderControlOpcode.RESPONSE_CODE -> readGeneralResponse(data)
            else -> throw IllegalArgumentException("Opcode $opcode not supported by this parser")
        }
    }

    internal fun readGeneralResponse(data: DataReader): StatusReaderControlResponse {
        val requestOpcode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT16),
                StatusReaderControlOpcode::class.java,
                StatusReaderControlOpcode.RESERVED_FOR_FUTURE_USE
        )

        val responseCode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                StatusReaderControlResponseCode::class.java,
                StatusReaderControlResponseCode.RESERVED_FOR_FUTURE_USE
        )

        return StatusReaderControlGeneralResponse(requestOpcode, responseCode)
    }

}