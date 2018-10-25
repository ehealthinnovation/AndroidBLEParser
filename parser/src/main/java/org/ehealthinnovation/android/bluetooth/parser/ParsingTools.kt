package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.glucose.FlagEnum
import java.util.*

/**
 * Parse an input integer into an [EnumSet] of the given [flagType]
 *
 * @param flagValue The compound integer value representing a set of flags.
 * @param flagType The enum type of the flag, which must implements the [FlagEnum] interface
 *
 * @return A [EnumSet] of the decoded flags
 */
fun <T> parseFlags (flagValue: Int, flagType: Class<T>): EnumSet<T> where T : Enum<T>, T: FlagEnum {
    val resultSet = EnumSet.noneOf(flagType)
    val valueSet =  EnumSet.allOf(flagType)

    for (enumValue in valueSet){

        // Ignore the negative values in the flag set. These are reserved enums that exist
        // outside of the bit space, representing unknown or reserved-for-future values.
        if(enumValue.bitOffset >= 0) {
            val flag: Int = 0x01 shl enumValue.bitOffset
            if ((flag and flagValue) == flag) {
                resultSet.add(enumValue)
            }
        }
    }
    return resultSet
}

/**
 * Retrieve an enumeration from [rawValue] of the specified [enumType].
 * Use this variant of [readEnumeration] if the enumeration type allows for an unsupported value.
 *
 * @return One of [enumType], or [defaultValue] if no matching value is found.
 *
 * @see readEnumeration(rawValue: Int, enumType: Class<T>)
 */
fun <T> readEnumeration(rawValue: Int, enumType: Class<T>, defaultValue: T): T where T : Enum<T>, T: EnumerationValue {
    val map = EnumSet.allOf(enumType).associate { Pair(it.key, it) }
    return map[rawValue] ?: defaultValue
}

/**
 * Retrieve an enumeration from [rawValue] of the specified [enumType].
 * Use this variant of [readEnumeration] if the enumeration type does not allow for an unsupported value.
 *
 * @return One of [enumType].
 *
 * @throws Exception if [rawValue] does not correspond to an [enumType] value.
 *
 * @see readEnumeration(rawValue: Int, enumType: Class<T>, defaultValue: T)
 */
fun <T> readEnumeration(rawValue: Int, enumType: Class<T>): T where T : Enum<T>, T: EnumerationValue {
    val map = EnumSet.allOf(enumType).associate { Pair(it.key, it) }
    return map[rawValue] ?: throw IllegalArgumentException(rawValue.toString() + " is not an enumeration of type " + enumType.simpleName)
}

/**
 * @param value assumed to be a 8bit unsigned int. Data beyond the 8 bit boundary is ignored.
 *
 * @return a pair of integers for each nibble as (least significant, most significant)
 */
fun readNibbles(value: Int): Pair<Int, Int> {
    return Pair(readLeastSignificantNibble(value), readMostSignificantNibble(value))
}

/**
 * @param value assumed to be a 8bit unsigned int. Data beyond the 8 bit boundary is ignored.
 *
 * @return the least significant nibble from [value]
 */
fun readLeastSignificantNibble(value: Int): Int {
    return 0xF and value
}

/**
 * @param value assumed to be a 8bit unsigned int. Data beyond the 8 bit boundary is ignored.
 *
 * @return the most significant nibble from [value]
 */
fun readMostSignificantNibble(value: Int): Int {
    return (value shr 4) and 0xF
}

/**
 * Read a [BluetoothDateTime] from the current point in a [DataReader].
 */
fun readDateTime(data: DataReader): BluetoothDateTime {

    val year = data.getNextInt(IntFormat.FORMAT_UINT16)
    val month = data.getNextInt(IntFormat.FORMAT_UINT8)
    val day = data.getNextInt(IntFormat.FORMAT_UINT8)
    val hour = data.getNextInt(IntFormat.FORMAT_UINT8)
    val minutes = data.getNextInt(IntFormat.FORMAT_UINT8)
    val seconds = data.getNextInt(IntFormat.FORMAT_UINT8)

    return BluetoothDateTime(
            if (year != 0) year else null,
            if (month != 0) month else null,
            if (day != 0) day else null,
            hour,
            minutes,
            seconds
    )
}