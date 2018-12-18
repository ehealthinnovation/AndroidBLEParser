package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.*

/**
 * Base class for all command control point commands. Any specific command should extend this class
 */
abstract class CommandControlCommand(val opcode: Opcode)

/**
 * A command to snooze an annunciation
 * @property operand an operand containing the id of an annunciation to snooze
 */
class SnoozeAnnunciation(val operand: SnoozeAnnunciationOperand) : CommandControlCommand(Opcode.SNOOZE_ANNUNCIATION)

/**
 * A base class for write profile template command. Any command that write a profile template should extend this class.
 * @property opcode The opcode of the write profile template command.
 */
abstract class WriteProfileTemplate(opcode: Opcode, val operand: WriteProfileTemplateOperand) : CommandControlCommand(opcode)

/**
 * Write basal rate profile template command.
 * @property operand an operand containing [BasalRateProfileTemplateTimeBlock] configuration for a single command transaction.
 */
class WriteBasalRateProfileTemplate(operand: WriteProfileTemplateOperand) : WriteProfileTemplate(Opcode.WRITE_BASAL_RATE_PROFILE_TEMPLATE, operand)

/**
 * Write ISF profile template command.
 * @property operand an operand containing [IsfProfileTemplateTimeBlock] configuration for a single command transaction.
 */
class WriteIsfProfileTemplate(operand: WriteProfileTemplateOperand) : WriteProfileTemplate(Opcode.WRITE_ISF_PROFILE_TEMPLATE, operand)

/**
 * Write I2CHO ratio profile template command.
 * @property operand an operand containing [I2CHOProfileTemplateTimeBlock] configuration for a single command transaction.
 */
class WriteI2CHORatioProfileTemplate(operand: WriteProfileTemplateOperand) : WriteProfileTemplate(Opcode.WRITE_I2CHO_RATIO_PROFILE_TEMPLATE, operand)

/**
 * A command to confirm an annunciation
 * @property operand an operand containing the id of an annunciation to confirm
 */
class ConfirmAnnunciation(val operand: ConfirmAnnunciationOperand) : CommandControlCommand(Opcode.CONFIRM_ANNUNCIATION)

/**
 * Base class for read profile template series of commands.
 * @param opcode the opcode for reading profile template
 * @property operand operand containing the profile template number to read
 */
abstract class ReadProfileTemplate(opcode: Opcode, val operand: ProfileTemplateNumber) : CommandControlCommand(opcode)

/**
 * Read the Basal Rate Profile Template of number specified in [operand]
 */
class ReadBasalRateProfileTemplate(operand: ProfileTemplateNumber) : ReadProfileTemplate(Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE, operand)

/**
 * Base class for commands that don't need an operand
 * @param opcode the opcode of the command
 */
abstract class SimpleControlCommand(opcode: Opcode) : CommandControlCommand(opcode)

/**
 * Cancel tha currently active TBR
 */
class CancelTbrAdjustment : SimpleControlCommand(Opcode.CANCEL_TBR_ADJUSTMENT)

/**
 * A command to set the TBR
 * @property operand contains the configuration of the TBR
 */
class SetTbrAdjustment(val operand: TbrAdjustmentOperand) : CommandControlCommand(Opcode.SET_TBR_ADJUSTMENT)

/**
 * Get Available Boluses
 */
class GetAvailableBolus : SimpleControlCommand(Opcode.GET_AVAILABLE_BOLUSES)

/**
 * A command to cancel a bolus
 * @property operand contains the bolus id
 */
class CancelBolus(val operand: BolusId) : CommandControlCommand(Opcode.CANCEL_BOLUS)

/**
 * A command to set a Bolus
 * @property operand contains the configuration of the bolus
 */
class SetBolus(val operand: BolusConfig) : CommandControlCommand(Opcode.SET_BOLUS)
