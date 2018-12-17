package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.*
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationOperand
import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import java.lang.IllegalArgumentException

/**
 * Composer helps to serialize a Idd Command Control Point Command into a buffer
 */
class IddCommandControlPointComposer : CharacteristicComposer<CommandControlCommand> {

    private val operandComposer = CommandControlOperandComposer()

    override fun canCompose(request: CommandControlCommand): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun compose(request: CommandControlCommand, dataWriter: DataWriter) {
        when (request) {
            is SnoozeAnnunciation -> composeSnoozeAnnunciation(request.operand, dataWriter)
            is SetTbrAdjustment -> composeSetTbrAdjustment(request.operand, dataWriter)
            else -> IllegalArgumentException("request not supported")
        }
    }

    internal fun composeSnoozeAnnunciation(operand: SnoozeAnnunciationOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.SNOOZE_ANNUNCIATION.key, IntFormat.FORMAT_UINT16)
        operandComposer.composeSnoozeAnnunciationOperand(operand, dataWriter)
    }

    internal fun composeSetTbrAdjustment(operand: TbrAdjustmentOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.SET_TBR_ADJUSTMENT.key, IntFormat.FORMAT_UINT16)
        SetTbrAdjustmentOperandComposer().composeOperand(operand, dataWriter)
    }
}