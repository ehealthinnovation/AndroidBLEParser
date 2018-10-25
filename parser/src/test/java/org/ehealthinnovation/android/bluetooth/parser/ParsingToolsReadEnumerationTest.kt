package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.glucose.SampleLocation
import org.junit.Test

import org.junit.Assert.*

class ParsingToolsReadEnumerationTest {

    @Test
    fun readEnumerationWithDefault() {
        val TEST_ENUM = SampleLocation.ALTERNATE_SITE_TEST
        val TEST_INT = TEST_ENUM.key

        assertEquals(TEST_ENUM, readEnumeration(TEST_INT, SampleLocation::class.java, SampleLocation.RESERVED_FOR_FUTURE))
        assertEquals(SampleLocation.RESERVED_FOR_FUTURE, readEnumeration(375, SampleLocation::class.java, SampleLocation.RESERVED_FOR_FUTURE))
    }

    @Test
    fun readEnumerationNoDefault() {
        val TEST_ENUM = SampleLocation.EARLOBE
        val TEST_INT = TEST_ENUM.key

        assertEquals(TEST_ENUM, readEnumeration(TEST_INT, SampleLocation::class.java))
    }

    @Test(expected = Exception::class)
    fun readEnumerationNoDefault_HasException() {
        readEnumeration(675, SampleLocation::class.java)
    }
}