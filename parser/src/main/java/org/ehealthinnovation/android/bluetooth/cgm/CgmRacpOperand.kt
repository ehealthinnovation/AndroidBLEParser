package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.common.racp.SingleBoundOperation
import org.ehealthinnovation.android.bluetooth.parser.isIntRangeValid


/**
 * The operands of filtering cgm records based on one sided criterion of [timeOffset]
 *
 * @property timeOffset the time offset in minutes from the session start time.
 * It is used in combination with [operator] to form a filtering criteria
 *
 * @property operation choose one of the operations enumerated in [SingleBoundOperation]
 */
class FilteredByTimeOffset(val timeOffset: Int, val operation: SingleBoundOperation) : CommandOperand()


/**
 * The operands of filtering cgm records by a [timeOffset] range INCLUSIVE
 *
 * @property startTimeOffset the starting time offset in minutes from the session start time.
 *
 * @property endTimeOffset the ending time offset in minutes from the session start time.
 *
 * The [startTimeOffset] should be smaller than the [endTimeOffset]
 *
 * @throws IllegalArgumentException if the input time offset range is invalid
 */
class FilteredByTimeOffsetRange(val startTimeOffset: Int, val endTimeOffset: Int) : CommandOperand(){
    init {
        if (!isIntRangeValid(startTimeOffset, endTimeOffset)) {
            throw IllegalArgumentException("Time offset range is invalid")
        }
    }
}


