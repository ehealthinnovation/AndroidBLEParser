package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parser to deserialize an byte array into an [IddAnnunciation] characteristic data structure
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.idd_annunciation_status.xml
 */
class IddAnnunciationParser : CharacteristicParser<IddAnnunciation> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.ANNUNCIATION_STATUS.uuid
    }

    override fun parse(packet: CharacteristicPacket): IddAnnunciation {
        val data = packet.readData()
        val flags = readFlag(data)
        val annunciation = if (flags.annunciationPresent) readAnnunciation(flags, data) else null

        return IddAnnunciation(annunciation)
    }

    /** Given the present of an [Annunciation], read from [DataReader] , create and output an [Annunciation] */
    internal fun readAnnunciation(flags: Flags, data: DataReader): Annunciation {
        val id = data.getNextInt(IntFormat.FORMAT_UINT16)
        val type = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT16), AnnunciationType::class.java, AnnunciationType.RESERVED_FOR_FUTURE_USE)
        val status = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT16), AnnunciationStatus::class.java, AnnunciationStatus.RESERVED_FOR_FUTURE_USE)
        val auxInfo = readAuxiliaryInformationList(flags, data)

        return Annunciation(id, type, status, auxInfo)
    }

    /**Read the next five data fields from [DataReader] according to [Flags] and output a list of [AuxiliaryInformation] */
    internal fun readAuxiliaryInformationList(flags: Flags, data: DataReader): List<AuxiliaryInformation> {
        val outputList = arrayListOf<AuxiliaryInformation>()
        if (flags.auxInfo1Present) outputList.add(readNextAuxiliaryInformation(data))
        if (flags.auxInfo2Present) outputList.add(readNextAuxiliaryInformation(data))
        if (flags.auxInfo3Present) outputList.add(readNextAuxiliaryInformation(data))
        if (flags.auxInfo4Present) outputList.add(readNextAuxiliaryInformation(data))
        if (flags.auxInfo5Present) outputList.add(readNextAuxiliaryInformation(data))
        return outputList
    }


    /** Read the next 2 bytes from [DataReader] and output an [AuxiliaryInformation]*/
    internal fun readNextAuxiliaryInformation(data: DataReader): AuxiliaryInformation {
        return AuxiliaryInformation(data.getNextInt(IntFormat.FORMAT_UINT16))
    }

    /**
     * Read the next byte from [DataReader], and parse it into a [Flags] data structure.
     */
    internal fun readFlag(data: DataReader): Flags {
        val flags = parseFlags(data.getNextInt(IntFormat.FORMAT_UINT8), IddAnnunciationFlag::class.java)
        return Flags(
                flags.contains(IddAnnunciationFlag.ANNUNCIATION_PRESENT),
                flags.contains(IddAnnunciationFlag.AUXINFO1_PRESENT),
                flags.contains(IddAnnunciationFlag.AUXINFO2_PRESENT),
                flags.contains(IddAnnunciationFlag.AUXINFO3_PRESENT),
                flags.contains(IddAnnunciationFlag.AUXINFO4_PRESENT),
                flags.contains(IddAnnunciationFlag.AUXINFO5_PRESENT)
        )
    }

    internal data class Flags(
            val annunciationPresent: Boolean,
            val auxInfo1Present: Boolean,
            val auxInfo2Present: Boolean,
            val auxInfo3Present: Boolean,
            val auxInfo4Present: Boolean,
            val auxInfo5Present: Boolean
    )
}