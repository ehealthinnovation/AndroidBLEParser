package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.*


/**
 * Data for [EventType.TBR_ADJUSTMENT_ENDED]
 *
 * This event should be recorded if a TBR ended (i.e., end of programmed duration or canceled).
 *
 * @property lastSetTbrType the type of TBR that just ended
 * @property effectiveDurationMinute the effective duration of the TBR that just ended
 * @property endReason the reason for ending TBR
 * @property lastSetTbrTemplateNumber if not null, it indicates the TBR template number used to program the TBR
 * @property annunciationId if not null, it indicates the annunciation ID associated with the TBR just ended.
 */
data class TbrAdjustmentEnded(
        val lastSetTbrType: TbrType,
        val effectiveDurationMinute: Int,
        val endReason: TbrEndReason,
        val lastSetTbrTemplateNumber: Int?,
        val annunciationId: Int?
)

internal class TbrAdjustmentEndedEventParser : HistoryEventDataParser<TbrAdjustmentEnded>() {

    override fun readData(dataReader: DataReader): TbrAdjustmentEnded {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TbrType::class.java, TbrType.RESERVED_FOR_FUTURE_USE)
        val duration = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val endReason = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), TbrEndReason::class.java, TbrEndReason.RESERVED_FOR_FUTURE_USE)
        val templateNumber =
                if (flags.contains(Flag.LAST_SET_TBR_TEMPLATE_NUMBER_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT8) else null
        val annunciationId =
                if (flags.contains(Flag.ANNUNCIATION_INSTANCE_ID_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null

        return TbrAdjustmentEnded(type, duration, endReason, templateNumber, annunciationId)
    }


    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Last Set TBR Template Number field is present. */
        LAST_SET_TBR_TEMPLATE_NUMBER_PRESENT(0),
        /**If this bit is set, the Annunciation Instance ID field is present. */
        ANNUNCIATION_INSTANCE_ID_PRESENT(1);
    }

}

enum class TbrEndReason(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    /**The reason why the TBR ended is undetermined. */
    UNDETERMINED(0x0F),
    /**The programmed duration of the TBR is over. */
    PROGRAMMED_DURATION_OVER(0x33),
    /**The TBR was canceled by an interaction (e.g., by the user via a remote control or the device itself). */
    CANCELED(0x3C),
    /**The TBR was aborted due to an error. */
    ERROR_ABORT(0x55);
}
