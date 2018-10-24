package org.ehealthinnovation.android.bluetooth.parser

import java.util.*

/**
 * A packet of unstructured data that represents a Bluetooth characteristic.
 *
 * @see https://www.bluetooth.com/specifications/gatt/characteristics
 */
interface CharacteristicPacket {

    /**
     * The UUID of the Bluetooth characteristic that is represented by this packet of data.
     * This value is a combination of the base UUID and the specification Assigned Number
     *
     * @see https://www.bluetooth.com/specifications/assigned-numbers/service-discovery
     */
    val uuid: UUID

    /**
     * Obtain a [DataReader] to access the data contained within this characteristic.
     *
     * @return a new [DataReader] instance. The read position of each instance is independent from
     *         previous instances. Each instance begins at the beginning of the data packet.
     *
     */
    fun readData(): DataReader
}