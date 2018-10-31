package org.ehealthinnovation.android.bluetooth.parser

/**
 * Base class for Bluetooth characteristic composer.
 */
interface CharacteristicComposer<in T> {
     /**
     * Determine if the [CharacteristicPacket] can be composed by this composer.
     * Callers should only call [compose] if [canCompose] return true.
     *
     * @return true if the specified [CharacteristicPacket] can be composed by this composer.
     *         false otherwise
     */
    fun canCompose(request: T):Boolean


    /**
     * Compose the provided [request] into byte arrays stored in the [dataWriter].
     * Callers should only call [compose] if [canCompose] returned true.
     *
     * The compose is successful unless there is an exception thrown.
     *
     */
    fun compose(request: T,  dataWriter: DataWriter)
}