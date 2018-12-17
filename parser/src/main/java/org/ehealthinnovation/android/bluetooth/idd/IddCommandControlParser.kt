package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.*
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser to parse the response from a Idd Command Control Point
 */
class IddCommandControlParser: CharacteristicParser<IddCommandControlResponse> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.COMMAND_CONTROL_POINT.uuid
    }

    override fun parse(packet: CharacteristicPacket): IddCommandControlResponse {
        val data = packet.readData()
        val opcode = readOpcode(data)
        return when(opcode){
            Opcode.RESPONSE_CODE -> readGeneralResponse(data)
            Opcode.SNOOZE_ANNUNCIATION_RESPONSE -> readSnoozeAnnunciationResponse(data)
            else->throw IllegalArgumentException("response opcode not supported")
        }
    }

    /**
     * Read the first 2 bytes from the buffer and map them to an [Opcode]
     */
    internal fun readOpcode(dataReader: DataReader): Opcode = readEnumeration(
            dataReader.getNextInt(IntFormat.FORMAT_UINT16),
            Opcode::class.java,
            Opcode.RESERVED_FOR_FUTURE_USE
    )

    internal fun readSnoozeAnnunciationResponse(dataReader: DataReader): SnoozeAnnunciationResponse =
            SimpleResponseParser().readSnoozeAnnunciationResponse(dataReader)

    internal fun readGeneralResponse(dataReader: DataReader): GeneralResponse =
            SimpleResponseParser().readGeneralResponse(dataReader)
}