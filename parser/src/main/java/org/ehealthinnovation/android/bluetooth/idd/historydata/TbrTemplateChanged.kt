package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration

/**
 * Event Data for [EventType.TBR_TEMPLATE_CHANGED] history event
 *
 *This event should be recorded if the settings of a TBR template are changed via the Insulin
 * Delivery Device or via a Client (i.e., procedure Set TBR Template is executed on the IDD Command
 * CP).
 *
 * @property templateNumber the number of the TBR template
 * @property type the TBR value type
 * @property value the TBR value. It has unit IU/h if [type] is [TbrType.ABSOLUTE]. It has no unit
 * but should be expressed as a percentage if [type] is [TbrType.RELATIVE]
 * @property duration The duration of TBR in effect in minutes
 */
data class TbrTemplateChanged(
        val templateNumber: Int,
        val type: TbrType,
        val value: Float,
        val duration: Int
)

internal class TbrTemplateChangedEventParser : HistoryEventDataParser<TbrTemplateChanged>() {
    override fun readData(dataReader: DataReader): TbrTemplateChanged {
        val templateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val value = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val duration = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        return TbrTemplateChanged(templateNumber, type, value, duration)
    }
}