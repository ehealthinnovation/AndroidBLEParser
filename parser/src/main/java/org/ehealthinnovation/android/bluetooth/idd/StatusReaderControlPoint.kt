package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.IddUuid
import java.util.*

/**
 * A parent class for commands to the Idd Status Reader Control Point. Any command to be fed into a
 * [IddStatusReaderControlComposer] needs to extend this class.
 */
abstract class StatusReaderControlCommand {
    val uuid: UUID = IddUuid.STATUS_READER_CONTROL_POINT.uuid
}

/**
 * Use this command to reset the Insulin Delivery Device Status Change Characteristic
 */
data class ResetStatus(val operand: StatusFlagToReset) : StatusReaderControlCommand()

/**
 * A parent class for all response from IDD status Reader Control point. Any response from the
 * [IddStatusReaderControlPointParser] must be a subclass of it.
 */
abstract class StatusReaderControlResponse

/**
 * The general response from the [IddStatusReaderControlPointParser]
 */
data class StatusReaderControlGeneralResponse(
        val requestOperation: StatusReaderControlOpcode,
        val response: StatusReaderControlResponseCode
) : StatusReaderControlResponse()

enum class StatusReaderControlResponseCode(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),

    /**Normal response for successful procedure.  */
    SUCCESS(0x0F),
    /**Normal response if unsupported Op Code is received. */
    OP_CODE_NOT_SUPPORTED(0x70),
    /**Normal response if Operand received does not meet the requirements of the service. */
    INVALID_OPERAND(0x71),
    /**Normal response if unable to complete a procedure for any reason. */
    PROCEDURE_NOT_COMPLETED(0x72),
    /**Normal response if Operand received does not meet the range requirements of the service. */
    PARAMETER_OUT_OF_RANGE(0x73),
    /**Normal response if the procedure cannot be executed because it is not applicable in the current Server Application context. */
    PROCEDURE_NOT_APPLICABLE(0x74);
}

enum class StatusReaderControlOpcode(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),

    /**Indicate it is a response from previous request */
    RESPONSE_CODE(0x0303),
    /** Resets the status exposed by the IDD Status Changed characteristic. The response to this control point is Response Code. */
    RESET_STATUS(0x030C),
    /**Gets the IDs of all Active Boluses (up to 7 Bolus IDs can be retrieved). The normal response to this control point is Get Active Bolus ID Response. For error conditions, the response is Response Code. */
    GET_ACTIVE_BOLUS_IDS(0x0330),
    /** This is the normal response to Get Active Bolus IDs. */
    GET_ACTIVE_BOLUS_IDS_RESPONSE(0x033F),
    /** Gets information about an Active Bolus identified by the given ID. The normal response to this control point is Get Active Bolus Delivery Response. For error conditions, the response is Response Code. */
    GET_ACTIVE_BOLUS_DELIVERY(0x0356),
    /** This is the normal response to Get Active Bolus Delivery. */
    GET_ACTIVE_BOLUS_DELIVERY_RESPONSE(0x0359),
    /**Gets the current active basal rate setting, including the TBR. The normal response to this control point is Get Active Basal Rate Delivery Response. For error conditions, the response is Response Code. */
    GET_ACTIVE_BASAL_RATE_DELIVERY(0x0365),
    /** This is the normal response to Get Active Basal Rate Delivery.  */
    GET_ACTIVE_BASAL_RATE_DELIVERY_RESPONSE(0x036A),
    /**Gets the total daily delivered bolus and basal insulin from midnight until now. When the day is done and as soon as midnight is reached, the total daily delivered amounts are reset. The normal response to this control point is Get Total Daily Insulin Status Response. For error conditions, the response is Response Code. */
    GET_TOTAL_DAILY_INSULIN_STATUS(0x0395),
    /**This is the normal response to Get Total Daily Insulin Status. */
    GET_TOTAL_DAILY_INSULIN_STATUS_RESPONSE(0x039A),
    /**Gets the value about an internal counter of the Insulin Delivery Device (e.g., the remaining warranty). The normal response to this control point is Get Counter Response. For error conditions, the response is Response Code. */
    GET_COUNTER(0x03A6),
    /**This is the normal response to procedure Get Counter.*/
    GET_COUNTER_RESPONSE(0x03A9),
    /**Gets the delivered amount of bolus and basal insulin since the last rollover of these amounts. The Server rollover behavior shall be defined per Service. The normal response to this control point is Get Delivered Insulin Response. For error conditions, the response is Response Code. */
    GET_DELIVERED_INSULIN(0x03C0),
    /**This is the normal response to procedure Get Delivered Insulin. */
    GET_DELIVERED_INSULIN_RESPONSE(0x03CF),
    /**Gets the insulin on board, which has been delivered by the Insulin Delivery Device. The normal response to this control point is Get Insulin On Board Response. For error conditions, the response is Response Code. */
    GET_INSULIN_ON_BOARD(0x03F3),
    /**This is the normal response to procedure Get Insulin On Board.  */
    GET_INSULIN_ON_BOARD_RESPONSE(0x03FC);


}