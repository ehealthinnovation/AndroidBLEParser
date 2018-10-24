package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class ParsingToolsTest {

    private val TEST_VALUE = 0x3A5
    private val LEAST_NIBBLE = 0x5
    private val MOST_NIBBLE = 0xA

    @Test
    fun testReadNibbles() {
        val (least, most) = readNibbles(TEST_VALUE)
        assertEquals(LEAST_NIBBLE, least)
        assertEquals(MOST_NIBBLE, most)
    }

    @Test
    fun testReadMostSignificantNibble() {
        assertEquals(MOST_NIBBLE, readMostSignificantNibble(TEST_VALUE))
    }

    @Test
    fun testReadLeastSignificantNibble() {
        assertEquals(LEAST_NIBBLE, readLeastSignificantNibble(TEST_VALUE))
    }
}