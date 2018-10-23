package org.ehealthinnovation.android.bluetooth.parser

/**
 * Format codes describing floating point numbers in Bluetooth GATT profiles.
 * The format codes match those found in BluetootGattCharacteristic.
 *
 * @see android.bluetooth.BluetoothGattCharacteristic
 */
enum class FloatFormat(val formatCode:Int) {

    /**
     * Characteristic value format type sfloat (16-bit float)
     */
    FORMAT_SFLOAT(0x32),

    /**
     * Characteristic value format type float (32-bit float)
     */
    FORMAT_FLOAT(0x34);

    val lengthBytes: Int = getFormatByteLength(formatCode)
}