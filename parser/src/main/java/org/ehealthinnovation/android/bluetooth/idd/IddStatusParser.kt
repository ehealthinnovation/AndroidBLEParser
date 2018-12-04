package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*

/**
 * Parse the Idd Status Characteristic
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.idd_status.xml
 */
class IddStatusParser : CharacteristicParser<IddStatus> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.STATUS.uuid
    }

    override fun parse(packet: CharacteristicPacket): IddStatus {
        val data = packet.readData()

        val therapyControlState = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                TherapyControlState::class.java,
                TherapyControlState.RESERVED_FOR_FUTURE_USE
        )

        val operationalState = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                OperationalState::class.java,
                OperationalState.RESERVED_FOR_FUTURE_USE
        )

        val remainingRemainingAmount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)

        val isReservoirAttached = readFlagsForReservoirState(data)

        return IddStatus(
                therapyControlState,
                operationalState,
                remainingRemainingAmount,
                isReservoirAttached
        )

    }

    internal fun readFlagsForReservoirState(data: DataReader): Boolean {
        val flags = parseFlags(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                IddStatusFlags::class.java
        )
        return flags.contains(IddStatusFlags.RESERVOIR_ATTACHED)
    }

}