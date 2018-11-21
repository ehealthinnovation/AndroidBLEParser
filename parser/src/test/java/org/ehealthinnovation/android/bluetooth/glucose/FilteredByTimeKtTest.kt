package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.common.racp.SingleBoundOperation
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
        BluetoothDateTimeUtility.composeBluetoothTime(inputTIme, testDataWriter)
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

        BluetoothDateTimeUtility.composeBluetoothTime(inputTIme1, testDataWriter1)
        testDataWriter1.checkWriteComplete()

        BluetoothDateTimeUtility.composeBluetoothTime(inputTIme2, testDataWriter2)
        testDataWriter2.checkWriteComplete()

        BluetoothDateTimeUtility.composeBluetoothTime(inputTIme3, testDataWriter3)
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
            val actual = Pair(BluetoothDateTimeUtility.convertDateToBluetoothDateTime(testCase.second), testCase.second)
            assertEquals(testCase, actual)
        }
    }

    @Test
    fun smokeTestComposeFilterWithTime() {
        val testWriter = StubDataWriter(
                uint8(SingleBoundOperation.GREATER_THAN_OR_EQUAL_TO.key),
                uint8(Filter.USER_FACING_TIME.key),
                uint16(2018),
                uint8(11),
                uint8(9),
                uint8(12),
                uint8(13),
                uint8(15)
        )

        val mockFilterByTime = FilteredByBluetoothDateTime(
                BluetoothDateTimeUtility.createBluetoothDateTime(2018, 11, 9, 12, 13, 15),
                SingleBoundOperation.GREATER_THAN_OR_EQUAL_TO
        )

        RacpOperandComposer.composeTimeOperand(mockFilterByTime, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun smokeTestIsDateRangeValid() {
        val startDate = Date(8379191)
        val endDate = Date(8379192)
        assertEquals(true, BluetoothDateTimeUtility.isValidDateRange(startDate, endDate))
        assertEquals(false, BluetoothDateTimeUtility.isValidDateRange(endDate, startDate))
    }

}