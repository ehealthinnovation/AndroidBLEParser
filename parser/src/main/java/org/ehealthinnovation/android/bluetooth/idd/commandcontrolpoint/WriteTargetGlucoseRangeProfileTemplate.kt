package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*


/**
 * Base class for time blocks that has two value, as oppose to [SingleValueTimeBlock]. Some profile template consists of an array of [RangedTimeBlock] of specific type.
 * @property duration the duration of this time block in minutes
 * @property lowerValue the lower bound of a range defined at this time block. It is a general floating point value, each type of profile template has its
 * unit and range definition for value.
 * @property higherValue the higher bound of a range defined at this time block. It is a general floating point value, each type of profile template has its
 * unit and range definition for value.
 */
data class RangedTimeBlock(val duration: Int, val lowerValue: Float, val higherValue: Float)

/**
 * Operand in a write profile template command. Each issue of the command contains less than or equal to two
 * time blocks, being constrained by the size of an BLE packet. Therefore, to write a complete profile template,
 * one needs to issue multiple commands in a transaction.
 *
 * @property isEndTransaction set to true if this operand contains the last time block in a profile template
 * @property templateNumber the template number the current write transaction is writing to
 * @property firstTimeBlockIndex the index of the first timeblock of this profile template operand
 * @property timeBlocks 1 or 2 time blocks to be transferred in this command.
 */
class WriteRangeProfileTemplateOperand {
    val isEndTransaction: Boolean
    val templateNumber: Int
    val firstTimeBlockIndex: Int
    val timeblocks: List<RangedTimeBlock>

    private constructor(isEndTransaction: Boolean,
                        templateNumber: Int,
                        firstTimeBlockIndex: Int,
                        timeBlocks: List<RangedTimeBlock>) {
        this.isEndTransaction = isEndTransaction
        this.templateNumber = templateNumber
        this.firstTimeBlockIndex = firstTimeBlockIndex
        this.timeblocks = timeBlocks
    }

    constructor(isEndTransaction: Boolean,
                templateNumber: Int,
                firstTimeBlockIndex: Int,
                firstTimeblock: RangedTimeBlock) :
            this(isEndTransaction, templateNumber, firstTimeBlockIndex, listOf<RangedTimeBlock>(firstTimeblock))

    constructor(isEndTransaction: Boolean,
                templateNumber: Int,
                firstTimeBlockIndex: Int,
                firstTimeblock: RangedTimeBlock,
                secondTimeBlock: RangedTimeBlock) :
            this(isEndTransaction, templateNumber, firstTimeBlockIndex, listOf<RangedTimeBlock>(firstTimeblock, secondTimeBlock))
}

/**
 * A class for writing a profile template command into a data buffer
 */
class WriteRangeProfileTemplateOperandComposer{

    /**
     * Call this function with [WriteRangeProfileTemplateOperand] to serialize the write operand into [DataWriter]
     */
    internal fun composeOperand(operand: WriteRangeProfileTemplateOperand, dataWriter: DataWriter) {
        val flags = getFlags(operand)
        val flagValue = writeEnumFlagsToInteger(flags)
        dataWriter.putInt(flagValue, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.templateNumber, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.firstTimeBlockIndex, IntFormat.FORMAT_UINT8)
        for (timeblock in operand.timeblocks) {
            writeTimeBlock(timeblock, dataWriter)
        }
    }

    internal fun getFlags(operand: WriteRangeProfileTemplateOperand): EnumSet<Flag> {
        val output = EnumSet.noneOf(Flag::class.java)
        if (operand.isEndTransaction) output.add(Flag.END_TRANSACTION)
        if (operand.timeblocks.size > 1) output.add(Flag.SECOND_TIME_BLOCK_PRESENT)
        return output
    }

    /**
     * Write a double value time block into [DataWriter]
     */
    internal fun writeTimeBlock(timeBlock: RangedTimeBlock, dataWriter: DataWriter) {
        dataWriter.putInt(timeBlock.duration, IntFormat.FORMAT_UINT16)
        dataWriter.putFloat(timeBlock.lowerValue, -1, FloatFormat.FORMAT_SFLOAT)
        dataWriter.putFloat(timeBlock.higherValue, -1, FloatFormat.FORMAT_SFLOAT)
    }

    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, it signals the Server that all time blocks of the basal rate profile have been sent (i.e., the basal rate profile is complete). It shall only be set if the last time blocks of the basal rate profile are sent. */
        END_TRANSACTION(0),
        /**If this bit is set, the fields Second Duration and Second Rate are present. */
        SECOND_TIME_BLOCK_PRESENT(1);
    }
}

/**
 * Use [readData] to parse the data in [DataReader] into a [RangedTimeBlock]
 */
internal class RangeValueTimeBlockParser{
    fun readData(dataReader: DataReader): RangedTimeBlock =
            RangedTimeBlock(
                    dataReader.getNextInt(IntFormat.FORMAT_UINT16),
                    dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT),
                    dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
            )
}



