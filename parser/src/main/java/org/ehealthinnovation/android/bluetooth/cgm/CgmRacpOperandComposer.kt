package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.common.racp.Operator
import org.ehealthinnovation.android.bluetooth.common.racp.RacpOperandComposer
import org.ehealthinnovation.android.bluetooth.common.racp.SimpleOperand
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

class CgmRacpOperandComposer : RacpOperandComposer(){
        /**
         * Compose [operand] and write into buffer through [dataWriter]
         *
         * [operand] must be subclass of [CommandOperand]
         *
         * @throws IllegalAccessException if operand class is not supported
         */
        override fun compose(operand: CommandOperand, dataWriter: DataWriter) {
            when (operand) {
                is SimpleOperand -> super.composeSimpleOperand(operand, dataWriter)
                is FilteredByTimeOffset -> composeTimeOffsetOperand(operand, dataWriter)
                is FilteredByTimeOffsetRange -> composeTimeOffsetRangeOperand(operand, dataWriter)
                else -> {
                    throw IllegalArgumentException("Operand not supported in this function")
                }
            }
        }

        /**
         * Compose the [operand] for filtering with single bounded time offset into [dataWriter]
         */
        internal fun composeTimeOffsetOperand(operand: FilteredByTimeOffset, dataWriter: DataWriter) {
            dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(org.ehealthinnovation.android.bluetooth.cgm.Filter.TIME_OFFSET.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(operand.timeOffset, IntFormat.FORMAT_UINT16)
        }

        /**
         * Compose the [operand] for filtering with time offset range into [dataWriter]
         */
        internal fun composeTimeOffsetRangeOperand(operand: FilteredByTimeOffsetRange, dataWriter: DataWriter) {
            dataWriter.putInt(Operator.WITHIN_RANGE_OF_INCLUSIVE.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(org.ehealthinnovation.android.bluetooth.cgm.Filter.TIME_OFFSET.key, IntFormat.FORMAT_UINT8)
            dataWriter.putInt(operand.startTimeOffset, IntFormat.FORMAT_UINT16)
            dataWriter.putInt(operand.endTimeOffset, IntFormat.FORMAT_UINT16)
        }
    }
