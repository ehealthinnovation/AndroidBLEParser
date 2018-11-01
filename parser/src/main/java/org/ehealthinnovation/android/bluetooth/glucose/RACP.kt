package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue


/**
 * A class representing the general response of an RACP request
 * @property requestOperation the request operation
 * @property response the response of the requested operation
 */
class RacpGeneralResponse(
        val requestOperation: Opcode,
        val response: ResponseCode
)

/**
 * A class representing the result of get record number
 * @property number the number of records found in the device
 */
class RacpGetRecordNumberResponse(
        val number: Int
)

/**
 * The filter field of the RACP
 */
enum class Filter(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SEQUENCE_NUMBER(1),
    USER_FACING_TIME(2);
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

/**
 * The response code from an RACP command
 */
enum class ResponseCode(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SUCCESS(1),
    OP_CODE_NOT_SUPPORTED(2),
    INVALID_OPERATOR(3),
    OPERATOR_NOT_SUPPORTED(4),
    INVALID_OPERAND(5),
    NO_RECORDS_FOUND(6),
    ABORT_UNSUCCESSFUL(7),
    PROCEDURE_NOT_COMPLETED(8),
    OPERAND_NOT_SUPPORTED(9);
}



