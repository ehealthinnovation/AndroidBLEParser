package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration

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

}