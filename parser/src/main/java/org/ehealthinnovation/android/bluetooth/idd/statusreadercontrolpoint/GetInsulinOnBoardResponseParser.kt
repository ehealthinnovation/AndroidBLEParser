package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.InsulinOnBoardResponse
import org.ehealthinnovation.android.bluetooth.parser.*
import org.ehealthinnovation.android.bluetooth.idd.*
import java.util.*

/**
 * Use this class to parse the response to [GetInsulinOnBoard] command.
 */
class GetInsulinOnBoardResponseParser {

    /**
     * If opcode field in the [DataReader] is [StatusReaderControlOpcode.GET_INSULIN_ON_BOARD_RESPONSE],
     * call this method to parse the serialized data into [InsulinOnBoardResponse] structure.
     */
    internal fun parseResponse(dataReader: DataReader): InsulinOnBoardResponse {
        val flag = readFlag(dataReader)
        val insulinOnBoard = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val remainingDuration = if (flag.contains(Flag.REMAINING_DURATION_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        return InsulinOnBoardResponse(insulinOnBoard, remainingDuration)
    }

    internal fun readFlag(dataReader: DataReader): EnumSet<Flag> =
            parseFlags(
                    dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                    Flag::class.java
            )

    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Remaining Duration field is present. */
        REMAINING_DURATION_PRESENT(0);
    }

}
