package org.ehealthinnovation.android.bluetooth.common.racp

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * A subset of [Operator] containing only simple operations.
 */
enum class SimpleOperation(val key: Int) {
    ALL_RECORDS(Operator.ALL_RECORDS.key),
    FIRST_RECORD(Operator.FIRST_RECORD.key),
    LAST_RECORD(Operator.LAST_RECORD.key);
}

/**
 * A subset of [Operator] that needs to specify one range bound.
 */
enum class SingleBoundOperation(val key: Int) {
    LESS_THAN_OR_EQUAL_TO(Operator.LESS_THAN_OR_EQUAL_TO.key),
    GREATER_THAN_OR_EQUAL_TO(Operator.GREATER_THAN_OR_EQUAL_TO.key);
}

/**
 * The operator field of an RACP command
 */
enum class Operator(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    NULL(0),
    ALL_RECORDS(1),
    LESS_THAN_OR_EQUAL_TO(2),
    GREATER_THAN_OR_EQUAL_TO(3),
    WITHIN_RANGE_OF_INCLUSIVE(4),
    FIRST_RECORD(5),
    LAST_RECORD(6);
}