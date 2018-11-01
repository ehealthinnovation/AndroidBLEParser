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
        //Test value is within range
        if (intIsValid(data, formatType)) {
            if (destination.setValue(data, formatType.formatCode, offset)) {
                offset += formatType.lengthBytes
            } else {
                throw RuntimeException("Failed to write int into buffer")
            }
        } else {
            throw IllegalArgumentException("Input $data is outside the format type range of $formatType")
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

    /**
     * This function tests if [data] fall within boundaries defined for [formatType]
     *
     * There are two special cases we will skip for testing.
     *
     * 1. for [IntFormat.FORMAT_SINT32], the value range is exactly the same as [Int], so there is no
     * need to check
     *
     * 2. for [IntFormat.FORMAT_UINT32], there is no way to represent values larger than [Int.MAX_VALUE]
     * without flipping the [Int] into a negative value. With no additional information, the underlying logic
     * does not know whether to treat it as a [Int] or unsigned [Int]. Currently, we resort to rely
     * on input type [Int] to gate the maximum input value to [Int.MAX_VALUE] i.e. we cannot pass in
     * 3 billion into the function
     */
    fun intIsValid(data: Int, formatType: IntFormat): Boolean {

        if(formatType == IntFormat.FORMAT_UINT32 || formatType == IntFormat.FORMAT_SINT32){
            return true
        }

        return isIntWithinRange(data.toLong(), formatType)

    }
}

/**
 * Find the mantissa from a given value and exponent.
 */
internal fun findMantissa(value: Float, exponent: Int): Int = (value / 10f.pow(exponent)).toInt()

