package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser for Cgm Specific Control Point response.
 * @see  https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_specific_ops_control_point.xml
 */
class CgmControlParser : CharacteristicParser<CgmControlResponse> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.SPECIFIC_CONTROL_POINT.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmControlResponse {
        val data = packet.readData()
        val opcode = readResponseOpcode(data) // this opcode is not used, just to consume the byte in the buffer
        val requestOpcode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                Opcode::class.java,
                Opcode.RESERVED_FOR_FUTURE_USE)
        val responseCode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                ResponseCode::class.java,
                ResponseCode.RESERVED_FOR_FUTURE_USE)

        return CgmControlResponse(
                requestOpcode,
                responseCode
        )
    }

    /**
     * Read in the next byte and test if it is [Opcode.RESPONSE_CODE]
     *
     * @throws IllegalStateException if the byte is not [Opcode.RESPONSE_CODE]
     */
    internal fun readResponseOpcode(dataReader: DataReader) {
        val opcode = readEnumeration(
                dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                Opcode::class.java)

        if (opcode != Opcode.RESPONSE_CODE) {
            throw IllegalStateException("The input opcode is not Response Code")
        }
    }
}