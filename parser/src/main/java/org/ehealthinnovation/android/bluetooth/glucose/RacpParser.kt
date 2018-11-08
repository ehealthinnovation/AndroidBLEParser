package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.*

class RacpParser : CharacteristicParser<RacpResponse> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parse(packet: CharacteristicPacket): RacpResponse {
        val data = packet.readData()
        val opcodeRawValue = data.getNextInt(IntFormat.FORMAT_UINT8)
        val opcode = readEnumeration(opcodeRawValue, Opcode::class.java, Opcode.RESERVED_FOR_FUTURE_USE)
        var output: RacpResponse

        when (opcode) {
            Opcode.NUMBER_OF_STORED_RECORDS_RESPONSE -> output = readGetRecordNumberResponse(data)
            else -> throw IllegalArgumentException("Opcode $opcode not recognized")
        }

        return output
    }

    internal fun readGetRecordNumberResponse(dataReader: DataReader): RacpGetRecordNumberResponse {
        val operationRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val operation = readEnumeration(operationRawValue, Operator::class.java, Operator.RESERVED_FOR_FUTURE_USE)
        val numberOfRecords = dataReader.getNextInt(IntFormat.FORMAT_UINT16)

        if (recordNumberResponseValid(operation, numberOfRecords)) {
            return RacpGetRecordNumberResponse(numberOfRecords)
        } else {
            throw IllegalArgumentException("Response fields not valid")
        }
    }

    internal fun recordNumberResponseValid(operation: Operator, numberOfRecords: Int): Boolean =
            (operation == Operator.NULL && ((numberOfRecords >= IntFormat.FORMAT_UINT16.minValue) && (numberOfRecords <= IntFormat.FORMAT_UINT16.maxValue)))

}