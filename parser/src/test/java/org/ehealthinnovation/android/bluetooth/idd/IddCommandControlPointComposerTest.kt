package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.ConfirmAnnunciationOperand
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.ProfileTemplateNumber
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SnoozeAnnunciationOperand
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
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

        val mockConfirmRequest = mock<ConfirmAnnunciation>()
        mockComposer.compose(mockConfirmRequest, mockDataWriter)

        val mockCancelTbr = mock<CancelTbrAdjustment>()
        mockComposer.compose(mockCancelTbr, mockDataWriter)

        val mockSetTbrAjustment = mock<SetTbrAdjustment>()
        mockComposer.compose(mockSetTbrAjustment, mockDataWriter)

        inOrder(mockComposer){
            verify(mockComposer, times(1)).composeSnoozeAnnunciation(mockSnoozeRequest.operand, mockDataWriter)
            verify(mockComposer, times(1)).composeConfirmAnnunciation(mockConfirmRequest.operand, mockDataWriter)
            verify(mockComposer, times(1)).composeSimpleCommand(mockCancelTbr, mockDataWriter)
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

    @Test
    fun composeConfirmAnnunciation() {
        val testWriter = StubDataWriter(uint16(Opcode.CONFIRM_ANNUNCIATION.key), uint16(1))
        val operand = ConfirmAnnunciationOperand(1)
        IddCommandControlPointComposer().composeConfirmAnnunciation(operand, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composeReadProfileTemplate() {
        val testWriter = StubDataWriter(uint16(Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE.key), uint8(2))
        val operand = ProfileTemplateNumber(2)
        IddCommandControlPointComposer().composeReadProfileTemplate(Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE, operand, testWriter)
    }
    
    @Test
    fun composeSimpleCommandTest() {
        val testWriter = StubDataWriter(uint16(Opcode.CANCEL_TBR_ADJUSTMENT.key))
        val command = CancelTbrAdjustment()
        IddCommandControlPointComposer().composeSimpleCommand(command, testWriter)
        testWriter.checkWriteComplete()
    }
}