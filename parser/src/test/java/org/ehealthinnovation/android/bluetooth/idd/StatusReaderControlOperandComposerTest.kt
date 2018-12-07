package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

import org.junit.Assert.*

class StatusReaderControlOperandComposerTest {

    @Test
    fun composeGetCounter() {
        val testWriter = StubDataWriter(uint8(CounterType.IDD_LIFETIME.key), uint8(CounterValueSelection.REMAINING.key))
        val testOperandInput = GetCounterOperand(CounterType.IDD_LIFETIME, CounterValueSelection.REMAINING)
        StatusReaderControlOperandComposer().composeGetCounter(testOperandInput, testWriter)
        testWriter.checkWriteComplete()
    }
}