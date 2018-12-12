package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.DeliveredInsulinResponse
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

