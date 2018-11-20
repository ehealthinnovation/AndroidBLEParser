package org.ehealthinnovation.android.bluetooth.parser

/**
 * Decodes a length in bytes from the provided formatCode.
 *
 * @see FloatFormat.formatCode
 * @see IntFormat.formatCode
 */
fun getFormatByteLength(formatCode: Int): Int {
    return formatCode.and(0x0F)
}