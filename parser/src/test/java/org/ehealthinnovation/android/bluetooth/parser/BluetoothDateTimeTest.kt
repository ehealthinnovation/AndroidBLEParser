package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test

class BluetoothDateTimeTest {


    @Test
    fun createBluetoothDateTime() {

        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(2018, 1, 2, 3, 4, 5)
        val expectedOutput = BluetoothDateTime(2018, 1, 2, 3, 4, 5)
        Assert.assertEquals(expectedOutput, actualOutput)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidYearCauseException() {
        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(10000, 1, 2, 3, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidMonthCauseException() {
        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(2018, 13, 2, 3, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidDayCauseException() {
        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(2018, 1, 32, 3, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidHourCauseException() {
        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(2018, 1, 2, 24, 4, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidMinuteCauseException() {
        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(2018, 1, 2, 3, 60, 5)
    }

    @Test(expected = Exception::class)
    fun verifyInvalidSecCauseException() {
        val actualOutput = BluetoothDateTimeUtility.createBluetoothDateTime(2018, 1, 2, 3, 4, 60)
    }
    
    @Test
    fun isYearValid() {
        BluetoothDateTimeUtility.run {
            Assert.assertEquals(true, isYearValid(2019))
            Assert.assertEquals(false, isYearValid(10000))
            Assert.assertEquals(false, isYearValid(1581))
        }
    }

    @Test
    fun isMonthValid() {
        BluetoothDateTimeUtility.run {
            Assert.assertEquals(true, isMonthValid(3))
            Assert.assertEquals(false, isMonthValid(0))
            Assert.assertEquals(false, isMonthValid(13))
        }
    }

    @Test
    fun isDayValid() {
        BluetoothDateTimeUtility.run {
            Assert.assertEquals(true, isDayValid(3))
            Assert.assertEquals(false, isDayValid(0))
            Assert.assertEquals(false, isDayValid(32))
        }
    }

    @Test
    fun isHourValid() {
        BluetoothDateTimeUtility.run {
            Assert.assertEquals(true, isHourValid(3))
            Assert.assertEquals(false, isHourValid(-1))
            Assert.assertEquals(false, isHourValid(24))
        }
    }

    @Test
    fun isMinuteValid() {

        BluetoothDateTimeUtility.run {
            Assert.assertEquals(true, isMinuteValid(3))
            Assert.assertEquals(false, isMinuteValid(-1))
            Assert.assertEquals(false, isMinuteValid(60))
        }
    }

    @Test
    fun isSecondValid() {
        BluetoothDateTimeUtility.run {
            Assert.assertEquals(true, isSecondValid(3))
            Assert.assertEquals(false, isSecondValid(-1))
            Assert.assertEquals(false, isSecondValid(60))
        }
    }
}