package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

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
 * The operator field of an RACP command for IDD. It has the same labels as those in CGM and BGM,
 * except the key values.
 */
enum class Operator(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    NULL(0x0F),
    ALL_RECORDS(0x33),
    LESS_THAN_OR_EQUAL_TO(0x3C),
    GREATER_THAN_OR_EQUAL_TO(0x55),
    WITHIN_RANGE_OF_INCLUSIVE(0x5A),
    FIRST_RECORD(0x66),
    LAST_RECORD(0x69);
}