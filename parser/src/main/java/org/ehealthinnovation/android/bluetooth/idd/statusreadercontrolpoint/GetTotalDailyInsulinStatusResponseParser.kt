package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.StatusReaderControlOpcode
import org.ehealthinnovation.android.bluetooth.idd.TotalDailyInsulinStatusResponse
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat

/**
 * Use this class to parse the response to [GetTotalDailyInsulinStatus] command.
 */
class GetTotalDailyInsulinStatusResponseParser {

    /**
     * Pass in [DataReader] which hold the expected response in binary form, and the opcode field is
     * [StatusReaderControlOpcode.GET_TOTAL_DAILY_INSULIN_STATUS_RESPONSE]
     */
    internal fun parseResponse(dataReader: DataReader): TotalDailyInsulinStatusResponse {
        val bolus = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val basal = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val bolusAndBasal = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)

        return TotalDailyInsulinStatusResponse(bolus, basal, bolusAndBasal)
    }
}
