package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

class RacpOperandComposer {
    /**
     * Compose [operand] and write into buffer through [dataWriter]
     *
     * [operand] must be subclass of [CommandOperand]
     *
     * @throws IllegalAccessException if operand class is not supported
     */
    fun compose(operand: CommandOperand, dataWriter: DataWriter) {
        when (operand) {
            is SimpleOperationWithFilter -> composeFilteredSimpleOperand(operand, dataWriter)
            is FilteredBySequenceNumber -> composeSequenceNumberOperand(operand, dataWriter)
            is FilteredBySequenceNumberRange -> composeSequenceNumberRangeOperand(operand, dataWriter)
            else -> {
                throw IllegalArgumentException("Operand not supported in this function")
            }
        }
    }

    /**
     * Compose the [operand] for filtering with single bounded time offset into [dataWriter]
     */
    internal fun composeSequenceNumberOperand(operand: FilteredBySequenceNumber, dataWriter: DataWriter) {
        dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.filter.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.sequenceNumber, IntFormat.FORMAT_UINT32)
    }

    /**
     * Compose the [operand] for filtering with time offset range into [dataWriter]
     */
    internal fun composeSequenceNumberRangeOperand(operand: FilteredBySequenceNumberRange, dataWriter: DataWriter) {
        dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.filter.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.startSequenceNumber, IntFormat.FORMAT_UINT32)
        dataWriter.putInt(operand.endSequenceNumber, IntFormat.FORMAT_UINT32)
    }


    /**
     * Compose the [operand] for simple operation (with filtering) into [dataWriter] buffer
     *
     * @see [SimpleOperationWithFilter]
     */
    internal fun composeFilteredSimpleOperand(operand: SimpleOperationWithFilter, dataWriter: DataWriter) {
        dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.filter.key, IntFormat.FORMAT_UINT8)
    }
}
