package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.parser.isIntRangeValid


/**
 * The operands of filtering idd history data event records based on one sided criterion of sequence number
 *
 * @property sequenceNumber the sequence number in the history event. It is used in combination with [operator] to form a filtering criteria
 *
 * @property filter add an extra query result selection criteria
 *
 * @property operation choose one of the operations enumerated in [SingleBoundOperation]
 */
class FilteredBySequenceNumber(val sequenceNumber: Int, val filter: Filter, val operation: SingleBoundOperation) : CommandOperand()


/**
 * The operands of filtering history data records by a sequence number range INCLUSIVE
 *
 * @property startSequenceNumber the starting sequence number of history events
 *
 * @property endSequenceNumber the ending sequence number of history events
 * The [startSequenceNumber] should be smaller than or equal to the [endSequenceNumber]
 *
 * @property filter add an extra query result selection criteria
 *
 * @throws IllegalArgumentException if the input time offset range is invalid
 */
class FilteredBySequenceNumberRange(val startSequenceNumber: Int, val endSequenceNumber: Int, val filter: Filter) : CommandOperand() {
    /**
     * This operand is always defined for [Operator.WITHIN_RANGE_OF_INCLUSIVE].
     */
    val operation: Operator = Operator.WITHIN_RANGE_OF_INCLUSIVE

    init {
        if (!isIntRangeValid(startSequenceNumber, endSequenceNumber)) {
            throw IllegalArgumentException("Time offset range is invalid")
        }
    }
}

/**
 * The operands of filtering history event records with simple operation, which does not require sequence number input parameters.
 *
 * @property operation the simple operation including [SimpleOperation.ALL_RECORDS], [SimpleOperation.FIRST_RECORD] and [SimpleOperation.LAST_RECORD]
 *
 * @property filter the additional selection filter associated with the [SimpleOperation]
 *
 */
class SimpleOperationWithFilter(val operation: SimpleOperation, val filter: Filter) : CommandOperand()


