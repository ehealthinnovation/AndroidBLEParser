package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration
import org.ehealthinnovation.android.bluetooth.idd.*


/**
 * Use this class to parse the response to [GetCounter] command.
 */
class GetCounterResponseParser {

    /**
     * If opcode field in the [DataReader] is [StatusReaderControlOpcode.GET_TOTAL_DAILY_INSULIN_STATUS_RESPONSE],
     * call this method to parse the serialized data into a [CounterResponse] structure
     */
    internal fun parseResponse(dataReader: DataReader): CounterResponse {
        val counterType = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), CounterType::class.java, CounterType.RESERVED_FOR_FUTURE_USE)
        val selection = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), CounterValueSelection::class.java, CounterValueSelection.RESERVED_FOR_FUTURE_USE)
        val value = dataReader.getNextInt(IntFormat.FORMAT_SINT32)

        return CounterResponse(counterType, selection, value)

    }


}