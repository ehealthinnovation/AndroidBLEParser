package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

enum class Opcode(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SET_CGM_COMMUNICATION_INTERVAL(1),
    GET_CGM_COMMUNICATION_INTERVAL(2),
    CGM_COMMUNICATION_INTERVAL_RESPONSE(3),
    SET_GLUCOSE_CALIBRATION_VALUE(4),
    GET_GLUCOSE_CALIBRATION_VALUE(5),
    GLUCOSE_CALIBRATION_VALUE_RESPONSE(6),
    SET_PATIENT_HIGH_ALERT_LEVEL(7),
    GET_PATIENT_HIGH_ALERT_LEVEL(8),
    PATIENT_HIGH_ALERT_LEVEL_RESPONSE(9),
    SET_PATIENT_LOW_ALERT_LEVEL(10),
    GET_PATIENT_LOW_ALERT_LEVEL(11),
    PATIENT_LOW_ALERT_LEVEL_RESPONSE(12),
    SET_HYPO_ALERT_LEVEL(13),
    GET_HYPO_ALERT_LEVEL(14),
    HYPO_ALERT_LEVEL_RESPONSE(15),
    SET_HYPER_ALERT_LEVEL(16),
    GET_HYPER_ALERT_LEVEL(17),
    HYPER_ALERT_LEVEL_RESPONSE(18),
    SET_RATE_OF_DECREASE_ALERT_LEVEL(19),
    GET_RATE_OF_DECREASE_ALERT_LEVEL(20),
    RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE(21),
    SET_RATE_OF_INCREASE_ALERT_LEVEL(22),
    GET_RATE_OF_INCREASE_ALERT_LEVEL(23),
    RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE(24),
    RESET_DEVICE_SPECIFIC_ALERT(25),
    START_THE_SESSION(26),
    STOP_THE_SESSION(27),
    RESPONSE_CODE(28);
}

enum class ResponseCode constructor(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SUCCESS(1),
    OP_CODE_NOT_SUPPORTED(2),
    INVALID_OPERAND(3),
    PROCEDURE_NOT_COMPLETED(4),
    PARAMETER_OUT_OF_RANGE(5);
}


