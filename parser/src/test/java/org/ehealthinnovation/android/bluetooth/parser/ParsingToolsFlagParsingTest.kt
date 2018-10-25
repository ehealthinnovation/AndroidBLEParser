package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.glucose.Feature
import org.junit.Assert
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


