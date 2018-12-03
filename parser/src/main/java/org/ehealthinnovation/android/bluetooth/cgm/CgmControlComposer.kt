package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Composer for the control commands to the CGM Specific Command Control Point
 *
 * Call [canCompose] on your [CamControlCommand] to make sure it is supported. If it does,
 * [canCompose] returns true.
 *
 * Then call [compose] with the [CamControlCommand] and the target data buffer [DataWriter] to
 * write the serialized request into the target buffer.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_specific_ops_control_point.xml
 *
 * @throws Exception if write operation is unsuccessful
 */
class CgmControlComposer : CharacteristicComposer<CgmControlCommand> {

    private val operandComposer: CgmControlOperandComposer = CgmControlOperandComposer()

    override fun canCompose(request: CgmControlCommand): Boolean {
        return when (request) {
            is StartSession,
            is StopSession,
            is SetCommunicationInterval,
            is GetCommunicationInterval,
            is SetGlucoseCalibrationValue,
            is GetGlucoseCalibrationValue,
            is SetGlucoseAlertLevel,
            is GetGlucoseAlertLevel,
            is ResetDeviceSpecificAlert -> true
            else -> false
        }
    }

    override fun compose(request: CgmControlCommand, dataWriter: DataWriter) {
        when (request) {
            is StartSession -> composeSimpleCommand(Opcode.START_THE_SESSION, dataWriter)
            is StopSession -> composeSimpleCommand(Opcode.STOP_THE_SESSION, dataWriter)
            is SetCommunicationInterval -> composeSetCommunicationInterval(request.operand, dataWriter)
            is GetCommunicationInterval -> composeSimpleCommand(Opcode.GET_CGM_COMMUNICATION_INTERVAL, dataWriter)
            is SetGlucoseCalibrationValue -> composeSetCalibration(request.operand, dataWriter)
            is GetGlucoseCalibrationValue -> composeGetCalibration(request.operand, dataWriter)
            is SetGlucoseAlertLevel -> composeSetGlucoseAlertLevel(request, dataWriter)
            is GetGlucoseAlertLevel -> composeGetGlucoseAlertLevel(request, dataWriter)
            is ResetDeviceSpecificAlert -> composeSimpleCommand(Opcode.RESET_DEVICE_SPECIFIC_ALERT, dataWriter)
            else -> throw IllegalArgumentException("operation not recognized")
        }
    }

    /**
     * Use this compose function when the control command has only an opcode as argument.
     */
    internal fun composeSimpleCommand(opcode: Opcode, dataWriter: DataWriter) {
        dataWriter.putInt(opcode.key, IntFormat.FORMAT_UINT8)
    }

    internal fun composeSetCommunicationInterval(operand: CommunicationInterval, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.SET_CGM_COMMUNICATION_INTERVAL.key, IntFormat.FORMAT_UINT8)
        operandComposer.composeCommunicationInterval(operand, dataWriter)
    }

    internal fun composeSetCalibration(operand: CalibrationRecord, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.SET_GLUCOSE_CALIBRATION_VALUE.key, IntFormat.FORMAT_UINT8)
        operandComposer.composeCalibrationRecord(operand, dataWriter)
    }

    internal fun composeGetCalibration(operand: CalibrationRecordRequest, dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.GET_GLUCOSE_CALIBRATION_VALUE.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.recordNumber, IntFormat.FORMAT_UINT16)
    }

    internal fun composeSetGlucoseAlertLevel(request: SetGlucoseAlertLevel, dataWriter: DataWriter) {
        val opcode: Opcode = request.operand.alertType.setCommandOpcode
        val concentration: Float = request.operand.concentration
        dataWriter.putInt(opcode.key, IntFormat.FORMAT_UINT8)
        dataWriter.putFloat(concentration, 0, FloatFormat.FORMAT_SFLOAT)
    }

    internal fun composeGetGlucoseAlertLevel(request: GetGlucoseAlertLevel, dataWriter: DataWriter) {
        val opcode = request.alertType.getCommandOpcode
        dataWriter.putInt(opcode.key, IntFormat.FORMAT_UINT8)
    }
}