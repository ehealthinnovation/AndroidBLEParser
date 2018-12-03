package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser for Cgm Specific Control Point response.
 * @see  https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_specific_ops_control_point.xml
 */
class CgmControlParser : CharacteristicParser<CgmControlResponse> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.SPECIFIC_CONTROL_POINT.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmControlResponse {
        val data = packet.readData()
        val opcode = readResponseOpcode(data) // this opcode is not used, just to consume the byte in the buffer
        val response = when (opcode) {
            Opcode.RESPONSE_CODE -> readGenericResponse(data)
            Opcode.GLUCOSE_CALIBRATION_VALUE_RESPONSE -> readCalibrationRecordResponse(data)
            Opcode.PATIENT_HIGH_ALERT_LEVEL_RESPONSE,
            Opcode.PATIENT_LOW_ALERT_LEVEL_RESPONSE,
            Opcode.HYPO_ALERT_LEVEL_RESPONSE,
            Opcode.HYPER_ALERT_LEVEL_RESPONSE -> readGetGlucoseAlertLevelResponse(opcode, data)
            Opcode.RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE,
            Opcode.RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE -> readRateOfChangeAlertLevelResponse(opcode, data)
            Opcode.CGM_COMMUNICATION_INTERVAL_RESPONSE -> readCommunicationIntervalResponse(data)
            else -> {
                throw IllegalArgumentException("Unsupported response opcode")
            }
        }
        return response
    }

    internal fun readCalibrationRecordResponse(data: DataReader): CalibrationRecordResponse {
        val glucoseConcentration = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val calibrationTime = data.getNextInt(IntFormat.FORMAT_UINT16)
        val sampleLocationRawValue = data.getNextInt(IntFormat.FORMAT_UINT8)
        val sampleType = readEnumeration(readLeastSignificantNibble(sampleLocationRawValue), SampleType::class.java, SampleType.RESERVED_FOR_FUTURE)
        val sampleLocation = readEnumeration(readMostSignificantNibble(sampleLocationRawValue), SampleLocation::class.java, SampleLocation.RESERVED_FOR_FUTURE)
        val nextCalibrationTime = data.getNextInt(IntFormat.FORMAT_UINT16)
        val recordNumebr = data.getNextInt(IntFormat.FORMAT_UINT16)
        val calibrationStatus = parseFlags(data.getNextInt(IntFormat.FORMAT_UINT8), CalibrationStatus::class.java)

        return CalibrationRecordResponse(
                glucoseConcentration,
                calibrationTime,
                sampleType,
                sampleLocation,
                nextCalibrationTime,
                recordNumebr,
                calibrationStatus)
    }

    internal fun readGenericResponse(data: DataReader): CgmControlGenericResponse {
        val requestOpcode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                Opcode::class.java,
                Opcode.RESERVED_FOR_FUTURE_USE)

        val responseCode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                ResponseCode::class.java,
                ResponseCode.RESERVED_FOR_FUTURE_USE)

        return CgmControlGenericResponse(
                requestOpcode,
                responseCode)
    }

    internal fun readGetGlucoseAlertLevelResponse(opcode: Opcode, data: DataReader): CgmControlResponse {
        val glucoseAlertLevel = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        return when (opcode) {
            Opcode.PATIENT_HIGH_ALERT_LEVEL_RESPONSE -> PatientHighAlertResponse(glucoseAlertLevel)
            Opcode.PATIENT_LOW_ALERT_LEVEL_RESPONSE -> PatientLowAlertResponse(glucoseAlertLevel)
            Opcode.HYPO_ALERT_LEVEL_RESPONSE -> HypoAlertResponse(glucoseAlertLevel)
            Opcode.HYPER_ALERT_LEVEL_RESPONSE -> HyperAlertResponse(glucoseAlertLevel)
            else -> throw IllegalArgumentException("opcode $opcode not support the function")
        }
    }

    internal fun readRateOfChangeAlertLevelResponse(opcode: Opcode, data: DataReader): RateOfChangeAlertResponse {
        val rateOfChange = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        return when (opcode) {
            Opcode.RATE_OF_DECREASE_ALERT_LEVEL_RESPONSE -> RateOfDecreaseAlertResponse(rateOfChange)
            Opcode.RATE_OF_INCREASE_ALERT_LEVEL_RESPONSE -> RateOfIncreaseAlertResponse(rateOfChange)
            else -> throw IllegalArgumentException("opcode $opcode not support the function")
        }
    }

    internal fun readCommunicationIntervalResponse(data: DataReader): CommunicationIntervalResponse {
        return CommunicationIntervalResponse(data.getNextInt(IntFormat.FORMAT_UINT16))
    }

    /**
     * Read in the next byte and parse it into one of the [Opcode]
     *
     */
    internal fun readResponseOpcode(dataReader: DataReader): Opcode {
        return readEnumeration(
                dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                Opcode::class.java)
    }
}