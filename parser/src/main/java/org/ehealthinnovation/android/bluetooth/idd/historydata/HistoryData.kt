package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue

/**
 * A history event recording a reference time
 * @property eventInfo the info header of the event
 * @property eventData the data that reflect the time event
 */
data class HistoryEvent<T>(
        val eventInfo: EventInfo,
        val eventData: T
)

/**
 * Contains basic information contained in every history event instance.
 * @property EventType the type of a history event
 * @property sequenceNumber the unique sequence number of the history event
 * @property relativeOffset the time offset from the previous [ReferenceTimeEvent] in seconds.
 */
data class EventInfo(val type: EventType, val sequenceNumber: Int, val relativeOffset: Int)

enum class EventType(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    REFERENCE_TIME(0x000F),
    REFERENCE_TIME_BASE_OFFSET(0x0033),
    BOLUS_CALCULATED_PART_1_OF_2(0x003C),
    BOLUS_CALCULATED_PART_2_OF_2(0x0055),
    BOLUS_PROGRAMMED_PART_1_OF_2(0x005A),
    BOLUS_PROGRAMMED_PART_2_OF_2(0x0066),
    BOLUS_DELIVERED_PART_1_OF_2(0x0069),
    BOLUS_DELIVERED_PART_2_OF_2(0x0096),
    DELIVERED_BASAL_RATE_CHANGED(0x0099),
    TBR_ADJUSTMENT_STARTED(0x00A5),
    TBR_ADJUSTMENT_ENDED(0x00AA),
    TBR_ADJUSTMENT_CHANGED(0x00C3),
    PROFILE_TEMPLATE_ACTIVATED(0x00CC),
    BASAL_RATE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED(0x00F0),
    TOTAL_DAILY_INSULIN_DELIVERY(0x00FF),
    THERAPY_CONTROL_STATE_CHANGED(0x0303),
    OPERATIONAL_STATE_CHANGED(0x030C),
    RESERVOIR_REMAINING_AMOUNT_CHANGED(0x0330),
    ANNUNCIATION_STATUS_CHANGED_PART_1_OF_2(0x033F),
    ANNUNCIATION_STATUS_CHANGED_PART_2_OF_2(0x0356),
    ISF_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED(0x0359),
    I2CHO_RATIO_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED(0x0365),
    TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED(0x036A),
    PRIMING_STARTED(0x0395),
    PRIMING_DONE(0x039A),
    DATA_CORRUPTION(0x03A6),
    POINTER_EVENT(0x03A9),
    BOLUS_TEMPLATE_CHANGED_PART_1_OF_2(0x03C0),
    BOLUS_TEMPLATE_CHANGED_PART_2_OF_2(0x03CF),
    TBR_TEMPLATE_CHANGED(0x03F3),
    MAX_BOLUS_AMOUNT_CHANGED(0x03FC);
}