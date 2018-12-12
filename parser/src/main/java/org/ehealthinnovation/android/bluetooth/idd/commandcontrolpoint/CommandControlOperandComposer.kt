package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Contains a set of function to compose the operand part of a command
 */
class CommandControlOperandComposer {

    internal fun composeSnoozeAnnunciationOperand(operand: SnoozeAnnunciationOperand, dataWriter: DataWriter) {
        dataWriter.putInt(operand.id, IntFormat.FORMAT_UINT16)
    }
}