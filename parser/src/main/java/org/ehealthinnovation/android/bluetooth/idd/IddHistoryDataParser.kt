package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.historydata.*
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser to parse the response from a Idd Command Control Point characteristic. To use this parser,
 * call [canParse] first to make sure the parser can handle the input packet. Call [parse] afterwards
 * to get a instance of [HistoryEvent].
 *
 * The data type of [HistoryEvent.eventData] is determined by [HistoryEvent.eventInfo], specifically
 * the [EventInfo.type] field. User can cast the [HistoryEvent.eventData] to a data type according to
 * the following mapping.
 *
 * | Event Type                                     | Data Type           |
 * |:------------------------------------:          |:-------------------:|
 * |[EventType.REFERENCE_TIME]                      |[ReferenceTime]      |
 * |[EventType.REFERENCE_TIME_BASE_OFFSET]          |[ReferenceTimeBaseOffsetData]|
 * |[EventType.BOLUS_CALCULATED_PART_1_OF_2]        |[BolusCalculatedData]|
 * |[EventType.BOLUS_CALCULATED_PART_2_OF_2]        |[BolusCalculatedData]|
 * |[EventType.BOLUS_PROGRAMMED_PART_1_OF_2]        |[Bolus]|
 * |[EventType.BOLUS_PROGRAMMED_PART_2_OF_2]        |[BolusProgrammedConfiguration]|
 * |[EventType.BOLUS_DELIVERED_PART_1_OF_2]         |[Bolus]|
 * |[EventType.BOLUS_DELIVERED_PART_2_OF_2]         |[BolusDeliveredStatus]|
 * |[EventType.DELIVERED_BASAL_RATE_CHANGED]        |[BasalRateChanged]|
 * |[EventType.TBR_ADJUSTMENT_STARTED]              |[TbrAdjustmentStart]|
 * |[EventType.TBR_ADJUSTMENT_ENDED]                |[TbrAdjustmentEnded]|
 * |[EventType.TBR_ADJUSTMENT_CHANGED]              |[TbrAdjustmentChanged]|
 * |[EventType.PROFILE_TEMPLATE_ACTIVATED]          |[ProfileTemplateActivated]|
 * |[EventType.BASAL_RATE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED]| [SingleValueTimeBlockChangeData]|
 * |[EventType.TOTAL_DAILY_INSULIN_DELIVERY]        |[TotalDailyInsulinDelivery]|
 * |[EventType.THERAPY_CONTROL_STATE_CHANGED]       |[TherapyControlStateChangedData]|
 * |[EventType.OPERATIONAL_STATE_CHANGED]           |[OperationalStateChangedData]|
 * |[EventType.RESERVOIR_REMAINING_AMOUNT_CHANGED]  |[ReservoirRemainingAmount]|
 * |[EventType.ANNUNCIATION_STATUS_CHANGED_PART_1_OF_2]|[AnnunciationStatusChanged]|
 * |[EventType.ANNUNCIATION_STATUS_CHANGED_PART_2_OF_2]|[AnnunciationStatusChangeAdditionalInfo]|
 * |[EventType.ISF_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED]|[SingleValueTimeBlockChangeData]|
 * |[EventType.I2CHO_RATIO_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED]|[SingleValueTimeBlockChangeData]|
 * |[EventType.TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED]|[SingleValueTimeBlockChangeData]|
 * |[EventType.PRIMING_STARTED]                     |[PrimingStarted]|
 * |[EventType.PRIMING_DONE]                        |[PrimingDone]|
 * |[EventType.DATA_CORRUPTION]                     |[EmptyEventData]|
 * |[EventType.POINTER_EVENT]                       |[EmptyEventData]|
 * |[EventType.BOLUS_TEMPLATE_CHANGED_PART_1_OF_2]  |[BolusTemplateChangedBolusPart]|
 * |[EventType.BOLUS_TEMPLATE_CHANGED_PART_2_OF_2]  |[BolusTemplateChangedBolusDelay]|
 * |[EventType.TBR_TEMPLATE_CHANGED]                |[TbrTemplateChanged]|
 * |[EventType.MAX_BOLUS_AMOUNT_CHANGED]            |[MaxBolusAmountChanged]|
 *
 *  Example for mapping the history event data to the correct data type,
 *
 *  ` val historyEvent = IddHistoryDataParser().parse(packet)
 *    if( historyEvent.eventInfo.type == EventType.PRIMING_DONE){
 *          someFunctionRequiresPrimingDoneData((historyEvent.eventData as PrimingDone))
 *    }`
 *
 */
class IddHistoryDataParser : CharacteristicParser<HistoryEvent<out Any>> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.HISTORY_DATA.uuid
    }

    override fun parse(packet: CharacteristicPacket): HistoryEvent<out Any> {
        val data = packet.readData()
        val eventInfo = readEventInfo(data)
        val eventDataParser = getEventDataParser(eventInfo.type)
        return eventDataParser.parseEvent(eventInfo, data)
    }

    internal fun getEventDataParser(eventType: EventType): HistoryEventDataParser<out Any> {
        return when (eventType) {
            EventType.REFERENCE_TIME -> ReferenceTimeEventParser()
            EventType.REFERENCE_TIME_BASE_OFFSET -> ReferenceTimeBaseOffsetEventParser()
            EventType.BOLUS_CALCULATED_PART_1_OF_2,
            EventType.BOLUS_CALCULATED_PART_2_OF_2 -> BolusCalculatedEventParser()
            EventType.BOLUS_PROGRAMMED_PART_1_OF_2 -> BolusProgrammedPart1EventParser()
            EventType.BOLUS_PROGRAMMED_PART_2_OF_2 -> BolusProgrammedPart2EventParser()
            EventType.BOLUS_DELIVERED_PART_1_OF_2 -> BolusProgrammedPart1EventParser()
            EventType.BOLUS_DELIVERED_PART_2_OF_2 -> BolusDeliveredPart2EventParser()
            EventType.DELIVERED_BASAL_RATE_CHANGED -> DeliveredBasalRateChangedEventParser()
            EventType.TBR_ADJUSTMENT_STARTED -> TbrAdjustmentStartedParser()
            EventType.TBR_ADJUSTMENT_ENDED -> TbrAdjustmentEndedEventParser()
            EventType.TBR_ADJUSTMENT_CHANGED -> TbrAdjustmentChangedEventParser()
            EventType.PROFILE_TEMPLATE_ACTIVATED -> ProfileTemplateActivatedEventParser()
            EventType.ISF_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED,
            EventType.I2CHO_RATIO_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED,
            EventType.BASAL_RATE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED -> SingleValueProfileTemplateTimeBlockChangedEventParser()
            EventType.TOTAL_DAILY_INSULIN_DELIVERY -> TotalDailyInsulinDeliveryEventParser()
            EventType.THERAPY_CONTROL_STATE_CHANGED -> TherapyControlStateChangedEventParser()
            EventType.OPERATIONAL_STATE_CHANGED -> OperationalStateChangedEventParser()
            EventType.RESERVOIR_REMAINING_AMOUNT_CHANGED -> ReservoirRemainingAmountChangedParser()
            EventType.ANNUNCIATION_STATUS_CHANGED_PART_1_OF_2 -> AnnunciationStatusChangedPart1Parser()
            EventType.ANNUNCIATION_STATUS_CHANGED_PART_2_OF_2 -> AnnunciationStatusChangedPart2Parser()
            EventType.TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED -> PairedValueProfileTemplateTimeBlockChangedEventParser()
            EventType.PRIMING_STARTED -> PrimingStartedEventParser()
            EventType.PRIMING_DONE -> PrimingDoneEventParser()
            EventType.DATA_CORRUPTION,
            EventType.POINTER_EVENT -> EmptyEventDataParser()
            EventType.BOLUS_TEMPLATE_CHANGED_PART_1_OF_2 -> BolusTemplateChangedPart1EventParser()
            EventType.BOLUS_TEMPLATE_CHANGED_PART_2_OF_2 -> BolusTemplateChangedPart2EventParser()
            EventType.TBR_TEMPLATE_CHANGED -> TbrTemplateChangedEventParser()
            EventType.MAX_BOLUS_AMOUNT_CHANGED -> MaxBolusAmountChangedEventParser()
            else -> throw IllegalArgumentException("Event type not supported")
        }
    }
}
