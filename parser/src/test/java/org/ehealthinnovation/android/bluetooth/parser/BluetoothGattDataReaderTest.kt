package org.ehealthinnovation.android.bluetooth.parser

import android.bluetooth.BluetoothGattCharacteristic
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.*

/**
 * Unit tests for gatt data reader
 */
class BluetoothGattDataReaderTest {

    /**
     * Creates a mocked BluetoothGattCharacteristic that returns the post-offset upon getNextInt.
     * Asserts that the requested format matches the specified format.
     * Asserts that the offset matches the accumulated offset of previous calls with the specified format length.
     */
    fun mockGatt(vararg sampleFormats: IntFormat): BluetoothGattCharacteristic {
        var offset = 0
        var index = 0
        return mock {
            on { getIntValue(any(kotlin.Int::class.java), any(kotlin.Int::class.java)) } doAnswer {
                var format = sampleFormats[index]
                ++index
                assertEquals(format.formatCode, it.getArgument(0))
                assertEquals(offset, it.getArgument(1))
                offset += format.lengthBytes
                offset
            }
        }
    }

    /**
     * Creates a mocked BluetoothGattCharacteristic that returns the post-offset upon getNextFloat.
     * Asserts that the requested format matches the specified format.
     * Asserts that the offset matches the accumulated offset of previous calls with the specified format length.
     */
    fun mockGatt(vararg sampleFormats: FloatFormat): BluetoothGattCharacteristic {
        var offset = 0
        var index = 0
        return mock {
            on { getFloatValue(any(kotlin.Int::class.java), any(kotlin.Int::class.java)) } doAnswer {
                var format = sampleFormats[index]
                ++index
                assertEquals(format.formatCode, it.getArgument(0))
                assertEquals(offset, it.getArgument(1))
                offset += format.lengthBytes
                offset.toFloat()
            }
        }
    }

    /**
     * Creates a mocked BluetoothGattCharacteristic for querying a string
     */
    fun mockGatt(sampleString: String) : BluetoothGattCharacteristic {
        return mock {
            on { getStringValue(any(kotlin.Int::class.java)) } doAnswer  {
                sampleString
            }
        }
    }

    @Test
    fun testNextInt() {
        val reader = BluetoothGattDataReader(mockGatt(IntFormat.FORMAT_UINT8, IntFormat.FORMAT_UINT16, IntFormat.FORMAT_SINT32))

        assertEquals(1, reader.getNextInt(IntFormat.FORMAT_UINT8))
        assertEquals(1 + 2, reader.getNextInt(IntFormat.FORMAT_UINT16))
        assertEquals(1 + 2 + 4, reader.getNextInt(IntFormat.FORMAT_SINT32))
    }

    @Test
    fun testNextFloat() {
        val reader = BluetoothGattDataReader(mockGatt(FloatFormat.FORMAT_SFLOAT, FloatFormat.FORMAT_FLOAT))

        assertEquals(2f, reader.getNextFloat(FloatFormat.FORMAT_SFLOAT), Float.MIN_VALUE)
        assertEquals(2 + 4f, reader.getNextFloat(FloatFormat.FORMAT_FLOAT), Float.MIN_VALUE)
    }

    @Test
    fun testNextString() {
        val TEST_STRING = "Super test string"
        val reader = BluetoothGattDataReader(mockGatt(TEST_STRING))
        assertEquals(TEST_STRING, reader.getNextString())
    }
}