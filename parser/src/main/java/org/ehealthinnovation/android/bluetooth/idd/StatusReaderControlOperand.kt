package org.ehealthinnovation.android.bluetooth.idd

import java.util.*

/**
 * Parent class for all Status Reader Control Command operands.
 */
abstract class StatusReaderControlOperand

/** Operand for [ResetStatus] command
 * @property flagsToReset indicates the bits in the flag field to reset.
 */
class StatusFlagToReset(
        val flagsToReset: EnumSet<Status>
) : StatusReaderControlOperand()