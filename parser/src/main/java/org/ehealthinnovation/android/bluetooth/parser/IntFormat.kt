package org.ehealthinnovation.android.bluetooth.parser

/**
 * Format codes describing integers in Bluetooth GATT profiles.
 * The format codes match those found in BluetootGattCharacteristic.
 *
 * @see android.bluetooth.BluetoothGattCharacteristic
 */
enum class IntFormat(val formatCode:Int, val minValue: Long, val maxValue: Long) {

    /**
     * Characteristic value format type uint8
     */
    FORMAT_UINT8(0x11, 0, 255),

    /**
     * Characteristic value format type uint16
     */
    FORMAT_UINT16(0x12, 0, 65535),

    /**
     * Characteristic value format type uint24
     */
    FORMAT_UINT24(0x13, 0,16777215),

    /**
     * Characteristic value format type uint32
     */
    FORMAT_UINT32(0x14, 0, 4294967295),

    /**
     * Characteristic value format type sint8
     */
    FORMAT_SINT8(0x21, -128, 127),

    /**
     * Characteristic value format type sint16
     */
    FORMAT_SINT16(0x22, -32768, 32767),

    /**
     * Characteristic value format type sint32
     */
    FORMAT_SINT32(0x24, -2147483648, 2147483647);

    val lengthBytes: Int = getFormatByteLength(formatCode)
}