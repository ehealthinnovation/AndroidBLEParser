package org.ehealthinnovation.android.bluetooth.parser


class BluetoothCrcUtility {
    companion object {
        /**
         * Get the CRC check sum from a [message] of [length]. This version is specific to Bluetooth
         * Low Energy, which requires the crc16 buffered used in calculation be initialized to 0xFFFF
         * @return the CRC16 check sum from the message.
         */
        fun calculateCrc16(message: ByteArray, length: Int): Short {
            return CrcUtility.calculateCcittCrc16(0xFFFF, message, length)
        }

        /**
         * Return true if the input [message] of [length] passes the CRC check. Make sure the [message]
         * has included the CCITT-CRC16 code in its body.
         */
        fun verifyCrc16(message: ByteArray, length: Int): Boolean {
            return calculateCrc16(message, length).toInt() == 0
        }

        /**
         * Attach a 16 bit check sum code to the input [message].
         * @return the [message] with the check sum attached to the end
         */
        fun attachCrc16(message: ByteArray, length: Int): ByteArray {
            val crc = calculateCrc16(message, length)
            return message + crc.toByte() + (crc.toInt() shr 8).toByte()
        }

    }
}