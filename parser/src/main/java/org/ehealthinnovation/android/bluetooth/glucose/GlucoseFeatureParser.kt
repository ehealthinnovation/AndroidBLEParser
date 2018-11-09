package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Parser for the glucose feature characteristic
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_feature.xml
 * @see [CharacteristicParser]
 */
class GlucoseFeatureParser : CharacteristicParser<GlucoseFeature> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return true
    }

    override fun parse(packet: CharacteristicPacket): GlucoseFeature {
        val data = packet.readData()
        val supportedFeature = readSupportFeatures(data)
        return GlucoseFeature(supportedFeature)

    }
}


/**
 * Read and parse the support feature flags.
 * @param data the data buffer containing the raw data packet
 * @return An enum set containing all supported feature. An empty set results when there is no supported features.
 */
internal fun readSupportFeatures(data: DataReader): EnumSet<Feature> {
    return parseFlags(data.getNextInt(IntFormat.FORMAT_UINT16), Feature::class.java)
}
