package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*

class IddFeatureParser : CharacteristicParser<IddFeature> {

    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.FEATURE.uuid
    }

    override fun parse(packet: CharacteristicPacket): IddFeature {
        val data = packet.readData()
        val e2eCrc = data.getNextInt(IntFormat.FORMAT_UINT16) //This field is mandatory present, but it is not used in parsing.
        val e2eCounter = data.getNextInt(IntFormat.FORMAT_UINT8) //This field is mandatory present, but it is not used in parsing.
        val insulinConcentration = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val flags = parseFlags(data.getNextInt(IntFormat.FORMAT_UINT24), Feature::class.java)

        return IddFeature(
                insulinConcentration,
                flags
        )
    }

}