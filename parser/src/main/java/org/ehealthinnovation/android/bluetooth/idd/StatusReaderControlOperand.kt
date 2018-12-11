package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
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
/**
 * Operand for [GetActiveBolusDelivery] command
 * @property id The Bolus ID field represents a unique identifier as a uint16 data type created by the Server Application for a programmed bolus.
 * @property valueType A type of possible bolus delivery value
 */
class ActiveBolusDelivery(
        val id: Int,
        val valueType: BolusValueSelection
) : StatusReaderControlOperand()

enum class BolusValueSelection(override val key: Int) : EnumerationValue {
    PROGRAMMED(0x0F),
    REMAINING(0x33),
    DELIVERED(0x3C);
}
