package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.glucose.Feature
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class ParsingToolsFlagParsingTest {

    @Test
    fun testFlagAllZeros() {
        val TEST_INPUT_ALL_ZEROS = 0x00
        val EXPECTED_OUTPUT_EMPTYSET = EnumSet.noneOf(Feature::class.java)
        val output = parseFlags(TEST_INPUT_ALL_ZEROS, Feature::class.java)
        Assert.assertEquals(EXPECTED_OUTPUT_EMPTYSET, output)
    }


    @Test
    fun testFlagFirstBitSet() {
        val TEST_INPUT_ONE_BIT = 0x01
        val EXPECTED_OUTPUT_ONE_FLAG = EnumSet.of(Feature.LOW_BATTERY_DETECTION)
        val output = parseFlags(TEST_INPUT_ONE_BIT, Feature::class.java)
        Assert.assertEquals(EXPECTED_OUTPUT_ONE_FLAG, output)
    }


    @Test
    fun testFlagAllBitsSet() {
        val TEST_INPUT_ALL_BITS = 0x0FFF
        val EXPECTED_OUTPUT_ALL_FLAGS = EnumSet.allOf(Feature::class.java).minus(Feature.RESERVED_FOR_FUTURE_USE)
        val output = parseFlags(TEST_INPUT_ALL_BITS, Feature::class.java)
        Assert.assertEquals(EXPECTED_OUTPUT_ALL_FLAGS, output)

    }
}


class ParsingToolsNibbleParsingTest {

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