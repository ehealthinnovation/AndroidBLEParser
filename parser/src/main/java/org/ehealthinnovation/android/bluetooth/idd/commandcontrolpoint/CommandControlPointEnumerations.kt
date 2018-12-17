package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * Opcode fireld of a Command control point command.
 */
enum class Opcode(override val key: Int) : EnumerationValue {

    RESERVED_FOR_FUTURE_USE(-1),

    RESPONSE_CODE(0x0F55),
    /**Set the therapy control state of the Insulin Delivery Device. The response to this control point is Response Code. */
    SET_THERAPY_CONTROL_STATE(0x0F5A),
    /**Activates the flight mode of the Insulin Delivery Device. The response to this control point is Response Code. */
    SET_FLIGHT_MODE(0x0F66),
    /**Snoozes an annunciation for a limited amount of time. The normal response to this control point is Snooze Annunciation Response. For error conditions, the response is Response Code. */
    SNOOZE_ANNUNCIATION(0x0F69),
    /**This is the normal response to Snooze Annunciation. */
    SNOOZE_ANNUNCIATION_RESPONSE(0x0F96),
    /**Confirms an annunciation on the Server Application and removes this specific annunciation from the list of currently active annunciations on the Server Application. The normal response to this control point is Confirm Annunciation Response. For error conditions, the response is Response Code. */
    CONFIRM_ANNUNCIATION(0x0F99),
    /**This is the normal response to Confirm Annunciation. */
    CONFIRM_ANNUNCIATION_RESPONSE(0x0FA5),
    /**Reads a specific Basal Rate Profile Template. The response to this control point is Response Code. */
    READ_BASAL_RATE_PROFILE_TEMPLATE(0x0FAA),
    /**This response is used to report one or more time blocks of a Basal Rate Profile Template. */
    READ_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE(0x0FC3),
    /**Writes a specific Basal Rate Profile Template. The normal response to this control point is Write Basal Rate Profile Template Response. For error conditions, the response is Response Code. */
    WRITE_BASAL_RATE_PROFILE_TEMPLATE(0x0FCC),
    /**This is the normal response to Write Basal Rate Profile Template. */
    WRITE_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE(0x0FF0),
    /**Sets a new or changes a currently active TBR. The response to this control point is Response Code. */
    SET_TBR_ADJUSTMENT(0x0FFF),
    /**Cancels a currently active TBR. The response to this control point is Response Code. */
    CANCEL_TBR_ADJUSTMENT(0x1111),
    /**Gets the parameters of a specific TBR template. The normal response to this control point is Get TBR Template Response. For error conditions, the response is Response Code. */
    GET_TBR_TEMPLATE(0x111E),
    /**This is the normal response to Get TBR Template. */
    GET_TBR_TEMPLATE_RESPONSE(0x1122),
    /**Sets the parameters of a specific TBR template. The normal response to this control point is Set TBR Template Response. For error conditions, the response is Response Code. */
    SET_TBR_TEMPLATE(0x112D),
    /**This is the normal response to Set TBR Template. */
    SET_TBR_TEMPLATE_RESPONSE(0x1144),
    /**Sets a bolus with the specified parameters. The normal response to this control point is Set Bolus Response. For error conditions, the response is Response Code. */
    SET_BOLUS(0x114B),
    /**This is the normal response to Set Bolus. */
    SET_BOLUS_RESPONSE(0x1177),
    /**Cancels a bolus with the specified Bolus ID. The normal response to this control point is Cancel Bolus Response. For error conditions, the response is Response Code. */
    CANCEL_BOLUS(0x1178),
    /**This is the normal response to Cancel Bolus. */
    CANCEL_BOLUS_RESPONSE(0x1187),
    /**Gets the currently available bolus types. The normal response to this control point is Get Available Boluses Response. For error conditions, the response is Response Code. */
    GET_AVAILABLE_BOLUSES(0x1188),
    /**This is the normal response to Get Available Boluses. */
    GET_AVAILABLE_BOLUSES_RESPONSE(0x11B4),
    /**Gets the parameters of a specific bolus template. The normal response to this control point is Get Bolus Template Response. For error conditions, the response is Response Code. */
    GET_BOLUS_TEMPLATE(0x11BB),
    /**This is the normal response to Get Bolus Template. */
    GET_BOLUS_TEMPLATE_RESPONSE(0x11D2),
    /**Sets the parameters of a specific bolus template. The normal response to this control point is Set Bolus Template Response. For error conditions, the response is Response Code. */
    SET_BOLUS_TEMPLATE(0x11DD),
    /**This is the normal response to Set Bolus Template. */
    SET_BOLUS_TEMPLATE_RESPONSE(0x11E1),
    /**Gets the status and details of all the supported template types (i.e., if they are configured and / or configurable, the max number of supported time blocks and the template number range). The normal response to this control point is Get Template Status and Details Response. For error conditions, the response is Response Code. */
    GET_TEMPLATE_STATUS_AND_DETAILS(0x11EE),
    /**This is the normal response to Get Template Status and Details. */
    GET_TEMPLATE_STATUS_AND_DETAILS_RESPONSE(0x1212),
    /**Resets the status of one or many templates by marking them as Not Configured. The normal response to this control point is Reset Template Status Response. For error conditions, the response is Response Code. */
    RESET_TEMPLATE_STATUS(0x121D),
    /**This is the normal response to Reset Template Status. */
    RESET_TEMPLATE_STATUS_RESPONSE(0x1221),
    /**Activates profile templates. The normal response to this control point is Activate Profile Templates Response. For error conditions, the response is Response Code. */
    ACTIVATE_PROFILE_TEMPLATES(0x122E),
    /**This is the normal response to Activate Profile Templates. */
    ACTIVATE_PROFILE_TEMPLATES_RESPONSE(0x1247),
    /**Gets all the currently activated profile templates (e.g., basal rate profile template). The normal response to this control point is Get Activated Profile Templates Response. For error conditions, the response is Response Code. */
    GET_ACTIVATED_PROFILE_TEMPLATES(0x1248),
    /**This is the normal response to Get Activated Profile Templates. */
    GET_ACTIVATED_PROFILE_TEMPLATES_RESPONSE(0x1274),
    /**Starts the priming of the fluidic path of the Insulin Delivery Device with the provided amount of insulin. The priming is stopped as soon as that amount is delivered. The response to this control point is Response Code. */
    START_PRIMING(0x127B),
    /**Stops the priming of the fluidic path of the Insulin Delivery Device immediately. The response to this control point is Response Code. */
    STOP_PRIMING(0x1284),
    /**Sets the initial fill level of the reservoir after refill or replacement. The response to this control point is Response Code. */
    SET_INITIAL_RESERVOIR_FILL_LEVEL(0x128B),
    /**Resets the counter Reservoir Insulin Operation Time (retrieved by procedure Get Counter of IDD Status Reader CP with Counter Type Reservoir Insulin Operation Time). The response to this control point is Response Code. */
    RESET_RESERVOIR_INSULIN_OPERATION_TIME(0x12B7),
    /**Reads a specific ISF Profile Template. The response to this control point is Response Code. */
    READ_ISF_PROFILE_TEMPLATE(0x12B8),
    /**This response is used to report one or more time blocks of an ISF Profile Template. */
    READ_ISF_PROFILE_TEMPLATE_RESPONSE(0x12D1),
    /**Writes a specific ISF Profile Template. The normal response to this control point is Write ISF Profile Template Response. For error conditions, the response is Response Code. */
    WRITE_ISF_PROFILE_TEMPLATE(0x12DE),
    /**This is the normal response to Write ISF Profile Template. */
    WRITE_ISF_PROFILE_TEMPLATE_RESPONSE(0x12E2),
    /**Reads a specific I:CHO Ratio Profile Template. The response to this control point is Response Code. */
    READ_I2CHO_RATIO_PROFILE_TEMPLATE(0x12ED),
    /**This response is used to report one or more time blocks of an I:CHO Ratio Profile Template. */
    READ_I2CHO_RATIO_PROFILE_TEMPLATE_RESPONSE(0x1414),
    /**Writes a specific I:CHO Ratio Profile Template. The normal response to this control point is Write I2CHO Ratio Profile Template Response. For error conditions, the response is Response Code. */
    WRITE_I2CHO_RATIO_PROFILE_TEMPLATE(0x141B),
    /**This is the normal response to Write I2CHO Ratio Profile Template. */
    WRITE_I2CHO_RATIO_PROFILE_TEMPLATE_RESPONSE(0x1427),
    /**Reads a specific Target Glucose Range Profile Template. The response to this control point is Response Code. */
    READ_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE(0x1428),
    /**This response is used to report one or more time blocks of a Target Glucose Range Profile Template. */
    READ_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_RESPONSE(0x1441),
    /**Writes a specific Target Glucose Range Profile Template. The normal response to this control point is Write Target Glucose Range Profile Template Response. For error conditions, the response is Response Code. */
    WRITE_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE(0x144E),
    /**This is the normal response to Write Target Glucose Range Profile Template. */
    WRITE_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_RESPONSE(0x1472),
    /**Gets the maximum bolus amount that can be delivered in a single bolus. */
    GET_MAX_BOLUS_AMOUNT(0x147D),
    /**This is the normal response to Get Max Bolus Amount. */
    GET_MAX_BOLUS_AMOUNT_RESPONSE(0x1482),
    /**Sets the maximum bolus amount that can be delivered in a single bolus */
    SET_MAX_BOLUS_AMOUNT(0x148D);
}

enum class ResponseCode(override val key: Int): EnumerationValue {

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
    PROCEDURE_NOT_APPLICABLE(0x74),
    /**Normal response if a transaction consisting of several procedures was not completed by the Server because the parameters provided by the Client to perform this transaction has been invalid or inconsistent.*/
    PLAUSIBILITY_CHECK_FAILED(0x75),
    /**Normal response if the maximum number of boluses of a specific type is reached when executing the Set Bolus procedure. The maximum available number of boluses depends on the implementation of Insulin Delivery Device and which boluses are already running. */
    MAXIMUM_BOLUS_NUMBER_REACHED(0x76);
}
