package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test
import java.util.*

class IddStatusReaderControlComposerTest {

    @Test
    fun composeResetStatusCommand() {
        val testWriter1 = StubDataWriter(
                uint16(StatusReaderControlOpcode.RESET_STATUS.key),
                uint16(0x0011)
        )
        val inputCommandSomeStatusFlag = ResetStatus(StatusFlagToReset(EnumSet.of(Status.THERAPY_CONTROL_STATE_CHANGED, Status.TOTAL_DAILY_INSULIN_STATUS_CHANGED)))
        IddStatusReaderControlComposer().compose(inputCommandSomeStatusFlag, testWriter1)
        testWriter1.checkWriteComplete()

        val testWriter2 = StubDataWriter(
                uint16(StatusReaderControlOpcode.RESET_STATUS.key),
                uint16(0x0)
        )
        val inputCommandNoStatusFlag = ResetStatus(StatusFlagToReset(EnumSet.noneOf(Status::class.java)))
        IddStatusReaderControlComposer().compose(inputCommandNoStatusFlag, testWriter2)
        testWriter2.checkWriteComplete()

        val testWriter3 = StubDataWriter(
                uint16(StatusReaderControlOpcode.RESET_STATUS.key),
                uint16(0x0000FF)
        )
        val inputCommandAllStatusFlag = ResetStatus(StatusFlagToReset(EnumSet.allOf(Status::class.java)))
        IddStatusReaderControlComposer().compose(inputCommandAllStatusFlag, testWriter3)
        testWriter3.checkWriteComplete()
    }

     @Test
    fun composeGetCounterCommand() {
        val testWriter1 = StubDataWriter(
                uint16(StatusReaderControlOpcode.GET_COUNTER.key),
                uint8(CounterType.IDD_LIFETIME.key),
                uint8(CounterValueSelection.REMAINING.key)
        )
        val inputCommand = GetCounter(GetCounterOperand(CounterType.IDD_LIFETIME, CounterValueSelection.REMAINING))
        IddStatusReaderControlComposer().compose(inputCommand, testWriter1)
        testWriter1.checkWriteComplete()
    }
}