package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.historydata.*
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser to parse the response from a Idd Command Control Point
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
