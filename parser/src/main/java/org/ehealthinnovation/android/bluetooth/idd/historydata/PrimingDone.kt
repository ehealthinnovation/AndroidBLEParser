package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * The event data for [EventType.PRIMING_DONE] history event.
 *
 * This event should be recorded if the priming of the Insulin Delivery Device is terminated (i.e.,
 * aborted by the user, the programmed insulin amount for the priming was delivered completely, or
 * an error occurred that stopped the priming).
 *
 * @property deliveredAmount the amount of insulin delivered during priming process in UI
 * @property terminationReason the reason for priming termination
 * @property annunciationId if not null, it is the annunciation ID associate with the priming done
 * notification. If null, it means the original packet does not contain such information, and not an
 * erroreous condition.
 */
data class PrimingDone(
        val deliveredAmount: Float,
        val terminationReason: TerminationReason,
        val annunciationId: Int?
)

enum class TerminationReason(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    /**The termination reason of the priming is undetermined. */
    UNDETERMINED(0x0F),
    /**The user aborted the priming of the Insulin Delivery Device (i.e., procedure Stop Priming was executed or the priming was aborted directly on the Insulin Delivery Device). */
    ABORTED_BY_USER(0x33),
    /**The priming volume of the programmed amount provided by the Start Priming procedure was reached. */
    PROGRAMMED_AMOUNT_REACHED(0x3C),
    /**The priming was aborted due to an error. */
    ERROR_ABORT(0x55);
}

internal class PrimingDoneEventParser : HistoryEventDataParser<PrimingDone>() {
    override fun readData(dataReader: DataReader): PrimingDone {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val deliveredAmount = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val terminationReason = readEnumeration(
                dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                TerminationReason::class.java,
                TerminationReason.RESERVED_FOR_FUTURE_USE
        )
        val annunciationId = if (flags.contains(Flag.ANNUNCIATION_INSTANCE_ID_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        return PrimingDone(deliveredAmount, terminationReason, annunciationId)

    }


    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Annunciation Instance ID field is present. */
        ANNUNCIATION_INSTANCE_ID_PRESENT(0);
    }
}
