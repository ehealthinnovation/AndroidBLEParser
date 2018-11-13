package org.ehealthinnovation.android.bluetooth.parser

import java.util.*

class BluetoothUuidHelper {

    @ExperimentalUnsignedTypes
    companion object {
        private const val BASE_STRING = "00000000-0000-1000-8000-00805F9B34FB"
        private val BASE_UUID = UUID.fromString(BASE_STRING)!!
        private const val BASE_UUID_BITS_NUMBER = 96
        private const val SHORT_UUID_STARTING_OFFSET = 32
        private const val lower32BitsMask: Long = 0x0000FFFF

        /**
         * Extract and output a 32 bit short UUID from a 128 bits Bluetooth [UUID].
         */
        internal fun convertUuidToShortUuid(inputUUID: UUID): Int {
            val mostSignificant64BitsOfUuid = inputUUID.mostSignificantBits
            val mostSignificant16BitsOfUuid = (mostSignificant64BitsOfUuid shr SHORT_UUID_STARTING_OFFSET) and lower32BitsMask
            return mostSignificant16BitsOfUuid.toInt()
        }

        /**
         * Create a full 128 bits Bluetooth UUID from the Bluetooth 32Bit [shortUuid] form UUID.
         *
         * [shortUuid] must be in the range of 0x0000 to 0xFFFF
         *
         * @see CoreSpecification downloadable from https://www.bluetooth.com/specifications/bluetooth-core-specification
         * page 1918 (PartB, Section 2.5.1)
         *
         * The relevant equation is copied here for quick reference
         * 128_bit_value = 32_bit_value * 296 + Bluetooth_Base_UUID
         */
        internal fun createUuidFromShortUuid(shortUuid: Int): UUID {
            if (shortUuid in 0..0x0FFFF) {
                val higher64Bits = BASE_UUID.mostSignificantBits + (shortUuid.toLong() shl SHORT_UUID_STARTING_OFFSET)
                return UUID(higher64Bits, BASE_UUID.leastSignificantBits)
            } else {
                throw IllegalArgumentException("Input $shortUuid exceeds the 16 bit UUID range")
            }
        }

        /**
         * Test a if [uuid] conforms with the Bluetooth Sig format. i.e. If the UUID has the same has
         * the base uuid.
         */
        internal fun isValidBluetoothUuid(uuid: UUID): Boolean {
            val inputUuidConvertedToBluetoothUuid = createUuidFromShortUuid(convertUuidToShortUuid(uuid))
            return inputUuidConvertedToBluetoothUuid == uuid
        }
    }
}