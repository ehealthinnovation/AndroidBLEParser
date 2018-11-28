package org.ehealthinnovation.android.bluetooth.parser

import java.util.*

/**
 * Write a byte into [dataWriter]. The byte consists of a [lowerNibble] at the least significant 4 bits,
 * and a [upperNibble] at the most significant 4 bits.
 */
internal fun writeByteFromNibbles(lowerNibble: Int, upperNibble: Int, dataWriter: DataWriter) {
    if ((lowerNibble in 0..0x0f) && (upperNibble in 0..0x0f)) {
        val byteHolder = lowerNibble + (upperNibble shl 4)
        dataWriter.putInt(byteHolder, IntFormat.FORMAT_UINT8)
    } else {
        throw IllegalArgumentException("nibbles have values outside the range of 0 to 15. Writing" +
                "to a byte cause data loss")
    }
}

/**
 * Convert an enum set of flags into [Int].
 *
 * The enum set should contains [FlagValue].
 *
 * Do not use this function when the [FlagValue] has more than 32 bits (the size of [Int])
 *
 * @param input An enum set of [FlagValue]
 *
 * @return the converted integer
 */
internal fun <T> writeEnumFlagsToInteger(input: EnumSet<T>): Int where T : Enum<T>, T : FlagValue {
    var output = 0
    for (flag in input) {
        output += (1 shl flag.bitOffset)
    }
    return output
}

/**
 * Write an enum set of flags into the data buffer through [DataWriter]
 *
 * @param input An enum set of [FlagValue]
 *
 * @param format The bluetooth format that the flag field will be written into
 *
 * @param dataWriter the interface for writing data into a buffer
 */
internal fun <T> writeEnumFlags(input: EnumSet<T>, format: IntFormat, dataWriter: DataWriter) where T : Enum<T>, T : FlagValue {
    val data = writeEnumFlagsToInteger(input)
    dataWriter.putInt(data, format)
}
