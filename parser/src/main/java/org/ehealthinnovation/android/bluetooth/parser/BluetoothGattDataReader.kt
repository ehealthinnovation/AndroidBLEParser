package org.ehealthinnovation.android.bluetooth.parser

import android.bluetooth.BluetoothGattCharacteristic

/**
 * Provides buffer reading from a [BluetoothGattCharacteristic].
 *
 * Retrieving values from the buffer advances the buffer to the next buffer value.
 */
class BluetoothGattDataReader(private val source: BluetoothGattCharacteristic) : DataReader {

    /**
     * Current offset into the gatt buffer.
     */
    var offset: Int = 0

    override fun getNextInt(formatType: IntFormat): Int =
        getNextValue(formatType.formatCode, formatType.lengthBytes, source::getIntValue)

    override fun getNextFloat(formatType: FloatFormat): Float =
        getNextValue(formatType.formatCode, formatType.lengthBytes, source::getFloatValue)

    override fun getNextString(): String =
        source.getStringValue(offset)?.also {
            offset = -1 // Since getStringValue reads the remainder of the buffer, we are at the end of the buffer
        } ?: throw NullPointerException("No string value")

    /**
     * Retrieve the next [formatCode] value in the buffer of size [lengthBytes] using the [nextValue] accessor.
     * Advances the buffer upon retrieval.
     *
     * @return the next value in the buffer.
     *
     * @throws Exception if no next value is available.
     */
    private fun <T> getNextValue(formatCode: Int, lengthBytes: Int, nextValue: (Int, Int)->T): T =
        nextValue(formatCode, offset)?.also {
            offset += lengthBytes
        } ?: throw NullPointerException("No next value")
}