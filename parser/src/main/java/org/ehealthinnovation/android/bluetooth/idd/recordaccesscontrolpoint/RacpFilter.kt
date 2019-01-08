package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * The filter of the RACP specific to IDD
 *
 * Currently, IDD supports filtering measurement records based on sequence number, sequence number of
 * Reference Time Event (returned results only consist of Reference TIme Events), sequence number
 * of Non Reference Time Events (returned results only consist of Non Reference Time Events)
 */
enum class Filter(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SEQUENCE_NUMBER(0x0F),
    SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT(0x33),
    SEQUENCE_NUMBER_FILTERED_BY_NON_REFERENCE_TIME_EVENT(0x3C),
}