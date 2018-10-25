package org.ehealthinnovation.android.bluetooth.parser

/**
 * Interface for enum flags that represent enumerations in the specification.
 *
 * @property key The integer value of the enumeration defined in the specification.
 *               This is not necessarily the enum ordinal as not all valid enumeration keys start at 0.
 */
interface EnumerationValue {
    val key: Int
}