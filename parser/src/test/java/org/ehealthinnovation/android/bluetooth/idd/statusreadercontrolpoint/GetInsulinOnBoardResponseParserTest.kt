package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.InsulinOnBoardResponse
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class GetInsulinOnBoardResponseParserTest {

    @Test
    fun parseResponse() {
        val testDataReaderNoRemainingDuation = StubDataReader(
                uint8(0), sfloat(1.2f)
        )
        val expectedResult1 = InsulinOnBoardResponse(1.2f, null)
        Assert.assertEquals(expectedResult1, GetInsulinOnBoardResponseParser().parseResponse(testDataReaderNoRemainingDuation))

        val testDataReaderWithRemainingDuration = StubDataReader(
                uint8(1), sfloat(1.2f), uint16(3)
        )
        val expectedResult2 = InsulinOnBoardResponse(1.2f, 3)
        Assert.assertEquals(expectedResult2, GetInsulinOnBoardResponseParser().parseResponse(testDataReaderWithRemainingDuration))

    }
}