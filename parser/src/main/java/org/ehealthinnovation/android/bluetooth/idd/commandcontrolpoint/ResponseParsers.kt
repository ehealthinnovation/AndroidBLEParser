package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * This class contains a set of Command control point response parsing functions that are structurally
 * simple (only a single function is needed for parsing). If the parsing process is more complicated, a
 * dedicated class will be create for those responses.
 */
class SimpleResponseParser {

    /** Parse the response of a [SnoozeAnnunciation] command*/
    internal fun readSnoozeAnnunciationResponse(data: DataReader): SnoozeAnnunciationResponse =
            SnoozeAnnunciationResponse(data.getNextInt(IntFormat.FORMAT_UINT16))

    /** Parse the response of a [ConfirmAnnunciation] command*/
    internal fun parseConfirmAnnunciationResponse(data: DataReader): ConfirmAnnunciationResponse =
            ConfirmAnnunciationResponse(data.getNextInt(IntFormat.FORMAT_UINT16))

    /** Parse a general response indicating error or successful execution of a command*/
    internal fun readGeneralResponse(data: DataReader): GeneralResponse {
        val request = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT16),
                Opcode::class.java,
                Opcode.RESERVED_FOR_FUTURE_USE
        )

        val result = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                ResponseCode::class.java,
                ResponseCode.RESERVED_FOR_FUTURE_USE
        )

        return GeneralResponse(request, result)
    }

    /** Parse response from [SetTbrTemplate] command */
    internal fun readSetTbrTemplateResponse(data: DataReader): SetTbrTemplateResponse {
        val templateNumber = data.getNextInt(IntFormat.FORMAT_UINT8)
        return SetTbrTemplateResponse(templateNumber)
    }

    /** Parse a [SetBolusResponse] */
    internal fun readSetBolusResponse(data: DataReader): SetBolusResponse {
        val id = data.getNextInt(IntFormat.FORMAT_UINT16)
        return SetBolusResponse(id)
    }

    /** Parse the response of a [GetTbrTemplate] command */
    internal fun readGetTbrTemplateResponse(data: DataReader): GetTbrTemplateResponse {
        val templateNumber = data.getNextInt(IntFormat.FORMAT_UINT8)
        val type = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val value = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val duration = data.getNextInt(IntFormat.FORMAT_UINT16)
        return GetTbrTemplateResponse(templateNumber, type, value, duration)
    }

    /** Parse the response of a [CancelBolus] command */
    internal fun readCancelBolusResponse(data: DataReader): CancelBolusResponse {
        val id = data.getNextInt(IntFormat.FORMAT_UINT16)
        return CancelBolusResponse(id)
    }

    /**
     * Flags used in parsing the [GetAvailableBolusesResponse]
     */
    internal enum class GetAvailableBolusesFlag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, a fast bolus is currently available to be set. */
        FAST_BOLUS_AVAILABLE(0),
        /**If this bit is set, an extended bolus is currently available to be set. */
        EXTENDED_BOLUS_AVAILABLE(1),
        /**If this bit is set, a multiwave bolus is currently available to be set. */
        MULTIWAVE_BOLUS_AVAILABLE(2);
    }

    /**
     * Parse the response of a [GetAvailableBoluses] command
     */
    internal fun readGetAvailableBolusesResponse(data: DataReader): GetAvailableBolusesResponse {
        val flagRawData = data.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawData, GetAvailableBolusesFlag::class.java)
        val fastBolusAvailable = flags.contains(GetAvailableBolusesFlag.FAST_BOLUS_AVAILABLE)
        val extendedBolusAvailable = flags.contains(GetAvailableBolusesFlag.EXTENDED_BOLUS_AVAILABLE)
        val multiwaveBolusAvailable = flags.contains(GetAvailableBolusesFlag.MULTIWAVE_BOLUS_AVAILABLE)
        return GetAvailableBolusesResponse(fastBolusAvailable, extendedBolusAvailable, multiwaveBolusAvailable)
    }

    /** Parse response from [SetTbrTemplate] command */
    internal fun readSetBolusTemplateResponse(data: DataReader): SetBolusTemplateResponse {
        val templateNumber = data.getNextInt(IntFormat.FORMAT_UINT8)
        return SetBolusTemplateResponse(templateNumber)
    }

    internal fun readTemplatesOperationResult(data: DataReader): TemplatesOperationResults {
        val numberOfTemplate = data.getNextInt(IntFormat.FORMAT_UINT8)
        val templateNumbers = mutableListOf<Int>()
        for (i in 1..numberOfTemplate) {
            templateNumbers.add(data.getNextInt(IntFormat.FORMAT_UINT8))
        }
        return TemplatesOperationResults(numberOfTemplate, templateNumbers as List<Int>)
    }

    /** Parse response from [ResetTemplateStatus] command*/
    internal fun readResetTemplateStatusResponse(data: DataReader): ResetTemplateStatusResponse {
        return ResetTemplateStatusResponse(readTemplatesOperationResult(data))
    }

    /** Parse response from [ActivateTemplates] command*/
    internal fun readActivateTemplatesResponse(data: DataReader): ActivateTemplatesResponse {
        return ActivateTemplatesResponse(readTemplatesOperationResult(data))
    }

    /** Parse response from [GetMaxBolusAmount] command*/
    internal fun readMaxBolusAmountResponse(data: DataReader): MaxBolusAmountResponse {
        return MaxBolusAmountResponse(data.getNextFloat(FloatFormat.FORMAT_SFLOAT))
    }

}