package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

/**
 * Base class for all command operand in a command control point command. Any specific operand for each
 * command should extend this class.
 */
abstract class CommandControlOperand

/**
 * The base operand for annunciation operation.
 * @property id the id of the annunciation to be operated on
 */
abstract class AnnunciationOperand(val id: Int) : CommandControlOperand()

/**
 * The operand for [SnoozeAnnunciation] command
 * @param id the id of the annunciation to be operated on
 */
class SnoozeAnnunciationOperand(id: Int) : AnnunciationOperand(id)

/**
 * The operand for [ConfirmAnnunciation] command
 * @param id the id of the annunciation to be operated on
 */
class ConfirmAnnunciationOperand(id: Int) : AnnunciationOperand(id)

/**
 * The operand containing a profile template number.
 * @property number a profile template number
 */
class ProfileTemplateNumber(val number: Int) : CommandControlOperand()

/**
 * The operand containing template number.
 * @property number a template number
 */
class TemplateNumber(val number: Int) : CommandControlOperand()

/**
 * The operand containing a bolus ID. Used in [CancelBolus]
 * @property id a unique identifier for a bolus created by the server application for a programmed bolus
 */
class BolusId(val id: Int): CommandControlOperand()

/**
 * The operand containing a list of template number to operate on
 * @property templateNumbers a list of template number to operate on
 */
class TemplatesOperand(val templateNumbers: List<TemplateNumber>): CommandControlOperand(){
    init {
        if (templateNumbers.isEmpty() || templateNumbers.size>14){
            throw IllegalArgumentException("Must provide 1 to 14 template numbers")
        }
    }
}

/**
 * The operand containing an insulin reservoir
 * @property level the reservoir level
 */
class ReservoirFillLevel(val level: Float): CommandControlOperand()

/**
 * The operand containing the amount to prime
 * @property amount the amount to prime
 */
class PrimingAmount(val amount: Float): CommandControlOperand()


/**
 * The operand containing the amount of maximum bolus a remote insulin device can deliver
 * @property amount the amount of the maximum bolus
 */
class MaxBolusAmount(val amount: Float): CommandControlOperand()