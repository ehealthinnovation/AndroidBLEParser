package org.ehealthinnovation.android.bluetooth.idd

import java.util.*

/**
 * Parent class for all Status Reader Control Command operands.
 */
abstract class StatusReaderControlOperand

/** Operand for [ResetStatus] command
 * @property flagsToReset indicates the bits in the flag field to reset.
 */
data class StatusFlagToReset(
        val flagsToReset: EnumSet<Status>
) : StatusReaderControlOperand()

/**
 * Operand for [GetCounter] command
 * @property type the type of counter to select
 * @property valueSelection the type of the value of a counter to query
 * @see CounterValueSelection
 */
data class GetCounterOperand(
        val type: CounterType,
        val valueSelection: CounterValueSelection
)