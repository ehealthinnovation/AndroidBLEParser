package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*


/**
 * Base class for time blocks. A profile template consists of an array of [SingleValueTimeBlock] of specific type.
 * @property duration the duration of this time block in minutes
 * @property value the value at this time block. It is a general floating point value, each type of profile template has its
 * unit and range definition for value.
 */
open class SingleValueTimeBlock(val duration: Int, val value: Float)

/**
 * Operand in a write profile template command. Each issue of the command contains less than or equal to three
 * time blocks, being constrained by the size of an BLE packet. Therefore, to write a complete profile template,
 * one needs to issue multiple commands in a transaction.
 *
 * @property isEndTransaction set to true if this operand contains the last time block in a profile template
 * @property templateNumber the template number the current write transaction is writing to
 * @property firstTimeBlockIndex the index of the first timeblock of this profile template operand
 * @property timeBlocks 1,2 or 3 time blocks to be transferred in this command.
 */
class WriteProfileTemplateOperand {
    val isEndTransaction: Boolean
    val templateNumber: Int
    val firstTimeBlockIndex: Int
    val timeblocks: List<SingleValueTimeBlock>

    private constructor(isEndTransaction: Boolean,
                        templateNumber: Int,
                        firstTimeBlockIndex: Int,
                        timeBlocks: List<SingleValueTimeBlock>) {
        this.isEndTransaction = isEndTransaction
        this.templateNumber = templateNumber
        this.firstTimeBlockIndex = firstTimeBlockIndex
        this.timeblocks = timeBlocks
    }

    constructor(isEndTransaction: Boolean,
                templateNumber: Int,
                firstTimeBlockIndex: Int,
                firstTimeblock: SingleValueTimeBlock) :
            this(isEndTransaction, templateNumber, firstTimeBlockIndex, listOf<SingleValueTimeBlock>(firstTimeblock))

    constructor(isEndTransaction: Boolean,
                templateNumber: Int,
                firstTimeBlockIndex: Int,
                firstTimeblock: SingleValueTimeBlock,
                secondTimeBlock: SingleValueTimeBlock) :
            this(isEndTransaction, templateNumber, firstTimeBlockIndex, listOf<SingleValueTimeBlock>(firstTimeblock, secondTimeBlock))

    constructor(isEndTransaction: Boolean,
                templateNumber: Int,
                firstTimeBlockIndex: Int,
                firstTimeblock: SingleValueTimeBlock,
                secondTimeBlock: SingleValueTimeBlock,
                thirdTimeBlock: SingleValueTimeBlock) :
            this(isEndTransaction, templateNumber, firstTimeBlockIndex, listOf<SingleValueTimeBlock>(firstTimeblock, secondTimeBlock, thirdTimeBlock))
}

/**
 * A class for writing a profile template command into a data buffer
 */
class WriteProfileTemplateOperandComposer{

    /**
     * Call this function with [WriteProfileTemplateOperand] to serialize the write operand into [DataWriter]
     */
    internal fun composeOperand(operand: WriteProfileTemplateOperand, dataWriter: DataWriter) {
        val flags = getFlags(operand)
        val flagValue = writeEnumFlagsToInteger(flags)
        dataWriter.putInt(flagValue, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.templateNumber, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.firstTimeBlockIndex, IntFormat.FORMAT_UINT8)
        for (timeblock in operand.timeblocks) {
            writeTimeBlock(timeblock, dataWriter)
        }
    }

    internal fun getFlags(operand: WriteProfileTemplateOperand): EnumSet<Flag> {
        val output = EnumSet.noneOf(Flag::class.java)
        if (operand.isEndTransaction) output.add(Flag.END_TRANSACTION)
        if (operand.timeblocks.size > 1) output.add(Flag.SECOND_TIME_BLOCK_PRESENT)
        if (operand.timeblocks.size > 2) output.add(Flag.THIRD_TIME_BLOCK_PRESENT)
        return output
    }

    /**
     * Write a single time block into [DataWriter]
     */
    internal fun writeTimeBlock(timeBlock: SingleValueTimeBlock, dataWriter: DataWriter) {
        dataWriter.putInt(timeBlock.duration, IntFormat.FORMAT_UINT16)
        dataWriter.putFloat(timeBlock.value, -1, FloatFormat.FORMAT_SFLOAT)
    }

    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, it signals the Server that all time blocks of the basal rate profile have been sent (i.e., the basal rate profile is complete). It shall only be set if the last time blocks of the basal rate profile are sent. */
        END_TRANSACTION(0),
        /**If this bit is set, the fields Second Duration and Second Rate are present. */
        SECOND_TIME_BLOCK_PRESENT(1),
        /**If this bit is set, the fields Third Duration and Third Rate are present. */
        THIRD_TIME_BLOCK_PRESENT(2);
    }
}



