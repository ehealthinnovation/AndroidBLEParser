package org.ehealthinnovation.android.bluetooth.parser

/**
 * Format codes describing integers in Bluetooth GATT profiles.
 * The format codes match those found in BluetootGattCharacteristic.
 *
 * @see android.bluetooth.BluetoothGattCharacteristic
 */
enum class IntFormat(val formatCode:Int) {

    /**
     * Characteristic value format type uint8
     */
    FORMAT_UINT8(0x11),

    /**
     * Characteristic value format type uint16
     */
    FORMAT_UINT16(0x12),

    /**
     * Characteristic value format type uint32
     */
    FORMAT_UINT32(0x14),

    /**
     * Characteristic value format type sint8
     */
    FORMAT_SINT8(0x21),

    /**
     * Characteristic value format type sint16
     */
    FORMAT_SINT16(0x22),

    /**
     * Characteristic value format type sint32
     */
    FORMAT_SINT32(0x24);

    val lengthBytes: Int = getFormatByteLength(formatCode)
}