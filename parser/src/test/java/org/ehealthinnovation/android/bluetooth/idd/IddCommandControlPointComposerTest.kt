package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationOperand
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.junit.Test

import org.junit.Assert.*

class IddCommandControlPointComposerTest {

    @Test
    fun composeWhiteBoxTest() {
        val mockComposer = mock<IddCommandControlPointComposer>()
        val mockDataWriter = mock<DataWriter>()
        val mockSnoozeRequest = mock<SnoozeAnnunciation>()

        whenever(mockComposer.compose(any(), any())).thenCallRealMethod()

        mockComposer.compose(mockSnoozeRequest, mockDataWriter)

        val mockSetTbrAjustment = mock<SetTbrAdjustment>()
        mockComposer.compose(mockSetTbrAjustment, mockDataWriter)

        inOrder(mockComposer){
            verify(mockComposer, times(1)).composeSnoozeAnnunciation(mockSnoozeRequest.operand, mockDataWriter)
            verify(mockComposer, times(1)).composeSetTbrAdjustment(mockSetTbrAjustment.operand, mockDataWriter)
        }
    }

    @Test
    fun composeSnoozeAnnunciation() {
        val testWriter = StubDataWriter(uint16(Opcode.SNOOZE_ANNUNCIATION.key), uint16(1))
        val operand = SnoozeAnnunciationOperand(1)
        IddCommandControlPointComposer().composeSnoozeAnnunciation(operand, testWriter)
        testWriter.checkWriteComplete()
    }
}