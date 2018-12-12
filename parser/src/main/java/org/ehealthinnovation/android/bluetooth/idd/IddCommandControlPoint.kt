package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationOperand

/**
 * Base class for all command control point commands. Any specific command should extend this class
 */
abstract class CommandControlCommand

/**
 * A command to snooze an annunciation
 * @property operand an operand containing the id of an annunciation to snooze
 */
class SnoozeAnnunciation(val operand: SnoozeAnnunciationOperand) : CommandControlCommand()