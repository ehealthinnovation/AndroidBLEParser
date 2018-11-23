package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * The filter of the RACP specific to CGM
 *
 * Currently, CGM supports filtering measurement records based on time offset from the session
 * start time.
 */
enum class Filter(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    TIME_OFFSET(1);
}