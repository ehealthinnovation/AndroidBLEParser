package org.ehealthinnovation.android.bluetooth.idd


/**
 * Response to [GetActiveBolusIds]
 * @property ids A list of all currently active bolus ids.
 */
data class ActiveBolusIds(
        val ids: List<Int>
): StatusReaderControlResponse()