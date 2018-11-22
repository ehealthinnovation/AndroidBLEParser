package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * A parser that is capable of producing a [CgmMeasurement] from a Cgm measurement characteristic
 */
class CgmMeasurementParser : CharacteristicParser<CgmMeasurement> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.MEASUREMENT.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmMeasurement {
        val data = packet.readData()
        val size = data.getNextInt(IntFormat.FORMAT_UINT8) //Size is not used but the byte needs to tbe consumed from the buffer to continue parsing
        val flagConditions = readFlagConditions(data)
        val concentration = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val timeOffset = data.getNextInt(IntFormat.FORMAT_UINT16)
        val statusAnnunciation = readSensorStatusAnnunciation(flagConditions, data)
        val rateOfChange = if (flagConditions.C1) data.getNextFloat(FloatFormat.FORMAT_SFLOAT) else null
        val quality = if (flagConditions.C2) data.getNextFloat(FloatFormat.FORMAT_SFLOAT) else null

        return CgmMeasurement(concentration, timeOffset, rateOfChange, quality, statusAnnunciation)

    }

    internal data class FlagConditions(
            val C1: Boolean,
            val C2: Boolean,
            val C4WarningOctetPresent: Boolean,
            val C4CalTempOctetPresent: Boolean,
            val C4StatusOctetPresent: Boolean
    )

    /**
     * Read and parse the flag field into [FlagConditions]
     */
    internal fun readFlagConditions(data: DataReader): FlagConditions {
        val rawFlags = data.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(rawFlags, MeasurementFlag::class.java)

        val C1 = flags.contains(MeasurementFlag.TREND_INFORMATION)
        val C2 = flags.contains(MeasurementFlag.QUALITY)
        val C4WarningOctetPresent = flags.contains(MeasurementFlag.SENSOR_STATUS_ANNUNCIATION_FIELD_WARNING_OCTET)
        val C4CalTempOctetPresent = flags.contains(MeasurementFlag.SENSOR_STATUS_ANNUNCIATION_FIELD_CALTEMP_OCTET)
        val C4StatusOctetPresent = flags.contains(MeasurementFlag.SENSOR_STATUS_ANNUNCIATION_FIELD_STATUS_OCTET)

        return FlagConditions(C1, C2, C4WarningOctetPresent, C4CalTempOctetPresent, C4StatusOctetPresent)
    }

    /**
     * Read and parse the present [StatusFlag] from [data] based on the condition in
     * [FlagConditions]
     */
    internal fun readSensorStatusAnnunciation(flagConditions: FlagConditions, data: DataReader): EnumSet<StatusFlag>? =
            flagConditions.run {
                if (C4CalTempOctetPresent || C4StatusOctetPresent || C4WarningOctetPresent) {
                    var status = EnumSet.noneOf(StatusFlag::class.java)
                    if (C4StatusOctetPresent) {
                        status = combineEnumSet(status, readSensorStatusAnnunciationByOctet(0, data))
                    }
                    if (C4CalTempOctetPresent) {
                        status = combineEnumSet(status, readSensorStatusAnnunciationByOctet(1, data))
                    }
                    if (C4WarningOctetPresent) {
                        status = combineEnumSet(status, readSensorStatusAnnunciationByOctet(2, data))
                    }
                    status
                } else null
            }


    /**
     * Read and generate a [StatusFlag] enum set from a single byte. The 24-bit status flag is
     * organized in three groups, each of which can be present/absent based on condition 4 (C4) of
     * [FlagConditions].
     *
     * This function read in the next available flag group (one octet) and create the corresponding
     * enum set containing the present flag.
     *
     * [byteOffset] determines the position of byte within the 24-bit status field. It takes values
     * of 0 to 2.
     *
     */
    internal fun readSensorStatusAnnunciationByOctet(byteOffset: Int, data: DataReader): EnumSet<StatusFlag> =
            if (byteOffset in 0..2) {
                val bitToShift = byteOffset * 8
                val result = parseFlags(data.getNextInt(IntFormat.FORMAT_UINT8) shl bitToShift, StatusFlag::class.java)
                result
            } else
                throw IllegalArgumentException("the byte offset is out of range")


}
