package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.*


/**
 * Parser for parsing the response data from RACP Characteristic in an IDD.
 *
 */
class RacpParser : CharacteristicParser<RacpResponse> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.RECORD_ACCESS_CONTROL_POINT.uuid
    }

    override fun parse(packet: CharacteristicPacket): RacpResponse {
        val data = packet.readData()
        val opcodeRawValue = data.getNextInt(IntFormat.FORMAT_UINT8)
        val opcode = readEnumeration(opcodeRawValue, Opcode::class.java, Opcode.RESERVED_FOR_FUTURE_USE)
        val output: RacpResponse

        output = when (opcode) {
            Opcode.NUMBER_OF_STORED_RECORDS_RESPONSE -> readGetRecordNumberResponse(data)
            Opcode.RESPONSE_CODE -> readGeneralResponse(data)
            else -> throw IllegalArgumentException("Opcode $opcode not recognized")
        }

        return output
    }

    /**
     * Method to parse the data in [DataReader] when the opcode is [Opcode.RESPONSE_CODE]
     */
    internal fun readGeneralResponse(dataReader: DataReader): RacpGeneralResponse {
        val operationRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val operation = readEnumeration(operationRawValue, Operator::class.java, Operator.RESERVED_FOR_FUTURE_USE)
        val requestOpcodeRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val requestOpcode = readEnumeration(requestOpcodeRawValue, Opcode::class.java, Opcode.RESERVED_FOR_FUTURE_USE)
        val responseCodeRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val response = readEnumeration(responseCodeRawValue, RacpResponseCode::class.java, RacpResponseCode.RESERVED_FOR_FUTURE_USE)

        if (genericResponseValid(operation, requestOpcode, response)) {
            return RacpGeneralResponse(requestOpcode, response)
        } else {
            throw IllegalArgumentException("Response fields not valid")
        }
    }

    /**
     * Parses the data in [DataReader] when the opcode is [Opcode.NUMBER_OF_STORED_RECORDS_RESPONSE].
     * Outputs the results in [RacpGetRecordNumberResponse], which contains the number of record found
     * to meet the request criteria.
     */
    internal fun readGetRecordNumberResponse(dataReader: DataReader): RacpGetRecordNumberResponse {
        val operationRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val operation = readEnumeration(operationRawValue, Operator::class.java, Operator.RESERVED_FOR_FUTURE_USE)
        val numberOfRecords = dataReader.getNextInt(IntFormat.FORMAT_UINT32)

        if (recordNumberResponseValid(operation, numberOfRecords)) {
            return RacpGetRecordNumberResponse(numberOfRecords)
        } else {
            throw IllegalArgumentException("Response fields not valid")
        }
    }

    /**
     * Return true if given response parameters are valid (i.e. not having value RESERVED_FOR_FUTURE_USE)
     */
    internal fun genericResponseValid(operation: Operator, requestOpcode: Opcode, responseCode: RacpResponseCode): Boolean = (operation == Operator.NULL &&
            requestOpcode != Opcode.RESERVED_FOR_FUTURE_USE &&
            responseCode != RacpResponseCode.RESERVED_FOR_FUTURE_USE)

    /**
     * Return true if given response parameters are valid (i.e. the operator is NULL, and number does not exceed the integer range)
     */
    internal fun recordNumberResponseValid(operation: Operator, numberOfRecords: Int): Boolean =
            (operation == Operator.NULL && ((numberOfRecords >= IntFormat.FORMAT_UINT32.minValue) && (numberOfRecords <= IntFormat.FORMAT_UINT32.maxValue)))

}