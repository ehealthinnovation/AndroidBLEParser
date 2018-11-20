package org.ehealthinnovation.android.bluetooth.parser

/**
 * Readable data buffer.
 */
interface DataReader {

    /**
     * Get the next integer of the specified format from the buffer.
     * Advances the buffer to the next element.
     *
     * @return the next [Int] value in the buffer.
     *
     * @throws Exception if the next value can not be read (type mismatch or end of buffer).
     */
    fun getNextInt(formatType: IntFormat): Int

    /**
     * Get the next floating point number of the specified format from the buffer.
     * Advances the buffer to the next element.
     *
     * @return the next [Float] value in the buffer.
     *
     * @throws Exception if the next value can not be read (type mismatch or end of buffer).
     */
    fun getNextFloat(formatType: FloatFormat): Float

    /**
     * Get the remainder of the buffer as a String.
     * Advances the buffer to the end.
     * Throws an exception if no next value is available.
     *
     * @return The next [String] value in the buffer.
     *
     * @throws Exception if the next value can not be read (type mismatch or end of buffer)
     */
    fun getNextString(): String

}