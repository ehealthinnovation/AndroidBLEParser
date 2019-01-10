package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commanddata.*
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Use this parser to parse the response from IDD Command Data Characteristic
 */
class IddCommandDataParser : CharacteristicParser<IddCommandDataResponse> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.COMMAND_DATA.uuid
    }

    override fun parse(packet: CharacteristicPacket): IddCommandDataResponse {
        val data = packet.readData()
        val opcode = readOpcode(data)
        return when (opcode) {
            Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE -> readBasalRateProfileTemplateResponse(data)
            Opcode.READ_ISF_PROFILE_TEMPLATE_RESPONSE -> readISFProfileTemplateResponse(data)
            Opcode.READ_I2CHO_RATIO_PROFILE_TEMPLATE_RESPONSE -> readI2CHOProfileTemplateResponse(data)
            Opcode.READ_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_RESPONSE -> readTargetGlucoseRangeProfileTemplateResponse(data)
            Opcode.GET_TEMPLATE_STATUS_AND_DETAILS_RESPONSE -> readGetTemplateStatusAndDetailsResponse(data)
            else -> throw IllegalArgumentException("Opcode not supported")
        }
    }


    /**
     * Read the first 2 bytes from the buffer and map them to an [Opcode]
     */
    internal fun readOpcode(dataReader: DataReader): Opcode = readEnumeration(
            dataReader.getNextInt(IntFormat.FORMAT_UINT16),
            Opcode::class.java,
            Opcode.RESERVED_FOR_FUTURE_USE)

    /**
     * Parse the rest data in [DataReader] into a [ReadBasalProfileTemplateResponse]. Use this method
     * when the opcode is [Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE]
     */
    internal fun readBasalRateProfileTemplateResponse(dataReader: DataReader): ReadBasalProfileTemplateResponse =
            ReadBasalProfileTemplateResponse(ReadSingleValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(dataReader))


    /**
     * Parse the rest data in [DataReader] into a [ReadISFProfileTemplateResponse]. Use this method
     * when the opcode is [Opcode.READ_ISF_PROFILE_TEMPLATE_RESPONSE]
     */
    internal fun readISFProfileTemplateResponse(dataReader: DataReader): ReadISFProfileTemplateResponse =
           ReadISFProfileTemplateResponse( ReadSingleValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(dataReader))


    /**
     * Parse the rest data in [DataReader] into a [ReadI2CHOProfileTemplateResponse]. Use this method
     * when the opcode is [Opcode.READ_I2CHO_RATIO_PROFILE_TEMPLATE_RESPONSE]
     */
    internal fun readI2CHOProfileTemplateResponse(dataReader: DataReader): ReadI2CHOProfileTemplateResponse =
            ReadI2CHOProfileTemplateResponse(ReadSingleValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(dataReader))


    /**
     * Parse the rest data in [DataReader] into a [ReadTargetGlucoseRangeProfileTemplateResponse]. Use this method
     * when the opcode is [Opcode.READ_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_RESPONSE]
     */
    internal fun readTargetGlucoseRangeProfileTemplateResponse(dataReader: DataReader): ReadTargetGlucoseRangeProfileTemplateResponse =
            ReadTargetGlucoseRangeProfileTemplateResponse(ReadRangedValueTimeBlockProfileTemplateResponseParser().parseResponseOperand(dataReader))


    /**
     * Parse the rest data in [DataReader] into a [GetTemplateStatusAndDetailsResponse]. Use this method
     * when the opcode is [Opcode.GET_TEMPLATE_STATUS_AND_DETAILS]
     */
    internal fun readGetTemplateStatusAndDetailsResponse(dataReader: DataReader): GetTemplateStatusAndDetailsResponse =
            GetTemplateStatusAndDetailsResponseParser().parseResponseOperand(dataReader)
}