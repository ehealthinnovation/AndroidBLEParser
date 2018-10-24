package org.ehealthinnovation.android.bluetooth.parser

/**
 * Base class for Bluetooth characteristic parsers.
 */
interface CharacteristicParser<out T> {

    /**
     * Determine if the [CharacteristicPacket] can be parsed by this parser.
     * Callers should only call [parse] if [canParse] returns true.
     *
     * @return true if the specified [CharacteristicPacket] is supported by this parser.
     *         false otherwise.
     */
    fun canParse(packet: CharacteristicPacket): Boolean

    /**
     * Parse the provided [CharacteristicPacket] into an object representing the contained data.
     * Callers should only call [parse] if [canParse] returned true.
     *
     * @return a new instance of a structured characteristic object that represents the data in the packet.
     *
     * @throws Exception if the parser encounters an error while parsing the packet.
     */
    fun parse(packet: CharacteristicPacket): T
}