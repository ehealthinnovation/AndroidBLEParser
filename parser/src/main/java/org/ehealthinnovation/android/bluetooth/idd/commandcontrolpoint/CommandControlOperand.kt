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
