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

    /** Parse the response of a [GetTbrTemplate] command */
    internal fun readGetTbrTemplateResponse(data: DataReader): GetTbrTemplateResponse {
        val templateNumber = data.getNextInt(IntFormat.FORMAT_UINT8)
        val type = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val value = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val duration = data.getNextInt(IntFormat.FORMAT_UINT16)
        return GetTbrTemplateResponse(templateNumber, type, value, duration)
    }

    /** Parse the response of a [CancelBolus] command */
    internal fun readCancelBolusResponse(data: DataReader): CancelBolusResponse{
        val id = data.getNextInt(IntFormat.FORMAT_UINT16)
        return CancelBolusResponse(id)
    }

}