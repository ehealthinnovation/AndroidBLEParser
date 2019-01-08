package org.ehealthinnovation.android.bluetooth.common.racp

/**
 * Simple operations for records access
 *
 * @property operation choose one of the operations enumerated in [SimpleOperation]
 */
class SimpleOperand(val operation: SimpleOperation) : CommandOperand()

/**
 * The super class for command operands. Any racp command operand should extend this class.
 */
abstract class CommandOperand
