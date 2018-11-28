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
): CgmControlResponse()

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

