package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.*
import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

class GetActiveBasalRateDeliveryResponseParser {

    /**
     * Use this parser for the response to [GetActiveBasalRateDelivery] command.
     * Create a instance of this class and call [readActiveBasalRateDeliveryResponse] with the buffer
     * if data returned from Status Reader Control Point starts with [StatusReaderControlOpcode.GET_ACTIVE_BASAL_RATE_DELIVERY_RESPONSE]
     */
    internal fun readActiveBasalRateDeliveryResponse(dataReader: DataReader): ActiveBasalRateDeliveryResponse {
        val flags = readFlags(dataReader)
        val activeBasalRateProfileTemplateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val activeBasalRateCurrentConfigValue = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)

        val tbrConfig = if (flags.contains(Flag.TBR_PRESENT)) readTbrConfig(dataReader) else null
        val tbrTemplateNumber = if (flags.contains(Flag.TBR_TEMPLATE_NUMBER_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT8) else null
        val basalDeliveryContext = if (flags.contains(Flag.BASAL_DELIVERY_CONTEXT_PRESENT)) readBasalDeliveryContext(dataReader) else null

        return ActiveBasalRateDeliveryResponse(
                activeBasalRateProfileTemplateNumber,
                activeBasalRateCurrentConfigValue,
                tbrConfig,
                tbrTemplateNumber,
                basalDeliveryContext
        )
    }

    internal fun readFlags(dataReader: DataReader): EnumSet<Flag> =
            parseFlags(dataReader.getNextInt(IntFormat.FORMAT_UINT8), Flag::class.java)

    internal fun readBasalDeliveryContext(dataReader: DataReader): BasalDeliveryContext =
            readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                    BasalDeliveryContext::class.java,
                    BasalDeliveryContext.RESERVED_FOR_FUTURE_USE)

    internal fun readTbrConfig(dataReader: DataReader): TbrConfig {
        val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val value = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val durationProgrammed = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val durationRemaining = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        return TbrConfig(type, value, durationProgrammed, durationRemaining)
    }


    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, there is an active TBR. In this case, the fields TBR Type, TBR Adjustment Value, TBR Duration Programmed, and TBR Duration Remaining are present. */
        TBR_PRESENT(0),
        /**If this bit and the TBR Present bit are set, the TBR Template Number field is present. */
        TBR_TEMPLATE_NUMBER_PRESENT(1),
        /**If this bit is set, the Basal Delivery Context field is present. */
        BASAL_DELIVERY_CONTEXT_PRESENT(2);
    }


}