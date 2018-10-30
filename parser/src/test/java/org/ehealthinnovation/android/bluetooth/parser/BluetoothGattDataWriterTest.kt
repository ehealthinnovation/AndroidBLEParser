package org.ehealthinnovation.android.bluetooth.parser

import android.bluetooth.BluetoothGattCharacteristic
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.mockito.Mockito.any

class BluetoothGattDataWriterTest {
    /**
     * creates a mocked bluetoothgattcharacteristic that returns the post-offset result upon setvalue
     * asserts that the requested format matches the specified format.
     * asserts that the offset matches the accumulated offset of previous calls with the specified format length.
     */
    fun mockGatt(vararg formatResults: Pair<IntFormat, Boolean>): BluetoothGattCharacteristic {
        var offset = 0
        var index = 0

        return mock {
            on { setValue(any(Int::class.java), any(Int::class.java), any(Int::class.java)) } doAnswer {
                var format = formatResults[index].first
                var result = formatResults[index].second
                assertEquals(format.formatCode, it.getArgument(1))
                assertEquals(offset, it.getArgument(2))
                offset += format.lengthBytes
                ++index
                result
            }
        }
    }

    /**
     * creates a mocked bluetoothgattcharacteristic that returns the post-offset result upon setvalue
     * asserts that the requested format matches the specified format.
     * asserts that the offset matches the accumulated offset of previous calls with the specified format length.
     */
    fun mockFloatGatt(vararg formatResults: Pair<FloatFormat, Boolean>): BluetoothGattCharacteristic {
        var offset = 0
        var index = 0

        return mock {
            on { setValue(any(Int::class.java), any(Int::class.java), any(Int::class.java), any(Int::class.java)) } doAnswer {
                var format = formatResults[index].first
                var result = formatResults[index].second
                assertEquals(format.formatCode, it.getArgument(2))
                assertEquals(offset, it.getArgument(3))
                offset += format.lengthBytes
                ++index
                result
            }
        }
    }

    /**
     * creates a mocked bluetoothgattcharacteristic that returns the post-offset result upon setvalue
     */
    fun mockStringGatt(output: Boolean): BluetoothGattCharacteristic {
        var offset = 0
        return mock {
            on { setValue(any(String::class.java)) } doAnswer {
                output
            }
        }
    }

    @Test
    fun putInt() {
        val mockGatt = mockGatt(Pair(IntFormat.FORMAT_UINT8, true),
                Pair(IntFormat.FORMAT_UINT16, true),
                Pair(IntFormat.FORMAT_UINT32, true),
                Pair(IntFormat.FORMAT_SINT8, true),
                Pair(IntFormat.FORMAT_SINT16, true),
                Pair(IntFormat.FORMAT_SINT32, true)
        )
        val writer = BluetoothGattDataWriter(mockGatt)

        writer.putInt(0x12, IntFormat.FORMAT_UINT8)
        assertEquals(1, writer.offset)
        verify(mockGatt).setValue(0x12, IntFormat.FORMAT_UINT8.formatCode, 0)

        writer.putInt(0x34, IntFormat.FORMAT_UINT16)
        assertEquals(1 + 2, writer.offset)
        verify(mockGatt).setValue(0x34, IntFormat.FORMAT_UINT16.formatCode, 1)

        writer.putInt(0x56, IntFormat.FORMAT_UINT32)
        assertEquals(1 + 2 + 4, writer.offset)
        verify(mockGatt).setValue(0x56, IntFormat.FORMAT_UINT32.formatCode, 3)

        writer.putInt(-1, IntFormat.FORMAT_SINT8)
        assertEquals(1 + 2 + 4 + 1, writer.offset)
        verify(mockGatt).setValue(-1, IntFormat.FORMAT_SINT8.formatCode, 1 + 2 + 4)

        writer.putInt(-3, IntFormat.FORMAT_SINT16)
        assertEquals(1 + 2 + 4 + 1 + 2, writer.offset)
        verify(mockGatt).setValue(-3, IntFormat.FORMAT_SINT16.formatCode, 1 + 2 + 4 + 1)

        writer.putInt(-5, IntFormat.FORMAT_SINT32)
        assertEquals(1 + 2 + 4 + 1 + 2 + 4, writer.offset)
        verify(mockGatt).setValue(-5, IntFormat.FORMAT_SINT32.formatCode, 1 + 2 + 4 + 1 + 2)
    }

    /**
     * Negative case when calling the setValue function returns false
     */
    @Test(expected = RuntimeException::class)
    fun testPutIntFailedCase() {
        val mockGatt = mockGatt(Pair(IntFormat.FORMAT_UINT8, false))
        val writer = BluetoothGattDataWriter(mockGatt)

        writer.putInt(0x12, IntFormat.FORMAT_UINT8)
        verify(mockGatt).setValue(0x12, IntFormat.FORMAT_SINT32.formatCode, 0)
    }

    @Test
    fun putFloat() {
        val mockGatt = mockFloatGatt(Pair(FloatFormat.FORMAT_SFLOAT, true), Pair(FloatFormat.FORMAT_FLOAT, true))

        val writer = BluetoothGattDataWriter(mockGatt)

        writer.putFloat(1.2f, -1, FloatFormat.FORMAT_SFLOAT)
        assertEquals(2, writer.offset)
        verify(mockGatt).setValue(12, -1, FloatFormat.FORMAT_SFLOAT.formatCode, 0)

        writer.putFloat(1.2f, -2, FloatFormat.FORMAT_FLOAT)
        assertEquals(2 + 4, writer.offset)
        verify(mockGatt).setValue(120, -2, FloatFormat.FORMAT_FLOAT.formatCode, 2)
    }

    /**
     * Negative case when calling the setValue function returns false
     */
    @Test(expected = RuntimeException::class)
    fun testPutFloatFailedCase() {
        val mockGatt = mockFloatGatt(Pair(FloatFormat.FORMAT_SFLOAT, false))
        val writer = BluetoothGattDataWriter(mockGatt)
        writer.putFloat(1.2f, -1, FloatFormat.FORMAT_SFLOAT)
        verify(mockGatt).setValue(12, -1, FloatFormat.FORMAT_SFLOAT.formatCode, 0)
    }


    @Test
    fun putString() {
        val mockGatt = mockStringGatt(true)
        val writer = BluetoothGattDataWriter(mockGatt)

        writer.putString("JKSLW tested")
        assertEquals(13, writer.offset)
        verify(mockGatt).setValue("JKSLW tested")
    }

    /**
     * Negative case when calling the setValue function returns false
     */
    @Test(expected = RuntimeException::class)
    fun testPutStringFailedCase() {
        val mockGatt = mockStringGatt(false)
        val writer = BluetoothGattDataWriter(
                mockGatt
        )

        writer.putString("I never get written")
        verify(mockGatt).setValue("I never get written")
    }

    @Test
    fun findMantissa() {
        //Test cases are tripple of the following format (value in float, exponent, expected output of the function)
        val testCases: List<Triple<Float, Int, Int>> = listOf(
                Triple(5.3f, -1, 53),
                Triple(5.3f, -2, 530),
                Triple(5.3f, 0, 5),
                Triple(5.3f, 1, 0)
        )

        for (testcase in testCases) {
            assertEquals(testcase.third, findMantissa(testcase.first, testcase.second))
        }

    }


}