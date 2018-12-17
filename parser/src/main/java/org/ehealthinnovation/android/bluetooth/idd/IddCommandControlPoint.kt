package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationOperand
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.TbrAdjustmentOperand

/**
 * Base class for all command control point commands. Any specific command should extend this class
 */
abstract class CommandControlCommand

/**
 * A command to snooze an annunciation
 * @property operand an operand containing the id of an annunciation to snooze
 */
class SnoozeAnnunciation(val operand: SnoozeAnnunciationOperand) : CommandControlCommand()

/**
 * A command to set the TBR
 * @property operand contains the configuration of the TBR
 */
class SetTbrAdjustment(val operand: TbrAdjustmentOperand) : CommandControlCommand()