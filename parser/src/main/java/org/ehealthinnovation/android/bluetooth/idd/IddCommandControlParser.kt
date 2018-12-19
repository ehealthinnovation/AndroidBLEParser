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
            Opcode.WRITE_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE -> readWriteBasalProfileTemplateResponse(data)
            Opcode.CONFIRM_ANNUNCIATION_RESPONSE -> readConfirmAnnunciationResponse(data)
            Opcode.SET_TBR_TEMPLATE_RESPONSE -> readSetTbrTemplateResponse(data)
            Opcode.SET_BOLUS_RESPONSE -> readSetBolusResponse(data)
            Opcode.GET_TBR_TEMPLATE_RESPONSE -> readTbrTemplateResponse(data)
            Opcode.CANCEL_BOLUS_RESPONSE -> readCancelBolusResponse(data)
            Opcode.GET_AVAILABLE_BOLUSES_RESPONSE -> readAvailableBolusesResponse(data)
            Opcode.GET_BOLUS_TEMPLATE_RESPONSE -> readGetBolusTemplateResponse(data)
            Opcode.SET_BOLUS_TEMPLATE_RESPONSE -> readSetBolusTemplateResponse(data)
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

    internal fun readWriteBasalProfileTemplateResponse(dataReader: DataReader): WriteBasalRateProfileTemplateResponse =
            WriteBasalRateProfileTemplateResponseParser().parseWriteBasalRateProfileTemplateResponse(dataReader)

    internal fun readConfirmAnnunciationResponse(dataReader: DataReader): ConfirmAnnunciationResponse =
            SimpleResponseParser().parseConfirmAnnunciationResponse(dataReader)

    internal fun readGeneralResponse(dataReader: DataReader): GeneralResponse =
            SimpleResponseParser().readGeneralResponse(dataReader)

    internal fun readSetTbrTemplateResponse(dataReader: DataReader): SetTbrTemplateResponse =
            SimpleResponseParser().readSetTbrTemplateResponse(dataReader)

    internal fun readSetBolusResponse(dataReader: DataReader): SetBolusResponse =
            SimpleResponseParser().readSetBolusResponse(dataReader)

    internal fun readTbrTemplateResponse(dataReader: DataReader): GetTbrTemplateResponse =
            SimpleResponseParser().readGetTbrTemplateResponse(dataReader)

    internal fun readCancelBolusResponse(dataReader: DataReader): CancelBolusResponse =
            SimpleResponseParser().readCancelBolusResponse(dataReader)

    internal fun readAvailableBolusesResponse(dataReader: DataReader): GetAvailableBolusesResponse =
            SimpleResponseParser().readGetAvailableBolusesResponse(dataReader)

    internal fun readGetBolusTemplateResponse(dataReader: DataReader): GetBolusTemplateResponse =
            GetBolusTemplateResponseParser().readGetBolusTemplateResponse(dataReader)

    internal fun readSetBolusTemplateResponse(dataReader: DataReader): SetBolusTemplateResponse =
            SimpleResponseParser().readSetBolusTemplateResponse(dataReader)
}