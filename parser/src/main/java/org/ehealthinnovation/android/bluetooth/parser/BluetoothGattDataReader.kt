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
            getNextValue(formatType.formatCode, formatType.lengthBytes, this::getIntValue)

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
    private fun <T> getNextValue(formatCode: Int, lengthBytes: Int, nextValue: (Int, Int) -> T): T =
            nextValue(formatCode, offset)?.also {
                offset += lengthBytes
            } ?: throw NullPointerException("No next value")

    /**
     * A wrapper function around the [BluetoothGattCharacteristic.getIntValue] function which handles
     * the special case to read a 24 bit unsigned Integer
     *
     * @throws Exception if no next value is available
     */
    private fun getIntValue(formatCode: Int, offset: Int): Int =
            when (formatCode) {
                IntFormat.FORMAT_UINT24.formatCode -> getNextU24IntValue()
                else -> source.getIntValue(formatCode, offset)
            }

    /**
     * Get each byte of a Unsigned 24 bit Integer represented in Little-Endian form, and combine them
     * into a single integer.
     */
    private fun getNextU24IntValue(): Int {
        val lowerU8 = getNextInt(IntFormat.FORMAT_UINT8)
        val middleU8 = getNextInt(IntFormat.FORMAT_UINT8)
        val upperU8 = getNextInt(IntFormat.FORMAT_UINT8)
        return (lowerU8 + (middleU8 shl 8) + (upperU8 shl 16))
    }

}