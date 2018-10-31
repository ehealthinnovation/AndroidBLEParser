package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test
import java.lang.ClassCastException
import java.util.*

/**
 * Stub for testing [DataWriter].
 *
 * Verify operations by [putInt], [putFloat] and [putString] generate the same
 * the list of provided format-qualified values in the order specified by [testValues].
 *
 * Asserts that the requested format matches as well as providing the value.
 *
 * Use [StubValue] to declare the format/value pair for [putInt] and [putString]. You may also the helper functions.
 *
 * Use [StubFloatValue] to declare the format/value pair for [putFloat].
 *
 * @see [uint8], [uint16], [uint32], [sint8], [sint16], [sint32], [floate], [sfloate], [string]
 */
class StubDataWriter(vararg testValues: Any) : DataWriter {


    private val values = LinkedList<Any>()

    init {
        values.addAll(Arrays.asList(*testValues))
    }

    override fun putInt(data: Int, formatType: IntFormat) {
        val (format, value) = values.poll() as StubValue
        Assert.assertEquals(format as IntFormat, formatType)
        Assert.assertEquals(value as Int, data)
    }

    override fun putFloat(value: Float, exponent: Int, formatType: FloatFormat) {
        val expectedValue = values.poll() as StubFloatValue
        Assert.assertEquals(expectedValue.format, formatType)
        Assert.assertEquals(expectedValue.exponent, exponent)
        Assert.assertEquals(expectedValue.value, value)
    }

    override fun putString(data: String) {
        val (expectedFormat, expectedData) = values.poll() as StubValue
        Assert.assertEquals(expectedFormat as StringFormat, StringFormat.FORMAT_STRING)
        Assert.assertEquals(expectedData, data)
    }


    /**
     * Call this function when write is completed. It will check if all the expected value has been
     * written through one of the interface functions (i.e. the [values] is empty)
     */
    fun checkWriteComplete(){
        Assert.assertEquals(true, values.isEmpty())
    }
}



data class StubFloatValue(val format: FloatFormat, val value: Float, val exponent: Int)
/**
 * Creating short float or long float stub value with exponents. Used in testing with [DataWriter.putFloat]
 */
fun sfloate(value: Float, exponent: Int) = StubFloatValue(FloatFormat.FORMAT_SFLOAT, value, exponent)

fun floate(value: Float, exponent: Int) = StubFloatValue(FloatFormat.FORMAT_FLOAT, value, exponent)

class StubDataWriterTest {

    @Test
    fun testStubDataWriter() {
        val test1 = 31
        val test2 = 16.5f
        val test3 = "Hello"

        val data = StubDataWriter(uint16(test1), sfloate(test2, -1), string(test3))

        data.putInt(test1, IntFormat.FORMAT_UINT16)
        data.putFloat(test2, -1, FloatFormat.FORMAT_SFLOAT)
        data.putString(test3)
    }

    @Test
    fun testSFloate() {
        val testValue = sfloate(1.3f, -1)
        Assert.assertEquals(1.3f, testValue.value)
        Assert.assertEquals(-1, testValue.exponent)
        Assert.assertEquals(FloatFormat.FORMAT_SFLOAT, testValue.format)
    }

    @Test
    fun testFloate() {
        val testValue = floate(1.3f, -1)
        Assert.assertEquals(1.3f, testValue.value)
        Assert.assertEquals(-1, testValue.exponent)
        Assert.assertEquals(FloatFormat.FORMAT_FLOAT, testValue.format)
    }

    /**
     * Tests wrong exponent is injected for put float
     */
    @Test(expected = AssertionError::class)
    fun testBadExponent() {
        val data = StubDataWriter(sfloate(1.4f, -1))
        data.putFloat(1.4f, -2, FloatFormat.FORMAT_SFLOAT)
    }


    /**
     * Tests an int, but the wrong int type. Should assert.
     */
    @Test(expected = AssertionError::class)
    fun testBadIntFormat() {
        val data = StubDataWriter(uint16(5))
        data.putInt(5, IntFormat.FORMAT_SINT32)
    }

    /**
     * Tests an int, but the wrong int type. Should assert.
     */
    @Test(expected = Exception::class)
    fun testBadFloatFormat() {
        val data = StubDataWriter(sfloat(5f))
        data.putFloat(5f, -1, FloatFormat.FORMAT_FLOAT)
    }

    /**
     * Tests a mismatch in type. Should throw an exception (probably a bad cast)
     */
    @Test(expected = Exception::class)
    fun testBadType() {
        val data = StubDataWriter(sfloat(5f))
        data.putInt(5, IntFormat.FORMAT_UINT16)
    }

    /**
     * Tests a mismatch in type. Should throw an exception (probably a bad cast)
     */
    @Test(expected = Exception::class)
    fun testBadType2() {
        val data = StubDataWriter(sfloat(5f))
        data.putString("5f")
    }

    /**
     * Tests running past the end. Should throw an exception (probably out of bounds)
     */
    @Test(expected = Exception::class)
    fun testEndOfData() {
        val data = StubDataWriter()
        data.putInt(12, IntFormat.FORMAT_UINT16)
    }


    /**
     *
     */
    @Test(expected = AssertionError::class)
    fun testNotWritingAllExpectedDataTOBuffer(){
        val data = StubDataWriter(uint16(16), uint8(2), uint32(23))
        data.putInt(16, IntFormat.FORMAT_UINT16)
        data.putInt(2, IntFormat.FORMAT_UINT8)
        data.checkWriteComplete()
    }


}