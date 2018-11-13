package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicPacket
import org.ehealthinnovation.android.bluetooth.parser.CharacteristicParser
import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Produces a [GlucoseMeasurement] characteristic from a matching [CharacteristicPacket]
 *
 * @see [CharacteristicParser]
 */
class GlucoseMeasurementParser : CharacteristicParser<GlucoseMeasurement> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == GlucoseUuid.GLUCOSE_MEASUREMENT.uuid
    }

    override fun parse(packet: CharacteristicPacket): GlucoseMeasurement {
        val data = packet.readData()

        val (C1, C2, C3, C5, contextFollows) = readFlagConditions(data)
        val sequenceNumber = readSequenceNumber(data)
        val dateTime = readDate(data)
        val timeOffset = if (C1) readTimeOffset(data) else null
        val sample = if (C2) readGlucoseSample(readGlucoseUnit(C3), data) else null
        val sensorStatus = if (C5) readSensorFlags(data) else null

        return GlucoseMeasurement(sequenceNumber, dateTime, timeOffset, sample, sensorStatus, contextFollows)
    }
}

/**
 * Specification conditions derived from the Flags value of the characteristic packet.
 */
internal data class FlagConditions(
        val C1:Boolean,
        val C2:Boolean,
        val C3:Boolean,
        // C4 is actually just the mutual exclusion of C3, so skip
        val C5:Boolean,
        val contextFollows:Boolean
)

/**
 * Read mandatory flags and convert into their corresponding specification conditions.
 * The result is the C1 - C5 conditions found in the [GlucoseMeasurement] specification.
 */
internal fun readFlagConditions(data: DataReader): FlagConditions {
    val rawFlags = data.getNextInt(IntFormat.FORMAT_UINT8)
    val flags = parseFlags(rawFlags, MeasurementFlag::class.java)

    val C1 = flags.contains(MeasurementFlag.TIME_OFFSET_PRESENT)
    val C2 = flags.contains(MeasurementFlag.GLUCOSE_CONCENTRATION_TYPE_SAMPLE_LOCATION_PRESENT)
    val C3 = !flags.contains(MeasurementFlag.GLUCOSE_CONCENTRATION_UNITS)
    val C5 = flags.contains(MeasurementFlag.SENSOR_STATUS_ANNUNCIATION_PRESENT)
    val contextFollows = flags.contains(MeasurementFlag.CONTEXT_INFORMATION_FOLLOWS)

    return FlagConditions(C1, C2, C3, C5, contextFollows)
}

/**
 * Read mandatory sequence number.
 */
internal fun readSequenceNumber(data: DataReader): Int {
    return data.getNextInt(IntFormat.FORMAT_UINT16)
}

/**
 * Read mandatory date.
 */
internal fun readDate(data: DataReader): BluetoothDateTime {
    return readDateTime(data)
}

/**
 * Read time offset from the current packet offset.
 */
internal fun readTimeOffset(data: DataReader): Int {
    return data.getNextInt(IntFormat.FORMAT_SINT16)
}

/**
 * Read a [ConcentrationUnits] from the C3 condition.
 */
internal fun readGlucoseUnit(C3: Boolean): ConcentrationUnits {
    return if (C3) ConcentrationUnits.KGL else ConcentrationUnits.MOLL
}

/**
 * Read [GlucoseSample] from the current packet offset.
 */
internal fun readGlucoseSample(units:ConcentrationUnits, data : DataReader): GlucoseSample {

    val concentration = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
    val (fluidInt, locationInt) = readNibbles(data.getNextInt(IntFormat.FORMAT_UINT8))

    val sampleType = readEnumeration(fluidInt, SampleType::class.java, SampleType.RESERVED_FOR_FUTURE)
    val sampleLocation = readEnumeration(locationInt, SampleLocation::class.java, SampleLocation.RESERVED_FOR_FUTURE)

    return GlucoseSample(concentration, units, sampleType, sampleLocation)
}

/**
 * Read sensor status from the current packet offset.
 */
internal fun readSensorFlags(data: DataReader): EnumSet<SensorStatus> {
    val rawStatus = data.getNextInt(IntFormat.FORMAT_UINT16)
    return parseFlags(rawStatus, SensorStatus::class.java)
}