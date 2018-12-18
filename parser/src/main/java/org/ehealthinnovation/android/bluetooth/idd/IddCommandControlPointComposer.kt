package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.*
import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataReader
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
            is ConfirmAnnunciation -> composeConfirmAnnunciation(request.operand, dataWriter)
            is ReadBasalRateProfileTemplate -> composeReadProfileTemplate(Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE, request.operand, dataWriter)
            is CancelTbrAdjustment -> composeSimpleCommand(request, dataWriter)
            is SetTbrAdjustment -> composeSetTbrAdjustment(request.operand, dataWriter)
            is CancelBolus -> composeCancelBolus(request, dataWriter)
            is SetBolus -> composeSetBolus(request, dataWriter)
            else -> IllegalArgumentException("request not supported")
        }
    }

    internal fun composeSnoozeAnnunciation(operand: SnoozeAnnunciationOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.SNOOZE_ANNUNCIATION.key, IntFormat.FORMAT_UINT16)
        operandComposer.composeAnnunciationOperand(operand, dataWriter)
    }

    internal fun composeConfirmAnnunciation(operand: ConfirmAnnunciationOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.CONFIRM_ANNUNCIATION.key, IntFormat.FORMAT_UINT16)
        operandComposer.composeAnnunciationOperand(operand, dataWriter)
    }

    internal fun composeReadProfileTemplate(opcode: Opcode, operand: ProfileTemplateNumber, dataWriter: DataWriter) {
        dataWriter.putInt(opcode.key, IntFormat.FORMAT_UINT16)
        operandComposer.composeProfileTemplateNumberOperand(operand, dataWriter)
    }

    internal fun composeSimpleCommand(request: SimpleControlCommand, dataWriter: DataWriter){
        dataWriter.putInt(request.opcode.key, IntFormat.FORMAT_UINT16)
    }
    
    internal fun composeSetTbrAdjustment(operand: TbrAdjustmentOperand, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.SET_TBR_ADJUSTMENT.key, IntFormat.FORMAT_UINT16)
        SetTbrAdjustmentOperandComposer().composeOperand(operand, dataWriter)
    }

    internal fun composeCancelBolus(request: CancelBolus, dataWriter: DataWriter) {
        dataWriter.putInt(request.opcode.key, IntFormat.FORMAT_UINT16)
        operandComposer.composeBolusIdOperand(request.operand, dataWriter)
    }

    internal fun composeSetBolus(request: SetBolus, dataWriter: DataWriter){
       dataWriter.putInt(request.opcode.key, IntFormat.FORMAT_UINT16)
        SetBolusComposer().compose(request.operand, dataWriter)
    }

}