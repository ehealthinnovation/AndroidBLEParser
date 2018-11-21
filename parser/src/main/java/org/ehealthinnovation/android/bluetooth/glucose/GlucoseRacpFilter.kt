package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * The filter field of the RACP
 */
enum class Filter(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SEQUENCE_NUMBER(1),
    USER_FACING_TIME(2);
}