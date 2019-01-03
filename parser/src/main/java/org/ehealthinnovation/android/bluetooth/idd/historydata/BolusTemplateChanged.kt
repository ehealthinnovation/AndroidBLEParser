package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Bolus
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.readBolus
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.parseFlags


/**
 * This is the event data for [EventType.BOLUS_TEMPLATE_CHANGED_PART_1_OF_2] history event.
 *
 * This event should be recorded if the settings of a bolus template are changed via the Insulin
 * Delivery Device or via a Client (i.e., procedure Set Bolus Template is executed on the IDD
 * Command CP).
 *
 * @property templateNumber the bolus template number having been changed
 * @property bolus the bolus content defined in the template
 */
data class BolusTemplateChangedBolusPart(
        val templateNumber: Int,
        val bolus: Bolus
)

/**
 * This is the event data for [EventType.BOLUS_TEMPLATE_CHANGED_PART_2_OF_2] history event.
 *
 * This event should be record together with [EventType.BOLUS_TEMPLATE_CHANGED_PART_1_OF_2]
 *
 * @property delayMinutes the delay of bolus specified in the template. If null, it means such information
 * is not present in the template, we should treat the bolus with no delay.
 */
data class BolusTemplateChangedBolusDelay(
        val delayMinutes: Int?
)


internal class BolusTemplateChangedPart1EventParser : HistoryEventDataParser<BolusTemplateChangedBolusPart>() {
    override fun readData(dataReader: DataReader): BolusTemplateChangedBolusPart {
        val templateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val bolus = readBolus(dataReader)
        return BolusTemplateChangedBolusPart(templateNumber, bolus)
    }

}

internal class BolusTemplateChangedPart2EventParser : HistoryEventDataParser<BolusTemplateChangedBolusDelay>() {

    override fun readData(dataReader: DataReader): BolusTemplateChangedBolusDelay {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val delayMinutes = if (flags.contains(Flag.BOLUS_DELAY_TIME_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        return BolusTemplateChangedBolusDelay(delayMinutes)
    }


    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Bolus Delay Time field is present. */
        BOLUS_DELAY_TIME_PRESENT(0),
        /**If this bit is set, the reason for the bolus is the correction of a high blood glucose level. */
        BOLUS_DELIVERY_REASON_CORRECTION(1),
        /**If this bit is set, the reason for the bolus is to cover the intake of food. */
        BOLUS_DELIVERY_REASON_MEAL(2);
    }


}