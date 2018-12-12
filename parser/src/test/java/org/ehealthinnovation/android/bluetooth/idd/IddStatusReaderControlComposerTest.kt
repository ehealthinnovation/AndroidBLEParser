package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
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
    fun composeSimpleCommand() {
        val testWriter = StubDataWriter(
                uint16(StatusReaderControlOpcode.GET_ACTIVE_BOLUS_IDS.key)
        )
        IddStatusReaderControlComposer().composeSimpleCommand(StatusReaderControlOpcode.GET_ACTIVE_BOLUS_IDS, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composerWhiteBoxTests() {
        val mockComposer = mock<IddStatusReaderControlComposer>()
        val mockWriter = mock<DataWriter>()
        whenever(mockComposer.compose(any(), any())).thenCallRealMethod()
        mockComposer.compose(GetActiveBolusIds(), mockWriter)
        mockComposer.compose(GetDeliveredInsulin(), mockWriter)
        mockComposer.compose(GetActiveBasalRateDelivery(), mockWriter)
        mockComposer.compose(GetTotalDailyInsulinStatus(), mockWriter)
        mockComposer.compose(GetActiveBolusDelivery(mock()), mockWriter)

        inOrder(mockComposer) {
            verify(mockComposer, times(1)).composeSimpleCommand(StatusReaderControlOpcode.GET_ACTIVE_BOLUS_IDS, mockWriter)
            verify(mockComposer, times(1)).composeSimpleCommand(StatusReaderControlOpcode.GET_DELIVERED_INSULIN, mockWriter)
            verify(mockComposer, times(1)).composeSimpleCommand(StatusReaderControlOpcode.GET_ACTIVE_BASAL_RATE_DELIVERY, mockWriter)
            verify(mockComposer, times(1)).composeSimpleCommand(StatusReaderControlOpcode.GET_TOTAL_DAILY_INSULIN_STATUS, mockWriter)
            verify(mockComposer, times(1)).composeGetActiveBolusDeliveryCommand(any(), any())
        }
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

    @Test
    fun composeGetActiveBolusDeliveryCommandTest(){
        val testWriter = StubDataWriter(uint16(StatusReaderControlOpcode.GET_ACTIVE_BOLUS_DELIVERY.key), uint16(12), uint8(BolusValueSelection.DELIVERED.key))
        val command = ActiveBolusDelivery(12, BolusValueSelection.DELIVERED)
        IddStatusReaderControlComposer().composeGetActiveBolusDeliveryCommand(command, testWriter)
        testWriter.checkWriteComplete()
    }
}