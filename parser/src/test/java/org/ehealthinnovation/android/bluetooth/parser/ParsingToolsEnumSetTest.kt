package org.ehealthinnovation.android.bluetooth.parser

import org.ehealthinnovation.android.bluetooth.cgm.StatusFlag
import org.junit.Assert
import org.junit.Test
import java.util.*

class ParsingToolsEnumSetTest {

    @Test
    fun combiningTwoEnumSets() {
        val setA = EnumSet.of(StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL)
        val setB = EnumSet.of(StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL)
        val expectedCombinedSet = EnumSet.of(
                StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL,
                StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL
        )
        Assert.assertEquals(expectedCombinedSet, combineEnumSet(setA, setB))
    }

    @Test
    fun combiningTwoEnumSetsWithOverlappingElements() {
        val setA = EnumSet.of(StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL, StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED)
        val setB = EnumSet.of(StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL, StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED)
        val expectedCombinedSet = EnumSet.of(
                StatusFlag.RESULT_UNDER_PATIENT_LOW_LEVEL,
                StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL,
                StatusFlag.TIME_SYNCHRONIZATION_BETWEEN_SENSOR_AND_COLLECTOR_REQUIRED
        )
        Assert.assertEquals(expectedCombinedSet, combineEnumSet(setA, setB))
    }


    @Test
    fun combiningOneEnumSetsWithEmptySet() {
        val setA = EnumSet.noneOf(StatusFlag::class.java)
        val setB = EnumSet.of(StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL)
        val expectedCombinedSet = EnumSet.of(
                StatusFlag.RESULT_ABOVE_PATIENT_HIGH_LEVEL
        )
        Assert.assertEquals(expectedCombinedSet, combineEnumSet(setA, setB))
    }
}
