package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat

/**
 * Data structure for holding outputs from a bolus calculator.
 *
 * It is used by [EventType.BOLUS_CALCULATED_PART_1_OF_2] and [EventType.BOLUS_CALCULATED_PART_2_OF_2]
 * to represent recommended bolus and confirmed bolus respectively.
 *
 * The insulin pump can include a calculator tha make dosage recommendations. Once a recommendation
 * is created, a [HistoryEvent] is recorded containing [BolusCalculatedData].
 *
 * Recommended [BolusCalculatedData] is the suggestion provided by the insulin pump. Once the user
 * confirms the dosage recommendation, a [HistoryEvent] containing [BolusCalculatedData] is
 * created.
 *
 * @property mealBolusFastAmountIU the fast amount of bolus intended to remove the impact of meal intake. Unit in IU
 * @property mealBolusExtendedAmountIU the extended amount of bolus intended to remove the impact of meal intake. Unit in IU
 * @property correctionBolusFastAmountIU the fast amount of bolus intended to for glucose correction. Unit in IU
 * @property correctionBolusExtendedAmountIU the fast amount of bolus intended to for glucose correction. Unit in IU
 */
data class BolusCalculatedData(
        val mealBolusFastAmountIU: Float,
        val mealBolusExtendedAmountIU: Float,
        val correctionBolusFastAmountIU: Float,
        val correctionBolusExtendedAmountIU: Float
)

/**
 * Parser for Bolus Calculated Part1 and part2 Event (the official name in the standard specification),
 * this event is a history event containing bolus calculator recommendation / confirmation.
 *
 * Part1 and part2 events may not be received at the same time. They are group together because
 * confirmed [BolusCalculatedData] is always related to a recommended [BolusCalculatedData]
 *
 * It will be internally by [IddHistoryDataParser] to parse a history event record.
 *
 * To use this parser, call [parseEvent] with a [DataReader] holding the binary of a recommended [BolusCalculatedData].
 *
 * Before parsing this event data, one needs to fetch and parse the preamble data from [DataReader] for [EventInfo]
 * to determine type of content in the rest of the buffer.
 */
internal class BolusCalculatedEventParser : HistoryEventDataParser<BolusCalculatedData>() {
    override fun readData(dataReader: DataReader): BolusCalculatedData = readCalculatedBolus(dataReader)
}


internal fun readCalculatedBolus(data: DataReader): BolusCalculatedData = BolusCalculatedData(
        data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
        data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
        data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
        data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
)


