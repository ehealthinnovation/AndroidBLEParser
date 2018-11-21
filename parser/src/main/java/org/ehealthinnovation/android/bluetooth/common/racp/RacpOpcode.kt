package org.ehealthinnovation.android.bluetooth.common.racp

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * The opcode of an RACP command
 */
enum class Opcode(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    REPORT_STORED_RECORDS(1),
    DELETE_STORED_RECORDS(2),
    ABORT_OPERATION(3),
    REPORT_NUMBER_OF_STORED_RECORDS(4),
    NUMBER_OF_STORED_RECORDS_RESPONSE(5),
    RESPONSE_CODE(6);
}