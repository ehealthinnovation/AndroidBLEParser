package org.ehealthinnovation.android.bluetooth.cgm

import java.util.*


abstract class CgmControlResponse

/**
 * The data structure for a control response
 *
 * @property requestOperation the request code a collector sent to the CGM
 *
 * @property response the response from the operation
 *
 */
data class CgmControlGenericResponse(
        val requestOperation: Opcode,
        val response: ResponseCode
) : CgmControlResponse()

/**
 * Data structure to hold the response from [GetGlucoseCalibrationValue]
 *
 * @property concentration the glucose concentration value used to set the calibration, in mg/dL
 *
 * @property timeOffsetMinutes the time offset relative to the session start time
 *
 * @property sampleType the type of the fluid sample used for calibration
 *
 * @property sampleLocation the location of the fluid sample used for calibration
 *
 * @property nextCalibrationTime the next time when a calibration is needed
 *
 * @property recordNumber a unique number to identify a calibration record. This number should be assigned
 * by the CGM device.
 *
 * @property status a flag field indicating the calibration status.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_specific_ops_control_point.xml
 */
data class CalibrationRecordResponse(
        val concentration: Float,
        val timeOffsetMinutes: Int,
        val sampleType: SampleType,
        val sampleLocation: SampleLocation,
        val nextCalibrationTime: Int,
        val recordNumber: Int,
        val status: EnumSet<CalibrationStatus>
) : CgmControlResponse()

/**
 * Super class to store glucose alert response levels
 *
 * @property concentration the alert glucose level in mg/dL
 */
abstract class AlertGlucoseLevelResponse(
        open val concentration: Float
) : CgmControlResponse()

/**
 * The responses to [GetGlucoseAlertLevel] for corresponding [GlucoseAlertType]
 */
data class PatientHighAlertResponse(override val concentration: Float) : AlertGlucoseLevelResponse(concentration)
data class PatientLowAlertResponse(override val concentration: Float) : AlertGlucoseLevelResponse(concentration)
data class HypoAlertResponse(override val concentration: Float) : AlertGlucoseLevelResponse(concentration)
data class HyperAlertResponse(override val concentration: Float) : AlertGlucoseLevelResponse(concentration)

/**
 * Super class to store get rate of change alert level response
 *
 * @property rateOfChange the alert glucose level in mg/dL/min
 */
abstract class RateOfChangeAlertResponse(
        open val rateOfChange: Float
) : CgmControlResponse()

/**
 * The responses to [GetGlucoseAlertLevel] for corresponding [GlucoseAlertType]
 */
data class RateOfDecreaseAlertResponse(override val rateOfChange: Float) : RateOfChangeAlertResponse(rateOfChange)
data class RateOfIncreaseAlertResponse(override val rateOfChange: Float) : RateOfChangeAlertResponse(rateOfChange)
