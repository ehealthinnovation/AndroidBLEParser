package org.ehealthinnovation.android.bluetooth.parser

import android.bluetooth.BluetoothGattCharacteristic
import kotlin.math.pow

/**
 * Provides buffer writing into a [BluetoothGattCharateristic]
 *
 * Auto advances the buffer offset to the next available value slot.
 */
class BluetoothGattDataWriter(private val destination: BluetoothGattCharacteristic) : DataWriter {
    /**
     * Current offset into the gatt buffer.
     */

    var offset: Int = 0

    override fun putInt(data: Int, formatType: IntFormat) {
        if (destination.setValue(data, formatType.formatCode, offset)) {
            offset += formatType.lengthBytes
        } else {
            throw RuntimeException("Failed to write int into buffer")
        }
    }

    override fun putFloat(value: Float, exponent: Int, formatType: FloatFormat) {
        val mantissa = findMantissa(value, exponent)
        if (destination.setValue(mantissa, exponent, formatType.formatCode, offset)) {
            offset += formatType.lengthBytes
        } else {
            throw RuntimeException("Failed to write float into buffer")
        }

    }

    override fun putString(data: String) {
        if (destination.setValue(data)) {
            offset += data.length + 1
        } else {
            throw RuntimeException("Failed to write string into buffer")
        }
    }
}

/**
 * Find the mantissa from a given value and exponent.
 */
internal fun findMantissa(value: Float, exponent: Int): Int = (value / 10f.pow(exponent)).toInt()

