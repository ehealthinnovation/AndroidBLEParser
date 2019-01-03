package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Data for [EventType.TBR_ADJUSTMENT_STARTED] event
 * This event should be recorded if a TBR is started via the Insulin Delivery Device or via a Client
 * (i.e., procedure Set TBR Adjustment is executed on the IDD Command CP with Change TBR bit set to
 * False).
 *
 * @property type the TBR type
 * @property value the TBR value If the TBR is absolute (i.e., TBR Type is set to “Absolute”), the
 * TBR Adjustment Value field contains the temporary basal rate as the absolute value in IU/h. If 
 * the TBR is relative (i.e., TBR Type is set to “Relative”), the TBR Adjustment Value field contains
 * a dimensionless scaling factor
 * @property durationMinute the duration of TBR programmed in minutes
 * @property templateNumber if present, it shows the template number used to program TBR adjustment
 */
data class TbrAdjustmentStart(
        val type: TbrType,
        val value: Float,
        val durationMinute: Int,
        val templateNumber: Int?
)

internal class TbrAdjustmentStartedParser : HistoryEventDataParser<TbrAdjustmentStart>() {

    override fun readData(dataReader: DataReader): TbrAdjustmentStart {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val value = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val duration = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val templateNumber =
                if (flags.contains(Flag.TBR_TEMPLATE_NUMBER_PRESENT)) {
                    dataReader.getNextInt(IntFormat.FORMAT_UINT8)
                } else null

        return TbrAdjustmentStart(type, value, duration, templateNumber)
    }

    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the TBR Template Number field is present. */
        TBR_TEMPLATE_NUMBER_PRESENT(0);
    }
}
