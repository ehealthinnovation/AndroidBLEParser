package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.parser.FloatFormat
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Write data buffer
 */
interface DataWriter {
    /**
     * Convert an integer to Bluetooth data format according to [IntFormat], and put it to the
     * data buffer. Advances the buffer to the next available space.
     *
     * @param data  the input value
     *
     * @param formatType the expected Bluetooth format that the [data] will be written into
     *
     * @throws Exception if fails to put the [data] into the buffer (say, buffer is full, or the data
     * value exceeds the Bluetooth [formatType] range
     */
    fun putInt(data: Int, formatType: IntFormat)

    /**
     * Convert a float to Bluetooth data format according to [FloatFormat], and put it to the
     * data buffer. Advances the buffer to the next available space.
     *
     * For Bluetooth floating point number,we can specify the desired decimal precision in representation.
     *
     * For example, 5.30f can has [value]=5.3 and [exponent]=-1, will convert into 53 * 10 ^ -1
     * For example, 5.30f can has [value]=5.3 and [exponent]=-2, will convert into 530 * 10 ^ -2
     *
     * Please read the specific GATT specification to know which decimal precision to set
     *
     * @param value the value of the floating point number.
     *
     * @param exponent the exponent of the floating point number in a decimal representation.
     *
     * @throws Exception if fails to put the data value into the buffer (say, buffer is full, or the data
     * value exceeds the Bluetooth [formatType] range
     */
    fun putFloat(value: Float, exponent: Int, formatType: FloatFormat)

    /**
     * Convert a float to Bluetooth data format according to [FloatFormat], and put it to the
     * data buffer. Advances the buffer to the next available space.
     *
     * This is a more straight forward interface to use. It will calculate the appropriate mantissa
     * and exponent to set into the Bluetooth float number.
     *
     * Implementations should choose the exponent that has the maximum mantissa.
     *
     * For example, to convert 5.3f to a [FloatFormat.FORMAT_SFLOAT], the resulting mantissa=530, and exponent=-2
     *
     * @param data the data to be written into the buffer
     *
     * @throws Exception if fails to put the [data] into the buffer (say, buffer is full, or the data
     * value exceeds the Bluetooth [formatType] range
     */
    //This function is not used in the glucose and cgm, so we left it for future implementation
    //fun putFloat(data: Float, formatType: FloatFormat)




    /**
     * Put a string into the buffer.
     * Advances the buffer to the next available space.
     *
     * @param data The [String] to put into buffer.
     *
     * @throws Exception if fails to put the [data] into the buffer (say, buffer is full)
     */
    fun putString(data: String)
}