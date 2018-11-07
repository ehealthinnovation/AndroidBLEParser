package org.ehealthinnovation.android.bluetooth.glucose

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class FilteredByTimeKtTest {

    @Test
    fun composeBluetoothDatetimeSmokeTest() {
        val testDataWriter = StubDataWriter(
                uint16(2018),
                uint8(11),
                uint8(1),
                uint8(2),
                uint8(28),
                uint8(38)
        )
        val inputTIme = BluetoothDateTime(2018, 11, 1, 2, 28, 38)
        RacpTimeComposer().composeBluetoothTime(inputTIme, testDataWriter)
        testDataWriter.checkWriteComplete()
    }

    @Test
    fun composeBluetoothDateTimeWithNullFields() {
        val testDataWriter1 = StubDataWriter(uint16(0), uint8(11), uint8(1), uint8(1), uint8(28), uint8(38))
        val inputTIme1 = BluetoothDateTime(null, 11, 1, 1, 28, 38)
        val testDataWriter2 = StubDataWriter(uint16(2019), uint8(0), uint8(1), uint8(1), uint8(28), uint8(38))
        val inputTIme2 = BluetoothDateTime(2019, 0, 1, 1, 28, 38)
        val testDataWriter3 = StubDataWriter(uint16(2019), uint8(11), uint8(0), uint8(1), uint8(28), uint8(38))
        val inputTIme3 = BluetoothDateTime(2019, 11, 0, 1, 28, 38)

        RacpTimeComposer().composeBluetoothTime(inputTIme1, testDataWriter1)
        testDataWriter1.checkWriteComplete()

        RacpTimeComposer().composeBluetoothTime(inputTIme2, testDataWriter2)
        testDataWriter2.checkWriteComplete()

        RacpTimeComposer().composeBluetoothTime(inputTIme3, testDataWriter3)
        testDataWriter3.checkWriteComplete()
    }

    @Test
    fun smokeTestConvertDateToBluetoothDateTime() {
        //the pair format (expect output BluetoothDateTime, input Date)
        val testCases = listOf<Pair<BluetoothDateTime, Date>>(
                Pair(BluetoothDateTime(2018, 11, 1, 20, 52, 21),
                        GregorianCalendar(2018, 10, 1, 20, 52, 21).time),
                Pair(BluetoothDateTime(2018, 1, 1, 0, 0, 0),
                        GregorianCalendar(2018, 0, 1, 0, 0, 0).time),
                Pair(BluetoothDateTime(4000, 12, 31, 23, 59, 59),
                        GregorianCalendar(4000, 11, 31, 23, 59, 59).time)
        )

        for (testCase in testCases) {
            val actual = Pair(BluetoothDataTimeUtility.convertDateToBluetoothDateTime(testCase.second), testCase.second)
            assertEquals(testCase, actual)
        }
    }

    @Test
    fun smokeTestComposeFilterWithTime() {
        val testWriter = StubDataWriter(
                uint8(GlucoseOperatorBound.GREATER_THAN_OR_EQUAL_TO.key),
                uint8(Filter.USER_FACING_TIME.key)
        )
        val mockFilterByTime = mock<RacpTimeComposer>()
        whenever(mockFilterByTime.composeTimeOperand(any(), any())).thenCallRealMethod()


        val mockOperand = mock<FilteredByBluetoothDateTime>()
        val mockDate = mock<BluetoothDateTime>()
        whenever(mockOperand.operation).thenReturn(GlucoseOperatorBound.GREATER_THAN_OR_EQUAL_TO)
        whenever(mockOperand.date).thenReturn(mockDate)

        mockFilterByTime.composeTimeOperand(mockOperand, testWriter)

        verify(mockFilterByTime).composeBluetoothTime(mockDate, testWriter)
    }

    @Test
    fun smokeTestIsDateRangeValid() {
        val startDate = Date(8379191)
        val endDate = Date(8379192)
        assertEquals(true, BluetoothDataTimeUtility.isValidDateRange(startDate, endDate))
        assertEquals(false, BluetoothDataTimeUtility.isValidDateRange(endDate, startDate))
    }

}