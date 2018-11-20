package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test
import java.util.*

class CrcUtilityTest {


    @Test
    fun calculateCcittCrc16SmokeTest() {

        val testVectors: List<Pair<Short, ByteArray>> = arrayListOf(
                //Arguments: First the correct CRC 16, Second the input message byte array
                Pair(0xf0b8.toShort(), byteArrayOf(0x00, 0x00)),
                Pair(0x63ef.toShort(), byteArrayOf(0x01, 0x00, 0x00)),
                //A typical glucose measurement packet
                Pair(0x8368.toShort(), byteArrayOf(0x13, 0x01, 0x00, 0xe2.toByte(), 0x07, 0x06, 0x06, 0x0e, 0x35, 0x0e, 0x00, 0x00, 0x54, 0xb0.toByte(), 0x19)),
                //A typical glucose measurement packet
                Pair(0xa45f.toShort(), byteArrayOf(0x03, 0x02, 0x00, 0xe2.toByte(), 0x07, 0x06, 0x06, 0x0e, 0x36, 0x12, 0x10, 0x00, 0x60, 0xb0.toByte(), 0x11.toByte()))
        )
        for (testVector in testVectors) {
            Assert.assertEquals(testVector.first, BluetoothCrcUtility.calculateCrc16(testVector.second, testVector.second.size))
        }
    }

    @Test
    fun ccittCrc16EncondedMessagesVerification() {
        val testVectors: List<Pair<Boolean, ByteArray>> = arrayListOf(
                //Arguments: First the output of verification, Second the input message to test
                Pair(true, byteArrayOf(0x00, 0x00, 0xb8.toByte(), 0xf0.toByte())),
                Pair(true, byteArrayOf(0x01, 0x00, 0x00, 0xef.toByte(), 0x63)),
                Pair(false, byteArrayOf(0x01, 0x00, 0x00, 0xef.toByte(), 0x64)),
                //A typical glucose measurement packet with correct CCITT CRC16 code attached
                Pair(true, byteArrayOf(0x13, 0x01, 0x00, 0xe2.toByte(), 0x07, 0x06, 0x06, 0x0e, 0x35, 0x0e, 0x00, 0x00, 0x54, 0xb0.toByte(), 0x19.toByte(), 0x68, 0x83.toByte())),
                //A typical glucose measurement packet with incorrect CCITT CRC16 code attached
                Pair(false, byteArrayOf(0x13, 0x01, 0x00, 0xe2.toByte(), 0x07, 0x06, 0x06, 0x0e, 0x35, 0x0e, 0x00, 0x00, 0x54, 0xb0.toByte(), 0x19.toByte(), 0x68, 0x84.toByte()))
        )
        for (testVector in testVectors) {
            Assert.assertEquals(testVector.first, BluetoothCrcUtility.verifyCrc16(testVector.second, testVector.second.size))
        }
    }

    @Test
    fun attachCcittCrc16() {
         val testVectors: List<Pair<ByteArray, ByteArray>> = arrayListOf(
                 //Arguments: First the output of message with CRC code attached, Second the input message
                Pair(byteArrayOf(0x00, 0x00, 0xb8.toByte(), 0xf0.toByte()), byteArrayOf(0x00, 0x00)),
                Pair(byteArrayOf(0x01, 0x00, 0x00, 0xef.toByte(), 0x63), byteArrayOf(0x01, 0x00, 0x00)),
                //A typical glucose measurement packet with correct CCITT CRC16 code attached
                Pair(byteArrayOf(0x13, 0x01, 0x00, 0xe2.toByte(), 0x07, 0x06, 0x06, 0x0e, 0x35, 0x0e, 0x00, 0x00, 0x54, 0xb0.toByte(), 0x19.toByte(), 0x68, 0x83.toByte()),
                        byteArrayOf(0x13, 0x01, 0x00, 0xe2.toByte(), 0x07, 0x06, 0x06, 0x0e, 0x35, 0x0e, 0x00, 0x00, 0x54, 0xb0.toByte(), 0x19.toByte()))
        )
        for (testVector in testVectors) {
            Assert.assertTrue(Arrays.equals(testVector.first,BluetoothCrcUtility.attachCrc16(testVector.second, testVector.second.size)))
        }
    }
}