package org.ehealthinnovation.android.bluetooth.glucose

import java.util.*


/**
 * A super class for all glucose commands (mainly concerning the Record Access Control Point)
 * @property uuid the UUID assigned by the Bluetooth Sig for Record Access Control Point
 */
abstract class GlucoseCommand {
    val uuid: UUID = UUID.randomUUID() //todo Change this to the bluetooth sig assiged uuid
}

/**
 * The following commands are usable by the users
 */

/**
 * Abort the Record Access Control Point
 */
class AbortOperation : GlucoseCommand()

/**
 * Report glucose measurements based on [operand]
 *
 * @property operand can be one of [SimpleOperand], [FilteredByDate], [FilteredByDateRange], [FilteredBySequenceNumber]
 * [FilteredBySequenceNumberRange]
 *
 */
class ReportRecords(val operand: CommandOperand) : GlucoseCommand()

/**
 * Delete glucose measurements based on [operand]
 *
 * @property operand can be one of [SimpleOperand], [FilteredByDate], [FilteredByDateRange], [FilteredBySequenceNumber]
 * [FilteredBySequenceNumberRange]
 *
 */
class DeleteRecords(val operand: CommandOperand) : GlucoseCommand()

/**
 * Report the number of records based on [operand]
 *
 * @property operand can be one of [SimpleOperand], [FilteredByDate], [FilteredByDateRange], [FilteredBySequenceNumber]
 * [FilteredBySequenceNumberRange]
 *
 */
class ReportNumberOfRecords(val operand: CommandOperand) : GlucoseCommand()

/**
 * Simple operations for glucose devices
 *
 * @property operation choose one of the operations enumerated in [GlucoseSimpleOperation]
 */
class SimpleOperand(val operation: GlucoseSimpleOperation) : CommandOperand()

/**
 * The operands of filtering glucose records based on one sided criterion for glucose devices
 *
 * @property date the date used in combination with [operator] to form meaningful filtering criteria
 *
 * @property operation choose one of the operations enumerated in [GlucoseOperatorBound]
 */
class FilteredByDate(val date: Date, val operation: GlucoseOperatorBound) : CommandOperand()


/**
 * The operands of filtering records by a date range INCLUSIVE
 *
 * @property startDate the starting date of the date range
 *
 * @property endDate the ending date of the date range
 *
 */
class FilteredByDateRange(val startDate: Date, val endDate: Date) : CommandOperand()

/**
 * The operands of filtering glucose records based on one sided criterion for glucose devices
 *
 * @property sequenceNumber the sequence number used in filtering
 *
 * @property operation choose one of the operations enumerated in [GlucoseOperatorBound]
 */
class FilteredBySequenceNumber(val sequenceNumber: Int, val operation: GlucoseOperatorBound) : CommandOperand()

/**
 * The operands of filtering records by a sequence number range INCLUSIVE
 *
 * @property lowerSequenceNumber the starting sequence number of the number range
 *
 * @property higherSequenceNumber the ending sequence number of the number range
 *
 */
class FilteredBySequenceNumberRange(val lowerSequenceNumber: Int, val higherSequenceNumber: Int) : CommandOperand()

/**
 * The super class for command operands. Any glucose command operand should extend this class.
 */
abstract class CommandOperand

/**
 * A subset of [Operator] containing only simple operations.
 */
enum class GlucoseSimpleOperation(val key: Int) {
    ALL_RECORDS(Operator.ALL_RECORDS.key),
    FIRST_RECORD(Operator.FIRST_RECORD.key),
    LAST_RECORD(Operator.LAST_RECORD.key);
}

/**
 * A subset of [Operator] that needs one argument
 */
enum class GlucoseOperatorBound(val key: Int) {
    LESS_THAN_OR_EQUAL_TO(Operator.LESS_THAN_OR_EQUAL_TO.key),
    GREATER_THAN_OR_EQUAL_TO(Operator.GREATER_THAN_OR_EQUAL_TO.key);
}
