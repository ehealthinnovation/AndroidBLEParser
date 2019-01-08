package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.parser.IddUuid
import java.util.*

/**
 * A super class for all racp commands (mainly concerning the Record Access Control Point)
 * @property uuid the UUID assigned by the Bluetooth Sig for Record Access Control Point
 */
abstract class RacpCommand {
    val uuid: UUID = IddUuid.RECORD_ACCESS_CONTROL_POINT.uuid
}

/**
 * Abort the Record Access Control Point Command
 */
class AbortOperation : RacpCommand()

/**
 * Report records based on [operand]
 *
 * @property operand can be one of [SimpleOperationWithFilter], [FilteredBySequenceNumber], [FilteredBySequenceNumberRange]
 */
class ReportRecords(val operand: CommandOperand) : RacpCommand()

/**
 * Delete records based on [operand]
 *
 * @property operand can be one of [SimpleOperationWithFilter], [FilteredBySequenceNumber], [FilteredBySequenceNumberRange]
 */
class DeleteRecords(val operand: CommandOperand) : RacpCommand()

/**
 * Report the number of records based on [operand]
 *
 * @property operand can be one of [SimpleOperationWithFilter], [FilteredBySequenceNumber], [FilteredBySequenceNumberRange]
 */
class ReportNumberOfRecords(val operand: CommandOperand) : RacpCommand()

