package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.Bolus
import org.ehealthinnovation.android.bluetooth.idd.BolusActivationType
import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Data for [BolusDeliveredPart2Event]
 *
 * The Bolus Delivered events should be recorded if the delivery of bolus is terminated (i.e., the
 * programmed amounts including the extended amount of an extended or multiwave bolus has been
 * delivered completely or partly if the bolus was canceled by the user or due to an error).
 *
 * @property startTimeOffsetSecond The Bolus Start Time Offset field describes the start time of the bolus delivery in relative time since the timestamp of the corresponding Bolus Programmed Part 1 of 2 event in seconds (i.e., the Bolus ID fields of these events have the same value).
 * @property activationType if not null, it records the bolus activation method.
 * @property endReason if not null, it indicates the reason for bolus delivery termination
 * @property annunciationId if not null, An annunciation corresponding to this event occurred and this field represents the unique identifier of that annunciation
 *
 * @note
 * Data for [BolusDeliveredPart1Event] is [Bolus], which is not redeclared here.
 */
data class BolusDeliveredStatus(
        val startTimeOffset: Int,
        val activationType: BolusActivationType?,
        val endReason: BolusEndReason?,
        val annunciationId: Int?
)

/**
 * This enum is used to specify the bolus delivery ending reason in [BolusDeliveredStatus]
 */
enum class BolusEndReason(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    /**The reason why the Bolus ended is undetermined. */
    UNDETERMINED(0x0F),
    /**The programmed duration of the Bolus is over. */
    PROGRAMMED_DURATION_OVER(0x33),
    /**The Bolus was canceled by an interaction (e.g., by the user via a remote control or the device itself). */
    CANCELED(0x3C),
    /**The Bolus was aborted due to an error. */
    ERROR_ABORT(0x55);

}

internal class BolusDeliveredPart2EventParser : HistoryEventDataParser<BolusDeliveredStatus>(){

    override fun readData(dataReader: DataReader): BolusDeliveredStatus {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val startTimeOffset = dataReader.getNextInt(IntFormat.FORMAT_UINT32)
        val activationType = if (flags.contains(Flag.BOLUS_ACTIVATION_TYPE_PRESENT)) readActivationType(dataReader) else null
        val endReason = if (flags.contains(Flag.BOLUS_END_REASON_PRESENT)) readEndReason(dataReader) else null
        val annunciationId = if (flags.contains(Flag.ANNUNCIATION_INSTANCE_ID_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        return BolusDeliveredStatus(startTimeOffset, activationType, endReason, annunciationId)
    }

    internal fun readActivationType(data: DataReader): BolusActivationType =
            readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), BolusActivationType::class.java, BolusActivationType.RESERVED_FOR_FUTURE_USE)

    internal fun readEndReason(data: DataReader): BolusEndReason =
            readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), BolusEndReason::class.java, BolusEndReason.RESERVED_FOR_FUTURE_USE)


    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Bolus Activation Type field is present. */
        BOLUS_ACTIVATION_TYPE_PRESENT(0),
        /**If this bit is set, the Bolus End Reason field is present. */
        BOLUS_END_REASON_PRESENT(1),
        /**If this bit is set, the Annunciation Instance ID field is present. */
        ANNUNCIATION_INSTANCE_ID_PRESENT(2);
    }
}