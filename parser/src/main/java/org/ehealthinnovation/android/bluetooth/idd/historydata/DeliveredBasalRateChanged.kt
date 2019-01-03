package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.BasalDeliveryContext
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Event data for [DeliveredBasalRateChangedEvent]
 * This event should be recorded in every case that affects the delivery of the basal rate (e.g.,
 * a new time block in the basal rate profile is entered; the basal rate profile is reconfigured;
 * a TBR starts, changes, or stops; the Therapy Control State of the device changes from Run to
 * another state or vice versa).
 *
 * @property oldValue the old basal rate in IU/h
 * @property newValue the new basal rate in IU/h
 * @property context the basal delivery context if not null
 */
data class BasalRateChanged(
        val oldValue: Float,
        val newValue: Float,
        val context: BasalDeliveryContext?
)

internal class DeliveredBasalRateChangedEventParser: HistoryEventDataParser<BasalRateChanged>() {
    override fun readData(dataReader: DataReader): BasalRateChanged {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val oldBasalRate = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val newBasalRate = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val context =
                if (flags.contains(Flag.BASAL_DELIVERY_CONTEXT_PRESENT)) {
                    readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), BasalDeliveryContext::class.java, BasalDeliveryContext.RESERVED_FOR_FUTURE_USE)
                } else null
        return BasalRateChanged(oldBasalRate, newBasalRate, context)
    }

    enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Basal Delivery Context field is present. */
        BASAL_DELIVERY_CONTEXT_PRESENT(0);
    }
}