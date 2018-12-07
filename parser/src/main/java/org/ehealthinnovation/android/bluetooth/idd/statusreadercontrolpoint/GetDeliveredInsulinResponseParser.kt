package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.StatusReaderControlOpcode
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat

/**
 * This parser provides methods to deserialize the response of [GetDeliveredInsulin] command into a
 * [DeliveredInsulinResponse] object
 */
class GetDeliveredInsulinResponseParser {

    /**
     * Call this method with [DataReader] holding the response part of a [DeliveredInsulinResponse].
     * A dispatcher can call this method when it determine the opcode is [StatusReaderControlOpcode.GET_DELIVERED_INSULIN_RESPONSE]
     */
    internal fun parseResponse(dataReader: DataReader): DeliveredInsulinResponse {
        val bolus = dataReader.getNextFloat(FloatFormat.FORMAT_FLOAT)
        val basal = dataReader.getNextFloat(FloatFormat.FORMAT_FLOAT)
        return DeliveredInsulinResponse(bolus, basal)
    }


}

/**
 * Data structure to hold a response from [GetDeliveredInsulin] command.
 * @property bolus expresses the total amount of insulin delivered through bolus injection since the internal counter is reset. Unit in IU
 * @property basal expresses the total amount of insulin delivered through basal since the internal counter is reset. Unit in IU
 */
data class DeliveredInsulinResponse(
        val bolus: Float,
        val basal: Float
)