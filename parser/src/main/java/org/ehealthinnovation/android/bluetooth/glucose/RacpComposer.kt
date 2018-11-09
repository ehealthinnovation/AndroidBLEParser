package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.*

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
            is DeleteRecords -> composeDeleteRecords(request.operand, dataWriter)
            is ReportRecords -> composeReportRecords(request.operand, dataWriter)
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
    internal fun composeAbortOperation(dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.ABORT_OPERATION.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(Operator.NULL.key, IntFormat.FORMAT_UINT8)
    }


    /**
     * Compose the report number of records into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer failed to process the write operations
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    internal fun composeReportNumberOfRecords(operand: CommandOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key, IntFormat.FORMAT_UINT8)
        RacpOperandComposer.composeOperand(operand, dataWriter)
    }

    /**
     * Compose the delete records into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer failed to process the write operations
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    internal fun composeDeleteRecords(operand: CommandOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.DELETE_STORED_RECORDS.key, IntFormat.FORMAT_UINT8)
        RacpOperandComposer.composeOperand(operand, dataWriter)
    }

    /**
     * Compose the report records into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer failed to process the write operations
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    internal fun composeReportRecords(operand: CommandOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.REPORT_STORED_RECORDS.key, IntFormat.FORMAT_UINT8)
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
        internal fun composeOperand(operand: CommandOperand, dataWriter: DataWriter) {
            when (operand) {
                is SimpleOperand -> composeSimpleOperand(operand, dataWriter)
                is FilteredBySequenceNumber -> composeSequenceNumberOperand(operand, dataWriter)
                is FilteredBySequenceNumberRange -> composeSequenceNumberRangeOperand(operand, dataWriter)
                is FilteredByBluetoothDateTime -> composeTimeOperand(operand, dataWriter)
                is FilteredByBluetoothDateTimeRange -> composeTimeRangeOperand(operand, dataWriter)
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
        internal fun composeSimpleOperand(operand: SimpleOperand, dataWriter: DataWriter) {
            dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
        }

        /**
         * Compose the [operand] for filtering with single bounded sequence number into [dataWriter]
         */
        internal fun composeSequenceNumberOperand(operand: FilteredBySequenceNumber, dataWriter: DataWriter) {
            dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(Filter.SEQUENCE_NUMBER.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(operand.sequenceNumber, IntFormat.FORMAT_UINT16)
        }

        /**
         * Compose the [operand] for filtering with sequence number range into [dataWriter]
         */
        internal fun composeSequenceNumberRangeOperand(operand: FilteredBySequenceNumberRange, dataWriter: DataWriter) {
            if (!isIntRangeValid(operand.lowerSequenceNumber, operand.higherSequenceNumber)) {
                throw IllegalArgumentException("Sequence number range is invalid")
            }
            dataWriter.putInt(Operator.WITHIN_RANGE_OF_INCLUSIVE.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(Filter.SEQUENCE_NUMBER.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(operand.lowerSequenceNumber, IntFormat.FORMAT_UINT16)
            dataWriter.putInt(operand.higherSequenceNumber, IntFormat.FORMAT_UINT16)
        }

        /**
         * Compose the [operand] for filtering with single bounded [Date] into
         * a buffer through [dataWriter]
         *
         * The [BluetoothDateTime] operand in [FilteredByBluetoothDateTime] must be created by
         * [createBluetoothDateTime] where the validity check of data takes place
         */
        internal fun composeTimeOperand(operand: FilteredByBluetoothDateTime, dataWriter: DataWriter) {
            dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(Filter.USER_FACING_TIME.key, IntFormat.FORMAT_UINT8)
            BluetoothDateTimeUtility.composeBluetoothTime(operand.date, dataWriter)
        }

        /**
         * Uses Bluetooth DateTime range defined in [FilteredByBluetoothDateTimeRange] as filtering criteria for records.
         * And compose the range filter as operand into the buffer through [dataWriter].
         *
         * This function does not check the validity of the date range. The user of this function needs
         * to make sure the [startDate] is before the [endDate]. If an invalid range is input and send to
         * a target device, a response indicating invalid operand is likely returned.
         *
         * The [BluetoothDateTime] of [startDate] and [endDate] must be created by
         * [createBluetoothDateTime] where the validity check of data takes place
         */
        internal fun composeTimeRangeOperand(operand: FilteredByBluetoothDateTimeRange, dataWriter: DataWriter) {
            dataWriter.putInt(Operator.WITHIN_RANGE_OF_INCLUSIVE.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(Filter.USER_FACING_TIME.key, IntFormat.FORMAT_UINT8)
            BluetoothDateTimeUtility.composeBluetoothTime(operand.startDate, dataWriter)
            BluetoothDateTimeUtility.composeBluetoothTime(operand.endDate, dataWriter)
        }

    }

}









