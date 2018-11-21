package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.common.racp.SingleBoundOperation
import org.ehealthinnovation.android.bluetooth.parser.BluetoothDateTime
import java.util.*

/**
 * The operands of filtering glucose records based on one sided criterion for glucose devices
 *
 * @property date the date used in combination with [operator] to form meaningful filtering criteria
 *
 * @property operation choose one of the operations enumerated in [SingleBoundOperation]
 */
class FilteredByDate(val date: Date, val operation: SingleBoundOperation) : CommandOperand()

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
 * @property operation choose one of the operations enumerated in [SingleBoundOperation]
 */
class FilteredBySequenceNumber(val sequenceNumber: Int, val operation: SingleBoundOperation) : CommandOperand()

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
 * The operands of filtering glucose records based on one sided criterion on Bluetooth DateTime for glucose devices
 *
 * @property date the Bluetooth DateTime used in filtering
 *
 * @property operation choose one of the operations enumerated in [SingleBoundOperation]
 */
class FilteredByBluetoothDateTime(val date: BluetoothDateTime, val operation: SingleBoundOperation) : CommandOperand()

/**
 * The operands of filtering records by a Bluetooth DateTime range INCLUSIVE
 *
 * @property startDate the starting date of the range
 *
 * @property higherSequenceNumber the ending date of the range
 *
 */
class FilteredByBluetoothDateTimeRange(val startDate: BluetoothDateTime, val endDate: BluetoothDateTime) : CommandOperand()