package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * The opcode of an RACP command in IDD. This set of opcode is the has the same names as those in CGM
 * and BGM, except the key values.
 */
enum class Opcode(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    REPORT_STORED_RECORDS(0x33),
    DELETE_STORED_RECORDS(0x3C),
    ABORT_OPERATION(0x55),
    REPORT_NUMBER_OF_STORED_RECORDS(0x5A),
    NUMBER_OF_STORED_RECORDS_RESPONSE(0x66),
    RESPONSE_CODE(0x0F);
}