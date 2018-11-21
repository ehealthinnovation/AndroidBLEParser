package org.ehealthinnovation.android.bluetooth.common.racp

import java.util.*

/**
 * A super class for all racp commands (mainly concerning the Record Access Control Point)
 * @property uuid the UUID assigned by the Bluetooth Sig for Record Access Control Point
 */
abstract class RacpCommand {
    val uuid: UUID = UUID.randomUUID() //todo Change this to the bluetooth sig assiged uuid
}

/**
 * Abort the Record Access Control Point
 */
class AbortOperation : RacpCommand()

/**
 * Report records based on [operand]
 *
 * @property operand can be one of [SimpleOperand], [FilteredByDate], [FilteredByDateRange], [FilteredBySequenceNumber]
 * [FilteredBySequenceNumberRange]
 *
 */
class ReportRecords(val operand: CommandOperand) : RacpCommand()

/**
 * Delete records based on [operand]
 *
 * @property operand can be one of [SimpleOperand], [FilteredByDate], [FilteredByDateRange], [FilteredBySequenceNumber]
 * [FilteredBySequenceNumberRange]
 *
 */
class DeleteRecords(val operand: CommandOperand) : RacpCommand()

/**
 * Report the number of records based on [operand]
 *
 * @property operand can be one of [SimpleOperand], [FilteredByDate], [FilteredByDateRange], [FilteredBySequenceNumber]
 * [FilteredBySequenceNumberRange]
 *
 */
class ReportNumberOfRecords(val operand: CommandOperand) : RacpCommand()

