package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.ProfileTemplateNumber
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationOperand
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.TbrAdjustmentOperand

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
 * Base class for read profile template series of commands.
 * @property operand operand containing the profile template number to read
 */
abstract class ReadProfileTemplate(val operand: ProfileTemplateNumber) : CommandControlCommand()

/**
 * Read the Basal Rate Profile Template of number specified in [operand]
 */
class ReadBasalRateProfileTemplate(operand: ProfileTemplateNumber) : ReadProfileTemplate(operand)

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
class SetTbrAdjustment(val operand: TbrAdjustmentOperand) : CommandControlCommand()
