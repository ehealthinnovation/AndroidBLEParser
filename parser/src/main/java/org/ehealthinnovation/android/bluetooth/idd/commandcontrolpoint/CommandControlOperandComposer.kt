package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Contains a set of function to compose the operand part of a command
 */
class CommandControlOperandComposer {

    internal fun composeAnnunciationOperand(operand: AnnunciationOperand, dataWriter: DataWriter) {
        dataWriter.putInt(operand.id, IntFormat.FORMAT_UINT16)
    }

    internal fun composeProfileTemplateNumberOperand(operand: ProfileTemplateNumber, dataWriter: DataWriter) {
        dataWriter.putInt(operand.number, IntFormat.FORMAT_UINT8)
    }

    internal fun composeBolusIdOperand(operand: BolusId, dataWriter: DataWriter){
        dataWriter.putInt(operand.id, IntFormat.FORMAT_UINT16)
    }

}