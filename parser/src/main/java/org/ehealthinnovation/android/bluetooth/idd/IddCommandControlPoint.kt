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
class ReadBasalRateProfileTemplate(opcode:Opcode, operand: ProfileTemplateNumber) : ReadProfileTemplate(opcode, operand)

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
