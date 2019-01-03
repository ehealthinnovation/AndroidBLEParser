package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.sfloat
import org.junit.Assert
import org.junit.Test

class BolusCalculatedTest {

    @Test
    fun readCalaculatedBoluses() {
        val testData = StubDataReader(sfloat(1f), sfloat(2f), sfloat(3f), sfloat(4f))
        val expected = BolusCalculatedData(1f, 2f, 3f, 4f)
        Assert.assertEquals(expected, readCalculatedBolus(testData))
    }
}