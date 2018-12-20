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
 * A base class for write profile template command. Any command that write a profile template with ranged timeblocks should extend this class.
 * @property opcode The opcode of the write range profile template command.
 */
abstract class WriteRangeProfileTemplate(opcode: Opcode, val operand: WriteRangeProfileTemplateOperand) : CommandControlCommand(opcode)

/**
 * Write Target Glucose Range profile template command.
 * @property operand an operand containing [DoubleValueTimeBlock] configuration for a single command transaction.
 */
class WriteTargetGlucoseRangeProfileTemplate(operand: WriteRangeProfileTemplateOperand) : WriteRangeProfileTemplate(Opcode.WRITE_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE, operand)


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
 * Read the target Glucose range profile template of number specified in [operand]
 */
class ReadTargetGlucoseRangeProfileTemplate(operand: ProfileTemplateNumber) : ReadProfileTemplate(Opcode.READ_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE, operand)

/**
 * Read the I2CHO profile template of number specified in [operand]
 */
class ReadI2CHOProfileTemplate(operand: ProfileTemplateNumber) : ReadProfileTemplate(Opcode.READ_I2CHO_RATIO_PROFILE_TEMPLATE, operand)

/**
 * Read the ISF profile template of number specified in [operand]
 */
class ReadIsfProfileTemplate(operand: ProfileTemplateNumber) : ReadProfileTemplate(Opcode.READ_ISF_PROFILE_TEMPLATE, operand)

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
 * A base class for getting template
 * @param opcode the opcode for getting template
 * @property operand the template number to query
 */
abstract class GetTemplate(opcode: Opcode, val operand: TemplateNumber) : CommandControlCommand(opcode)

/**
 * A command to get TBR template
 * @property operand contains the tbr template number to query
 */
class GetTbrTemplate(operand: TemplateNumber) : GetTemplate(Opcode.GET_TBR_TEMPLATE, operand)

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

/**
 * A command to set a bolus template
 * @property operand contains the bolus template
 */
class SetBolusTemplate(val operand: SetBolusTemplateOperand) : CommandControlCommand(Opcode.SET_BOLUS_TEMPLATE)

/**
 * A command to get Bolus template
 * @property operand contains a bolus template number to query
 */
class GetBolusTemplate(operand: TemplateNumber) : GetTemplate(Opcode.GET_BOLUS_TEMPLATE, operand)

/**
 * A command to get template status and details
 */
class GetTemplatesStatusAndDetails : SimpleControlCommand(Opcode.GET_TEMPLATE_STATUS_AND_DETAILS)

/**
 * Base class for command that operate on a list of template and requires a list of template number as operand
 * @param opcode the exact operation to perform
 * @property operand contains a list of template number to perform operation on
 */
abstract class TemplatesOperation(opcode: Opcode, val operand: TemplatesOperand) : CommandControlCommand(opcode)

/**
 * A command to reset template slots specified in [ResetTemplateStatusOperand]
 */
class ResetTemplatesStatus(operand: TemplatesOperand) : TemplatesOperation(Opcode.RESET_TEMPLATE_STATUS, operand)

/**
 * A command to activate template specified in [TemplatesOperand]
 * @property operand a list of profile template numbers to be activated
 */
class ActivateProfileTemplates(operand: TemplatesOperand) : TemplatesOperation(Opcode.ACTIVATE_PROFILE_TEMPLATES, operand)

/**
 * A command to initial reservoir filled level [SetInitialReservoirFillLevel]
 * @property operand the fill level it set to
 */
class SetInitialReservoirFillLevel(val operand: ReservoirFillLevel) : CommandControlCommand(Opcode.SET_INITIAL_RESERVOIR_FILL_LEVEL)

/**
 * A Command to start priming the insulin infusion set
 * @property operand the amount to prime
 */
class StartPriming(val operand: PrimingAmount) : CommandControlCommand(Opcode.START_PRIMING)

/**
 * A command to stop the priming process
 */
class StopPriming : SimpleControlCommand(Opcode.STOP_PRIMING)
 
 /**
 * A command to get the activated profile templates
 */
class GetActivatedProfileTemplates: SimpleControlCommand(Opcode.GET_ACTIVATED_PROFILE_TEMPLATES)


