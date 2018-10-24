package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.glucose.FlagEnum
import java.util.*

/**
 * Parse an input integer into an [EnumSet] of the given [flagType]
 * @param flagValue The integer value of the flag field
 * @param flagType The enum type of the flag, which must implements the [FlagEnum] interface
 * @return A [EnumSet] of the decoded flags
 */
fun <T> parseFlags (flagValue: Int, flagType: Class<T>): EnumSet<T> where T : Enum<T>, T: FlagEnum {
    val resultSet = EnumSet.noneOf(flagType)
    val valueSet =  EnumSet.allOf(flagType)

    for (enumValue in valueSet){
        //Ignore the negative values in the flag set
        if(enumValue.bit >= 0) {
            val flag: Int = 0x01 shl enumValue.bit
            if ((flag and flagValue) == flag) {
                resultSet.add(enumValue)
            }
        }
    }
    return resultSet
}

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
