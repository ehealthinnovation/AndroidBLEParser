package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test

class BluetoothDateTimeTest {


    @Test
    fun createBluetoothDateTime() {

        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(2018, 1, 2, 3, 4, 5)
        val expectedOutput = BluetoothDateTime(2018, 1, 2, 3, 4, 5)
        Assert.assertEquals(expectedOutput, actualOutput)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidYearCauseException() {
        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(10000, 1, 2, 3, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidMonthCauseException() {
        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(2018, 13, 2, 3, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidDayCauseException() {
        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(2018, 1, 32, 3, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidHourCauseException() {
        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(2018, 1, 2, 24, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidMinuteCauseException() {
        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(2018, 1, 2, 3, 60, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidSecCauseException() {
        val actualOutput = BluetoothDataTimeUtility.createBluetoothDateTime(2018, 1, 2, 3, 4, 60)
    }
    
    @Test
    fun isYearValid() {
        BluetoothDataTimeUtility.run {
            Assert.assertEquals(true, isYearValid(2019))
            Assert.assertEquals(false, isYearValid(10000))
            Assert.assertEquals(false, isYearValid(1581))
        }
    }

    @Test
    fun isMonthValid() {
        BluetoothDataTimeUtility.run {
            Assert.assertEquals(true, isMonthValid(3))
            Assert.assertEquals(false, isMonthValid(0))
            Assert.assertEquals(false, isMonthValid(13))
        }
    }

    @Test
    fun isDayValid() {
        BluetoothDataTimeUtility.run {
            Assert.assertEquals(true, isDayValid(3))
            Assert.assertEquals(false, isDayValid(0))
            Assert.assertEquals(false, isDayValid(32))
        }
    }

    @Test
    fun isHourValid() {
        BluetoothDataTimeUtility.run {
            Assert.assertEquals(true, isHourValid(3))
            Assert.assertEquals(false, isHourValid(-1))
            Assert.assertEquals(false, isHourValid(24))
        }
    }

    @Test
    fun isMinuteValid() {

        BluetoothDataTimeUtility.run {
            Assert.assertEquals(true, isMinuteValid(3))
            Assert.assertEquals(false, isMinuteValid(-1))
            Assert.assertEquals(false, isMinuteValid(60))
        }
    }

    @Test
    fun isSecondValid() {
        BluetoothDataTimeUtility.run {
            Assert.assertEquals(true, isSecondValid(3))
            Assert.assertEquals(false, isSecondValid(-1))
            Assert.assertEquals(false, isSecondValid(60))
        }
    }
}