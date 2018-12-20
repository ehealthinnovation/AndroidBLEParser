package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.parseFlags

class WriteBasalRateProfileTemplateResponseParser {

    internal fun parseWriteBasalRateProfileTemplateResponse(data: DataReader): WriteBasalRateProfileTemplateResponse {

        val isTransactionCompleted = readTransactionCompleted(data)
        val templateNumber = data.getNextInt(IntFormat.FORMAT_UINT8)
        val firstTimeBlockNumber = data.getNextInt(IntFormat.FORMAT_UINT8)

        return WriteBasalRateProfileTemplateResponse(isTransactionCompleted, templateNumber, firstTimeBlockNumber)
    }

    internal fun readTransactionCompleted(data: DataReader): Boolean {
        val flags = parseFlags(data.getNextInt(IntFormat.FORMAT_UINT8), Flag::class.java)
        var isTransactionCompleted = false
        if (flags.contains(Flag.TRANSACTION_COMPLETED)) isTransactionCompleted = true
        return isTransactionCompleted
    }

    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the basal rate profile passed the plausibility checks of the Server and was written successfully (i.e., the write transaction of all time blocks of the basal rate profile completed).*/
        TRANSACTION_COMPLETED(0);

    }
}