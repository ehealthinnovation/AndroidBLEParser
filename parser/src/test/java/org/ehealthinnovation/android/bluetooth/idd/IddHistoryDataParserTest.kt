package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.idd.historydata.*
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class IddHistoryDataParserTest {

    lateinit var mockParser: IddHistoryDataParser

    @Before
    fun setupMockParser() {
        mockParser = mock()
        whenever(mockParser.parse(any())).thenCallRealMethod()
    }

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B28-0000-1000-8000-00805F9B34FB", IddHistoryDataParser()::canParse)
    }

    @Test
    fun parseReferenceTimeEventIntegrationTest() {
        val mockReferenceTimeEvent = MockCharacteristicPacket.mockPacketForRead(
                uint16(EventType.REFERENCE_TIME.key), uint32(10), uint16(11),
                uint8(RecordingReason.DATE_TIME_LOSS.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint8(-1), uint8(2)
        )
        val expected = HistoryEvent(EventInfo(EventType.REFERENCE_TIME, 10, 11),
                ReferenceTime(
                        RecordingReason.DATE_TIME_LOSS, BluetoothDateTime(2018, 1, 2, 3, 4, 5),
                        BluetoothTimeZone(-1), DstOffset.HALF_AN_HOUR_DAYLIGHT_TIME)
        )

        Assert.assertEquals(expected, IddHistoryDataParser().parse(mockReferenceTimeEvent))
    }

    @Test
    fun parseMethodWhiteBoxTest() {
        val mockReferenceTimeEvent = MockCharacteristicPacket.mockPacketForRead(
                uint16(EventType.REFERENCE_TIME.key), uint32(10), uint16(11),
                uint8(RecordingReason.DATE_TIME_LOSS.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                sint8(-1), uint8(2)
        )
        val mockHistoryEventParser = mock<HistoryEventDataParser<ReferenceTime>>()
        whenever(mockParser.getEventDataParser(any())).thenReturn(mockHistoryEventParser)
        mockParser.parse(mockReferenceTimeEvent)
        inOrder(mockParser, mockHistoryEventParser) {
            verify(mockParser, times(1)).getEventDataParser(EventType.REFERENCE_TIME)
            verify(mockHistoryEventParser, times(1)).parseEvent(any(), any())
        }
    }

    @Test
    fun getTheRightHistoryEventDataParser() {
        val eventParserMap = mapOf<EventType, Class<out Any>>(
                Pair(EventType.REFERENCE_TIME, ReferenceTimeEventParser::class.java),
                Pair(EventType.REFERENCE_TIME_BASE_OFFSET, ReferenceTimeBaseOffsetEventParser::class.java),
                Pair(EventType.BOLUS_CALCULATED_PART_1_OF_2, BolusCalculatedEventParser::class.java),
                Pair(EventType.BOLUS_CALCULATED_PART_2_OF_2, BolusCalculatedEventParser::class.java),
                Pair(EventType.BOLUS_DELIVERED_PART_1_OF_2, BolusProgrammedPart1EventParser::class.java),
                Pair(EventType.BOLUS_DELIVERED_PART_2_OF_2, BolusDeliveredPart2EventParser::class.java),
                Pair(EventType.BOLUS_PROGRAMMED_PART_1_OF_2, BolusProgrammedPart1EventParser::class.java),
                Pair(EventType.BOLUS_PROGRAMMED_PART_2_OF_2, BolusProgrammedPart2EventParser::class.java),
                Pair(EventType.DELIVERED_BASAL_RATE_CHANGED, DeliveredBasalRateChangedEventParser::class.java),
                Pair(EventType.TBR_ADJUSTMENT_STARTED, TbrAdjustmentStartedParser::class.java),
                Pair(EventType.TBR_ADJUSTMENT_ENDED, TbrAdjustmentEndedEventParser::class.java),
                Pair(EventType.TBR_ADJUSTMENT_CHANGED, TbrAdjustmentChangedEventParser::class.java),
                Pair(EventType.PROFILE_TEMPLATE_ACTIVATED, ProfileTemplateActivatedEventParser::class.java),
                Pair(EventType.BASAL_RATE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED, SingleValueProfileTemplateTimeBlockChangedEventParser::class.java),
                Pair(EventType.I2CHO_RATIO_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED, SingleValueProfileTemplateTimeBlockChangedEventParser::class.java),
                Pair(EventType.ISF_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED, SingleValueProfileTemplateTimeBlockChangedEventParser::class.java),
                Pair(EventType.TOTAL_DAILY_INSULIN_DELIVERY, TotalDailyInsulinDeliveryEventParser::class.java),
                Pair(EventType.THERAPY_CONTROL_STATE_CHANGED, TherapyControlStateChangedEventParser::class.java),
                Pair(EventType.OPERATIONAL_STATE_CHANGED, OperationalStateChangedEventParser::class.java),
                Pair(EventType.RESERVOIR_REMAINING_AMOUNT_CHANGED, ReservoirRemainingAmountChangedParser::class.java),
                Pair(EventType.ANNUNCIATION_STATUS_CHANGED_PART_1_OF_2, AnnunciationStatusChangedPart1Parser::class.java),
                Pair(EventType.ANNUNCIATION_STATUS_CHANGED_PART_2_OF_2, AnnunciationStatusChangedPart2Parser::class.java),
                Pair(EventType.TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED, PairedValueProfileTemplateTimeBlockChangedEventParser::class.java),
                Pair(EventType.PRIMING_STARTED, PrimingStartedEventParser::class.java),
                Pair(EventType.PRIMING_DONE, PrimingDoneEventParser::class.java),
                Pair(EventType.DATA_CORRUPTION, EmptyEventDataParser::class.java),
                Pair(EventType.POINTER_EVENT, EmptyEventDataParser::class.java),
                Pair(EventType.BOLUS_TEMPLATE_CHANGED_PART_1_OF_2, BolusTemplateChangedPart1EventParser::class.java),
                Pair(EventType.BOLUS_TEMPLATE_CHANGED_PART_2_OF_2, BolusTemplateChangedPart2EventParser::class.java),
                Pair(EventType.TBR_TEMPLATE_CHANGED, TbrTemplateChangedEventParser::class.java),
                Pair(EventType.MAX_BOLUS_AMOUNT_CHANGED, MaxBolusAmountChangedEventParser::class.java)
        )

        for (entry in eventParserMap) {
            val parser = IddHistoryDataParser().getEventDataParser(entry.key)
            val parserType = entry.value
            Assert.assertTrue(parser.javaClass == parserType)
        }
    }


}
