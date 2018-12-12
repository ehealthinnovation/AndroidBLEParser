package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

/**
 * Base class for all command operand in a command control point command. Any specific operand for each
 * command should extend this class.
 */
abstract class CommandControlOperand

/**
 * The operand for [SnoozeAnnunciation] command
 * @property id the is of the annunciation to be snoozed
 */
class SnoozeAnnunciationOperand(val id: Int) : CommandControlOperand()

