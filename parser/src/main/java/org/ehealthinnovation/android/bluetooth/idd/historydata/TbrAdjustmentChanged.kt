package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Data for [EventType.TBR_ADJUSTMENT_CHANGED] history event.
 *
 * This event should be recorded if the settings of a TBR template are changed via the Insulin
 * Delivery Device or via a Client (i.e., procedure Set TBR Template is executed on the IDD Command
 * CP).
 *
 * @property type the type of TBR to change to
 * @property value the value of the TBR adjustment. Depending on the TBR type, it has different values. For [TbrType.ABSOLUTE] the unit is IU/h. For other values, it does not have unit
 * @property durationProgrammedMinutes the duration of TBR programmed
 * @property durationElapsedMinutes the elapsed duration of TBR
 * @property templateNumber if not null, it is the template number used to program the TBR adjustment change
 */
data class TbrAdjustmentChanged(
        val type: TbrType,
        val value: Float,
        val durationProgrammedMinutes: Int,
        val durationElapsedMinutes: Int,
        val templateNumber: Int?
)

internal class TbrAdjustmentChangedEventParser : HistoryEventDataParser<TbrAdjustmentChanged>() {

    override fun readData(dataReader: DataReader): TbrAdjustmentChanged {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val value = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val durationProgrammed = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val durationElapsed = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val templateNumber =
                if (flags.contains(Flag.TBR_TEMPLATE_NUMBER_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT8) else null

        return TbrAdjustmentChanged(type, value, durationProgrammed, durationElapsed, templateNumber)

    }

    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the TBR Template Number field is present. */
        TBR_TEMPLATE_NUMBER_PRESENT(0);
    }

}