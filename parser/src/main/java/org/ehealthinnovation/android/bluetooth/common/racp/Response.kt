package org.ehealthinnovation.android.bluetooth.common.racp

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue


abstract class RacpResponse

/**
 * A class representing the general response of an RACP request
 * @property requestOperation the request operation
 * @property response the response of the requested operation
 */
data class RacpGeneralResponse(
        val requestOperation: Opcode,
        val response: RacpResponseCode
): RacpResponse()

/**
 * A class representing the result of get record number
 * @property number the number of records found in the device
 */
data class RacpGetRecordNumberResponse(
        val number: Int
): RacpResponse()


/**
 * The response code from an RACP command
 */
enum class RacpResponseCode(override val key: Int) : EnumerationValue {
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