package org.ehealthinnovation.android.bluetooth.parser

import android.bluetooth.BluetoothGattCharacteristic
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt

/**
 * Unit tests for gatt data reader
 */
class BluetoothGattDataReaderTest {

    /**
     * Creates a mocked BluetoothGattCharacteristic that returns the post-offset upon getNextInt.
     * Asserts that the requested format matches the specified format.
     * Asserts that the offset matches the accumulated offset of previous calls with the specified format length.
     */
    private fun mockGatt(vararg sampleFormats: IntFormat): BluetoothGattCharacteristic {
        var offset = 0
        var index = 0
        return mock {
            on { getIntValue(any(kotlin.Int::class.java), any(kotlin.Int::class.java)) } doAnswer {
                val format = sampleFormats[index]
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
    private fun mockGatt(vararg sampleFormats: FloatFormat): BluetoothGattCharacteristic {
        var offset = 0
        var index = 0
        return mock {
            on { getFloatValue(any(kotlin.Int::class.java), any(kotlin.Int::class.java)) } doAnswer {
                val format = sampleFormats[index]
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
    private fun mockGatt(sampleString: String): BluetoothGattCharacteristic {
        return mock {
            on { getStringValue(any(kotlin.Int::class.java)) } doAnswer {
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
    //This is a special case to read UINT24. It is implemented with three calls to the getInt(UINT8, offset)
    fun testNextIntOfU24() {
        var offset = 0
        val returnValueList = listOf(1, 2, 3)
        val expectedOutput = 0x030201


        val mockGattForU24 = mock<BluetoothGattCharacteristic>()
        whenever(mockGattForU24.getIntValue(eq(IntFormat.FORMAT_UINT8.formatCode), anyInt())).doAnswer {
            val output = returnValueList[offset]
            offset++
            output
        }

        val gattDataReader = BluetoothGattDataReader(mockGattForU24)
        val result = gattDataReader.getNextInt(IntFormat.FORMAT_UINT24)


        Assert.assertEquals(3, offset)
        Assert.assertEquals(expectedOutput, result)

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