package org.ehealthinnovation.android.bluetooth.parser

/**
 * Interface for enum class implementing status flags. These status flags have none or multiple bits set.
 * @property bitOffset the offset an enum flag represents. For example [bitOffset] = 5 means the fifth least significant bit is set
 */
interface FlagValue {
    val bitOffset: Int
}