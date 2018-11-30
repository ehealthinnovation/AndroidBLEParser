package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import java.util.*

abstract class CgmControlOperand

/**
 * Operand for the [SetCommunicationInterval] command.
 *
 * @property intervalMinute the period at which a CGM device is set to communicate with the
 * collector. It should fall in the range of 0 to 255. 0 has special meaning that
 * communication is paused. 255 means to communicate at highest frequency possible
 */
data class CommunicationInterval(val intervalMinute: Int) : CgmControlOperand() {
    init {
        if (intervalMinute < 0 || intervalMinute > 0x00FF) {
            throw IllegalArgumentException("$intervalMinute should not be negative or larger than 0xff")
        }
    }
}

/**
 * This class is used as operand to fetch a Calibration Record of number [recordNumber]
 * through [GetGlucoseCalibrationValue]
 */
data class CalibrationRecordRequest(
        val recordNumber: Int
) : CgmControlOperand()

/**
 * This class is used as operand to set a Calibration Record through [SetGlucoseCalibrationValue]
 * or reading a calibration record from the device as the response of [GetGlucoseCalibrationValue]
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
data class CalibrationRecord(
        val concentration: Float,
        val timeOffsetMinutes: Int,
        val sampleType: SampleType,
        val sampleLocation: SampleLocation,
        val nextCalibrationTime: Int,
        val recordNumber: Int,
        val status: EnumSet<CalibrationStatus>
) : CgmControlOperand()


enum class CalibrationStatus constructor(override val bitOffset: Int) : FlagValue {
    DATA_REJECTED(0),
    DATA_OUT_OF_RANGE(1),
    PROCESS_PENDING(2);
}

class GlucoseAlertLevel(val alertType: GlucoseAlertType, val concentration: Float) : CgmControlOperand()

enum class GlucoseAlertType(val getCommandOpcode: Opcode, val setCommandOpcode: Opcode){
    PATIENT_LOW_ALERT(Opcode.GET_PATIENT_LOW_ALERT_LEVEL, Opcode.SET_PATIENT_LOW_ALERT_LEVEL),
    PATIENT_HIGH_ALERT(Opcode.GET_PATIENT_HIGH_ALERT_LEVEL, Opcode.SET_PATIENT_HIGH_ALERT_LEVEL),
    HYPO_ALERT(Opcode.GET_HYPO_ALERT_LEVEL, Opcode.SET_HYPO_ALERT_LEVEL),
    HYPER_ALERT(Opcode.GET_HYPER_ALERT_LEVEL, Opcode.SET_HYPER_ALERT_LEVEL),
    RATE_OF_INCREASE(Opcode.GET_RATE_OF_INCREASE_ALERT_LEVEL, Opcode.SET_RATE_OF_INCREASE_ALERT_LEVEL),
    RATE_OF_DECREASE(Opcode.GET_RATE_OF_DECREASE_ALERT_LEVEL, Opcode.SET_RATE_OF_DECREASE_ALERT_LEVEL);
}



