package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Composer for the Record Access Control Point
 *
 * Use to compose RACP request for IDD History Event records.
 *
 * Call [canCompose] on your [RacpCommand] to make sure it is supported. If it is supported
 * [canCompose] returns true.
 *
 * Then call [compose] with the [RacpCommand] and the target data buffer [DataWriter] to
 * write the serialized request into the target buffer.
 *
 * When constructing a [RacpComposer], a [RacpOperandComposer] instance needs to be inject to handle
 * device specific operands composing.
 *
 * @throws Exception if the write operation is unsuccessful
 */
class RacpComposer: CharacteristicComposer<RacpCommand> {

   private val operandComposer: RacpOperandComposer = RacpOperandComposer()

    override fun canCompose(request: RacpCommand): Boolean =
            when (request) {
                is AbortOperation,
                is ReportNumberOfRecords,
                is DeleteRecords,
                is ReportRecords -> true
                else -> false
            }


    override fun compose(request: RacpCommand, dataWriter: DataWriter) {
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
        operandComposer.compose(operand, dataWriter)
    }

    /**
     * Compose the delete records into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer failed to process the write operations
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    internal fun composeDeleteRecords(operand: CommandOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.DELETE_STORED_RECORDS.key, IntFormat.FORMAT_UINT8)
        operandComposer.compose(operand, dataWriter)
    }

    /**
     * Compose the report records into the buffer through [dataWriter]
     *
     * @throws RuntimeException if the underlying buffer failed to process the write operations
     * (e.g. the buffer has insufficient space, data type miss-match etc.)
     */
    internal fun composeReportRecords(operand: CommandOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.REPORT_STORED_RECORDS.key, IntFormat.FORMAT_UINT8)
        operandComposer.compose(operand, dataWriter)
    }

}

