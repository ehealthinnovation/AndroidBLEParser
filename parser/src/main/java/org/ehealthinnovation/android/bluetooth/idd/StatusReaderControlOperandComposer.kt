package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.writeEnumFlags

/**
 * Compose [StatusReaderControlOperand] to the data buffer through [DataWriter]
 */
class StatusReaderControlOperandComposer {

    internal fun composeStatusFlagToReset(resetStatus: StatusFlagToReset, writer: DataWriter) {
        writeEnumFlags(resetStatus.flagsToReset, IntFormat.FORMAT_UINT16, writer)
    }

    internal fun composeGetCounter(counterConfig: GetCounterOperand, writer: DataWriter) {
        writer.putInt(counterConfig.type.key, IntFormat.FORMAT_UINT8)
        writer.putInt(counterConfig.valueSelection.key, IntFormat.FORMAT_UINT8)
    }

    internal fun composeGetActiveBolusDelivery(operand: ActiveBolusDelivery, writer: DataWriter) {
        writer.putInt(operand.id, IntFormat.FORMAT_UINT16)
        writer.putInt(operand.valueType.key, IntFormat.FORMAT_UINT8)
    }
}