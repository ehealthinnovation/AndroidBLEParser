package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.AnnunciationStatus
import org.ehealthinnovation.android.bluetooth.idd.AnnunciationType
import org.ehealthinnovation.android.bluetooth.parser.*


/**
 * This is the history event data for [EventType.ANNUNCIATION_STATUS_CHANGED_PART_1_OF_2] history event.
 *
 * The Annunciation Status Changed events should be recorded if a new annunciation is reported.
 *
 * Each AuxInfo field is two bytes data to provide specific details to the annunciation. The interpretation of
 * auxInfo may be different for each manufacturer. Please consult their documentation on how to interpret the data.
 *
 * @property id The id of The annunciation
 * @property type the type of annunciation
 * @property status the status of annunciation
 * @property auxInfo1 if not null, it is a two bytes information supplementing the annunciation
 * @property auxInfo2 if not null, it is a two bytes information supplementing the annunciation
 */
data class AnnunciationStatusChanged(
        val id: Int,
        val type: AnnunciationType,
        val status: AnnunciationStatus,
        val auxInfo1: Int?,
        val auxInfo2: Int?
)

/**
 *
 * This is the history event data for [EventType.ANNUNCIATION_STATUS_CHANGED_PART_2_OF_2] history event. Part 2 of
 * annunciation must appear with part 1 of the annunciation. If [auxInfo3] - [auxInfo5] is absent, the part 2 annunciation
 * has only null property value.
 *
 * The Annunciation Status Changed events should be recorded if a new annunciation is reported.
 *
 * Each AuxInfo field is two bytes data to provide specific details to the annunciation. The interpretation of
 * auxInfo may be different for each manufacturer. Please consult their documentation on how to interpret the data.
 *
 * @property auxInfo3 if not null, it is a two bytes information supplementing the annunciation
 * @property auxInfo4 if not null, it is a two bytes information supplementing the annunciation
 * @property auxInfo5 if not null, it is a two bytes information supplementing the annunciation
 *
 */
data class AnnunciationStatusChangeAdditionalInfo(
        val auxInfo3: Int?,
        val auxInfo4: Int?,
        val auxInfo5: Int?
)

internal class AnnunciationStatusChangedPart1Parser : HistoryEventDataParser<AnnunciationStatusChanged>() {

    override fun readData(dataReader: DataReader): AnnunciationStatusChanged {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val id = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
        val type = readEnumeration(
                dataReader.getNextInt(IntFormat.FORMAT_UINT16),
                AnnunciationType::class.java,
                AnnunciationType.RESERVED_FOR_FUTURE_USE
        )
        val status = readEnumeration(
                dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                AnnunciationStatus::class.java,
                AnnunciationStatus.RESERVED_FOR_FUTURE_USE
        )
        val auxInfo1 = if (flags.contains(Flag.AUXINFO1_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        val auxInfo2 = if (flags.contains(Flag.AUXINFO2_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        return AnnunciationStatusChanged(id, type, status, auxInfo1, auxInfo2)
    }

    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the AuxInfo1 field is present. */
        AUXINFO1_PRESENT(0),
        /**If this bit is set, the AuxInfo2 field is present. */
        AUXINFO2_PRESENT(1);
    }
}


class AnnunciationStatusChangedPart2Parser : HistoryEventDataParser<AnnunciationStatusChangeAdditionalInfo>() {

    override fun readData(dataReader: DataReader): AnnunciationStatusChangeAdditionalInfo {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val auxInfo3 = if (flags.contains(Flag.AUXINFO3_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        val auxInfo4 = if (flags.contains(Flag.AUXINFO4_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        val auxInfo5 = if (flags.contains(Flag.AUXINFO5_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        return AnnunciationStatusChangeAdditionalInfo(auxInfo3, auxInfo4, auxInfo5)
    }

    private enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the AuxInfo3 field is present. */
        AUXINFO3_PRESENT(0),
        /**If this bit is set, the AuxInfo4 field is present. */
        AUXINFO4_PRESENT(1),
        /**If this bit is set, the AuxInfo5 field is present*/
        AUXINFO5_PRESENT(2);
    }
}