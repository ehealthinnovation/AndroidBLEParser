package org.ehealthinnovation.android.bluetooth.idd.commanddata

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SingleValueProfileTemplateTimeBlockParser
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SingleValueTimeBlock
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.parseFlags
import kotlin.reflect.KClass


/**
 * Operand in a read profile template command response. Each template consists of 1-3 single value
 * timeblocks.
 *
 * @property templateNumber the number of the profile template
 * @property firstTimeBlockIndex the index of the first timeblock contained in this packet.
 * @property firstTimeBlock the first time block of the profile template, it must be present.
 * @property secondTimeBlock the second time block of the profile template. If this value is null,
 * it means the second time block is not present.
 * @property thirdTimeBlock the third time block of the profile template. If this value is null,
 * it means the third time block is not present.
 */
data class ReadSingleValueProfileTemplateResponseOperand(
        val templateNumber: Int,
        val firstTimeBlockIndex: Int,
        val firstTimeBlock: SingleValueTimeBlock,
        val secondTimeBlock: SingleValueTimeBlock?,
        val thirdTimeBlock: SingleValueTimeBlock?
)

/**
 * The response for Idd Command Control Point operation [Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE]
 */
data class ReadBasalProfileTemplateResponse(
        val operand:ReadSingleValueProfileTemplateResponseOperand
): IddCommandDataResponse()

/**
 * The response for Idd Command Control Point operation [Opcode.READ_I2CHO_RATIO_PROFILE_TEMPLATE]
 */
data class ReadI2CHOProfileTemplateResponse(
        val operand:ReadSingleValueProfileTemplateResponseOperand
) : IddCommandDataResponse()

/**
 * The response for Idd Command Control Point operation [Opcode.READ_ISF_PROFILE_TEMPLATE_RESPONSE]
 */
data class ReadISFProfileTemplateResponse(
        val operand:ReadSingleValueProfileTemplateResponseOperand
) : IddCommandDataResponse()

/**
 * Use this class to parse the operand from the [DataReader] for response from Idd Command Data
 * characteristic with one of the following three opcodes,[Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE]
 * [Opcode.READ_I2CHO_RATIO_PROFILE_TEMPLATE_RESPONSE] and [Opcode.READ_ISF_PROFILE_TEMPLATE_RESPONSE]
 */
class ReadSingleValueTimeBlockProfileTemplateResponseParser {


    /**
     * Parses the data in [DataReader] into [ReadSingleValueProfileTemplateResponseOperand]
     */
    internal fun parseResponseOperand(dataReader: DataReader): ReadSingleValueProfileTemplateResponseOperand {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val templateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val firstTimeBlockIndex = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val timeBlockParser = SingleValueProfileTemplateTimeBlockParser()
        val firstBlock = timeBlockParser.readData(dataReader)
        val secondBlock = if (flags.contains(Flag.SECOND_TIME_BLOCK_PRESENT)) timeBlockParser.readData(dataReader) else null
        val thirdBlock = if (flags.contains(Flag.THIRD_TIME_BLOCK_PRESENT)) timeBlockParser.readData(dataReader) else null

        return ReadSingleValueProfileTemplateResponseOperand(templateNumber, firstTimeBlockIndex, firstBlock, secondBlock, thirdBlock)
    }

    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the fields Second Duration and Second Rate are present. */
        SECOND_TIME_BLOCK_PRESENT(0),
        /**If this bit is set, the fields Third Duration and Third Rate are present. */
        THIRD_TIME_BLOCK_PRESENT(1)
    }
}