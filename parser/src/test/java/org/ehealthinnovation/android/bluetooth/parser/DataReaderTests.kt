package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import java.util.*

/**
 * Stub for testing users of [DataReader].
 * Asserts that the order of format requests to the [DataReader] matches the provided mixed list of
 * [IntFormat], [FloatFormat] and/or [String::class.java].
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
        Assert.assertEquals(formats.poll(), String::class.java)
        return ""
    }
}

/**
 * Stub for testing users of [DataReader].
 * Provides values for [getNextInt], [getNextFloat] and [getNextString] from
 * the provided mixed list in the order specified by [testValues].
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