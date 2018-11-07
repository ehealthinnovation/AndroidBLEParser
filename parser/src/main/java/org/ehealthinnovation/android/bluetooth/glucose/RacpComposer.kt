package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.isIntRangeValid

/**
 * Composer for the Record Access Control Point
 *
 */
class RacpComposer : CharacteristicComposer<GlucoseCommand> {

    override fun canCompose(request: GlucoseCommand): Boolean {
        //This test if the request meets requirements
        return false
    }


    override fun compose(request: GlucoseCommand, dataWriter: DataWriter) {
        when (request) {
            is AbortOperation -> composeAbortOperation(dataWriter)
            is ReportNumberOfRecords -> composeReportNumberOfRecords(request.operand, dataWriter)
            else -> {
                throw IllegalArgumentException("operation not recognized")
            }
        }
    }

    /**
     * Compose the abort operation into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer fails to process the write operation
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    fun composeAbortOperation(dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.ABORT_OPERATION.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(Operator.NULL.key, IntFormat.FORMAT_UINT8)
    }


    /**
     * Compose the report number of records into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer failed to process the write operations
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    fun composeReportNumberOfRecords(operand: CommandOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key, IntFormat.FORMAT_UINT8)
        RacpOperandComposer.composeOperand(operand, dataWriter)
    }


}


class RacpOperandComposer {

    companion object {
        /**
         * Compose [operand] and write into buffer through [dataWriter]
         *
         * [operand] must be subclass of [CommandOperand]
         *
         * @throws IllegalAccessException if operand class is not supported
         */
        fun composeOperand(operand: CommandOperand, dataWriter: DataWriter) {
            when (operand) {
                is SimpleOperand -> composeSimpleOperand(operand, dataWriter)
                is FilteredBySequenceNumber -> composeSequenceNumberOperand(operand, dataWriter)
                is FilteredBySequenceNumberRange -> composeSequenceNumberRangeOperand(operand, dataWriter)
                else -> {
                    throw IllegalArgumentException("Operand not supported in this function")
                }
            }
        }


        /**
         * Compose the [operand] for simple operation into [dataWriter] buffer
         *
         * @see [GlucoseSimpleOperation]
         */
        fun composeSimpleOperand(operand: SimpleOperand, dataWriter: DataWriter) {
            dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
        }

        /**
         * Compose the [operand] for filtering with single bounded sequence number into [dataWriter]
         */
        fun composeSequenceNumberOperand(operand: FilteredBySequenceNumber, dataWriter: DataWriter) {
            dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(Filter.SEQUENCE_NUMBER.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(operand.sequenceNumber, IntFormat.FORMAT_UINT16)
        }

        /**
         * Compose the [operand] for filtering with sequence number range into [dataWriter]
         */
        fun composeSequenceNumberRangeOperand(operand: FilteredBySequenceNumberRange, dataWriter: DataWriter) {
            if (!isIntRangeValid(operand.lowerSequenceNumber, operand.higherSequenceNumber)) {
                throw IllegalArgumentException("Sequence number range is invalid")
            }
            dataWriter.putInt(Operator.WITHIN_RANGE_OF_INCLUSIVE.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(Filter.SEQUENCE_NUMBER.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(operand.lowerSequenceNumber, IntFormat.FORMAT_UINT16)
            dataWriter.putInt(operand.higherSequenceNumber, IntFormat.FORMAT_UINT16)
        }
    }

}









