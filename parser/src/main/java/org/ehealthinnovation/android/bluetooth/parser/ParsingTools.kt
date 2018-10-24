package org.ehealthinnovation.android.bluetooth.parser

/**
 * @param value asssumed to be a 8bit unsigned int. Data beyond the 8 bit boundary is ignored.
 *
 * @return a pair of integers for each nibble as (least significant, most significant)
 */
fun readNibbles(value: Int): Pair<Int, Int> {
    return Pair(readLeastSignificantNibble(value), readMostSignificantNibble(value))
}

/**
 * @param value asssumed to be a 8bit unsigned int. Data beyond the 8 bit boundary is ignored.
 *
 * @return the least significant nibble from [value]
 */
fun readLeastSignificantNibble(value: Int): Int {
    return 0xF and value
}

/**
 * @param value asssumed to be a 8bit unsigned int. Data beyond the 8 bit boundary is ignored.
 *
 * @return the most significant nibble from [value]
 */
fun readMostSignificantNibble(value: Int): Int {
    return (value shr 4) and 0xF
}