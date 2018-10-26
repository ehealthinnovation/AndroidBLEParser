package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Stub for testing users of [DataReader].
 * Asserts that the order of format requests to the [DataReader] matches the provided mixed list of
 * [IntFormat], [FloatFormat] and/or [StringFormat].
 */
class StubFormatReader(vararg testFormats: Any) : DataReader {
    private val formats = LinkedList<Any>()

    init {
        formats.addAll(Arrays.asList(*testFormats))
    }

    override fun getNextInt(formatType: IntFormat): Int {
        Assert.assertEquals(formats.poll() as IntFormat, formatType)
        return 0
    }

    override fun getNextFloat(formatType: FloatFormat): Float {
        Assert.assertEquals(formats.poll() as FloatFormat, formatType)
        return 0f
    }

    override fun getNextString(): String {
        Assert.assertEquals(formats.poll() as StringFormat, StringFormat.FORMAT_STRING)
        return ""
    }
}

/**
 * Stub for testing users of [DataReader].
 * Provides values for [getNextInt], [getNextFloat] and [getNextString] from
 * the provided mixed list in the order specified by [testValues].
 *
 * Ignores the format request. To test the format, see [StubFormatReader] or
 * [StubDataReader]
 */
class StubValueReader(vararg testValues: Any) : DataReader {

    private val values = LinkedList<Any>()

    init {
        values.addAll(Arrays.asList(*testValues))
    }

    override fun getNextInt(formatType: IntFormat): Int {
        return values.poll() as Int
    }

    override fun getNextFloat(formatType: FloatFormat): Float {
        return values.poll() as Float
    }

    override fun getNextString(): String {
        return values.poll() as String
    }
}

/**
 * Stub for testing [DataReader].
 *
 * Provides values for [getNextInt], [getNextFloat] and [getNextString] from
 * the list of provided format-qualified values in the order specified by [testValues].
 * Asserts that the requested format matches as well as providing the value.
 *
 * Use [StubValue] to declare the format/value pair. You may also the helper functions.
 *
 * @see [uint8], [uint16], [uint32], [sint8], [sint16], [sint32], [float], [sfloat], [string]
 */
class StubDataReader(vararg testValues: StubValue) : DataReader {
    private val values = LinkedList<StubValue>()
    init {
        values.addAll(Arrays.asList(*testValues))
    }

    override fun getNextInt(formatType: IntFormat): Int {
        val (format, value) = values.poll()
        Assert.assertEquals(format as IntFormat, formatType)
        return value as Int
    }

    override fun getNextFloat(formatType: FloatFormat): Float {
        val (format, value) = values.poll()
        Assert.assertEquals(format as FloatFormat, formatType)
        return value as Float
    }

    override fun getNextString(): String {
        val (format, value) = values.poll()
        Assert.assertEquals(format as StringFormat, StringFormat.FORMAT_STRING)
        return value as String
    }
}

/**
 * A typed value for the [StubDataReader]
 */
data class StubValue(val format: Any, val value: Any)

/**
 * Stub format class for string classes in [DataReader]
 */
enum class StringFormat {
    FORMAT_STRING
}

// Helper functions for declaring [StubValue] format/value pairs for [StubDataReader]

fun uint8(value:Int) = StubValue(IntFormat.FORMAT_UINT8, value and 0xFF)
fun uint8(low:Int, high:Int) = StubValue(IntFormat.FORMAT_UINT8, ((low and 0xF) or ((high and 0xF) shl 4)))
fun uint16(value:Int) = StubValue(IntFormat.FORMAT_UINT16, value and 0xFFFF)
fun uint32(value:Int) = StubValue(IntFormat.FORMAT_UINT32, value)
fun sint8(value:Int) = StubValue(IntFormat.FORMAT_SINT8, value)
fun sint16(value:Int) = StubValue(IntFormat.FORMAT_SINT16, value)
fun sint32(value:Int) = StubValue(IntFormat.FORMAT_SINT32, value)
fun float(value:Float) = StubValue(FloatFormat.FORMAT_FLOAT, value)
fun sfloat(value:Float) = StubValue(FloatFormat.FORMAT_SFLOAT, value)
fun string(value:String) = StubValue(StringFormat.FORMAT_STRING, value)


class StubDataTest {

    @Test
    fun testStubDataReader() {
        val TEST1 = 31
        val TEST2 = 16.5f
        val TEST3 = "Hello"

        val data = StubDataReader(uint16(TEST1), sfloat(TEST2), string(TEST3))

        Assert.assertEquals(TEST1, data.getNextInt(IntFormat.FORMAT_UINT16))
        Assert.assertEquals(TEST2, data.getNextFloat(FloatFormat.FORMAT_SFLOAT))
        Assert.assertEquals(TEST3, data.getNextString())
    }

    @Test
    fun testUints() {
        Assert.assertEquals(0x16, uint8(0x216).value) // only keeps lower 8 bits
        Assert.assertEquals(0x1613, uint16(0x41613).value) // only keeps lower 16 bits
        Assert.assertEquals(0x123456, uint32(0x123456).value)
    }

    @Test
    fun testMakeNibble() {
        val LOW = 0xE
        val HIGH = 0x3
        val TOTAL = 0x3E

        Assert.assertEquals(TOTAL, uint8(LOW, HIGH).value)
    }

    /**
     * Tests an int, but the wrong int type. Should assert.
     */
    @Test(expected = AssertionError::class)
    fun testBadIntFormat() {
        val data = StubDataReader(uint16(5))
        data.getNextInt(IntFormat.FORMAT_SINT32)
    }

    /**
     * Tests an int, but the wrong int type. Should assert.
     */
    @Test(expected = AssertionError::class)
    fun testBadFloatFormat() {
        val data = StubDataReader(sfloat(5f))
        data.getNextFloat(FloatFormat.FORMAT_FLOAT)
    }

    /**
     * Tests a mismatch in type. Should throw an exception (probably a bad cast)
     */
    @Test(expected = Exception::class)
    fun testBadType() {
        val data = StubDataReader(sfloat(5f))
        data.getNextInt(IntFormat.FORMAT_UINT16)
    }

    /**
     * Tests a mismatch in type. Should throw an exception (probably a bad cast)
     */
    @Test(expected = Exception::class)
    fun testBadType2() {
        val data = StubDataReader(sfloat(5f))
        data.getNextString()
    }

    /**
     * Tests running past the end. Should throw an exception (probably out of bounds)
     */
    @Test(expected = Exception::class)
    fun testEndOfData() {
        val data = StubDataReader()
        data.getNextInt(IntFormat.FORMAT_UINT16)
    }
}