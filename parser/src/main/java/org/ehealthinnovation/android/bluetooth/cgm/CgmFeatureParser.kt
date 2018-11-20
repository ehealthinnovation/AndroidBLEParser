package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

class CgmFeatureParser : CharacteristicParser<CgmFeature> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == CgmUuid.FEATURE.uuid
    }

    override fun parse(packet: CharacteristicPacket): CgmFeature {
        val data = packet.readData()
        val feature = readSupportedFeatures(data)
        val (cgmType, cgmSampleLocation) = readSampleTypeAndLocation(data)
        return CgmFeature(
                supportedFeature = feature,
                sampleType = cgmType,
                sampleLocation = cgmSampleLocation
        )
    }

    internal fun readSupportedFeatures(data: DataReader): EnumSet<Feature> {
        return parseFlags(data.getNextInt(IntFormat.FORMAT_UINT24), Feature::class.java)
    }

    internal fun readSampleTypeAndLocation(data: DataReader): Pair<SampleType, SampleLocation> {
        val (typeKeyValue, locationKeyValue) = readNibbles(data.getNextInt(IntFormat.FORMAT_UINT8))
        val sampleType = readEnumeration(typeKeyValue, SampleType::class.java, SampleType.RESERVED_FOR_FUTURE)
        val sampleLocation = readEnumeration(locationKeyValue, SampleLocation::class.java, SampleLocation.RESERVED_FOR_FUTURE)
        return Pair(sampleType, sampleLocation)
    }
}