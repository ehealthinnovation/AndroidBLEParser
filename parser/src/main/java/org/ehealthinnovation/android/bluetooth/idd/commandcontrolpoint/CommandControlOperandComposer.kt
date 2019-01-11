package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.SetInitialReservoirFillLevel
import org.ehealthinnovation.android.bluetooth.idd.TherapyControlState
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat
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

    internal fun composeTemplateNumberOperand(operand: TemplateNumber, dataWriter: DataWriter) {
        dataWriter.putInt(operand.number, IntFormat.FORMAT_UINT8)
    }

    internal fun composeBolusIdOperand(operand: BolusId, dataWriter: DataWriter){
        dataWriter.putInt(operand.id, IntFormat.FORMAT_UINT16)
    }

    internal fun composeTemplatesNumberListOperand(operand: TemplatesOperand, dataWriter: DataWriter){
        dataWriter.putInt(operand.templateNumbers.size, IntFormat.FORMAT_UINT8)
        for (templateNumber in operand.templateNumbers){
            dataWriter.putInt(templateNumber.number, IntFormat.FORMAT_UINT8)
        }
    }


    internal fun composeSetInitialReservoirFillLevel(operand: ReservoirFillLevel, dataWriter: DataWriter) {
        dataWriter.putFloat(operand.level, -1, FloatFormat.FORMAT_SFLOAT)
    }

    internal fun composePrimeAmountOperand(operand: PrimingAmount, dataWriter: DataWriter){
        dataWriter.putFloat(operand.amount,-1, FloatFormat.FORMAT_SFLOAT)
    }

    internal fun composeMaxBolusAmountOperand(operand: MaxBolusAmount, dataWriter: DataWriter){
        dataWriter.putFloat(operand.amount, -1, FloatFormat.FORMAT_SFLOAT)
    }

    internal fun composeTherapyControlStateOperand(operand: TherapyControlState, dataWriter: DataWriter){
        dataWriter.putInt(operand.key, IntFormat.FORMAT_UINT8)
    }

    internal fun composeSetTbrTemplate(operand: SetTbrAdjustmentTemplateOperand, dataWriter: DataWriter){
        dataWriter.putInt(operand.templateNumber, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.type.key, IntFormat.FORMAT_UINT8)
        dataWriter.putFloat(operand.value, -1, FloatFormat.FORMAT_SFLOAT)
        dataWriter.putInt(operand.duration, IntFormat.FORMAT_UINT16)
    }

}