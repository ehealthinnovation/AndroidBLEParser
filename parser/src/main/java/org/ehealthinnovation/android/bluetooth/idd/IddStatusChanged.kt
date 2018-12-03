package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import java.util.*


/**
 * The characteristic indicates the Insulin Delivery Device (IDD) status changed.
 * @property status A group of flags that indicate any changed.
 */
data class IddStatusChanged(
        val status: EnumSet<Status>
)

enum class Status(override val bitOffset: Int) : FlagValue {
    /**If this bit is set, the therapy control state of the Insulin Delivery Device changed. */
    THERAPY_CONTROL_STATE_CHANGED(0),

    /**If this bit is set, the operational state of the Insulin Delivery Device changed. */
    OPERATIONAL_STATE_CHANGED(1),

    /**If this bit is set, the status of the insulin reservoir changed (caused by a reservoir change or the delivery of insulin). */
    RESERVOIR_STATUS_CHANGED(2),

    /**If this bit is set, a new annunciation was created by the Server application. */
    ANNUNCIATION_STATUS_CHANGED(3),

    /**If this bit is set, the total daily insulin amount changed due to a bolus or basal delivery. The bit shall be set at the end of an effective delivery. */
    TOTAL_DAILY_INSULIN_STATUS_CHANGED(4),

    /**If this bit is set, the current basal rate changed due to a new basal rate value (e.g., caused by a changed basal rate profile, reaching of a time block with another basal rate value or by a TBR). */
    ACTIVE_BASAL_RATE_STATUS_CHANGED(5),

    /**If this bit is set, a new bolus was initiated or the status of current Active Bolus changed. */
    ACTIVE_BOLUS_STATUS_CHANGED(6),

    /**If this bit is set, a new event has been recorded in the history. */
    HISTORY_EVENT_RECORDED(7);
}