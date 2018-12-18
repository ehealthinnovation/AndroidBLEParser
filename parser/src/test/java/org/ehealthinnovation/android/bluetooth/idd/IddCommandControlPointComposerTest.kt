package org.ehealthinnovation.android.bluetooth.idd

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.*
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Test

class IddCommandControlPointComposerTest {

    @Test
    fun composeWhiteBoxTest() {
        val mockComposer = mock<IddCommandControlPointComposer>()
        val mockDataWriter = mock<DataWriter>()

        whenever(mockComposer.compose(any(), any())).thenCallRealMethod()

        val mockSnoozeRequest = mock<SnoozeAnnunciation>()
        mockComposer.compose(mockSnoozeRequest, mockDataWriter)

        val mockWriteBasalRateProfileTemplate = mock<WriteBasalRateProfileTemplate>()
        mockComposer.compose(mockWriteBasalRateProfileTemplate, mockDataWriter)

        val mockWriteIsfProfileTemplate = mock<WriteIsfProfileTemplate>()
        mockComposer.compose(mockWriteIsfProfileTemplate, mockDataWriter)

        val mockWriteI2CHORatioProfileTemplate = mock<WriteI2CHORatioProfileTemplate>()
        mockComposer.compose(mockWriteI2CHORatioProfileTemplate, mockDataWriter)

        val mockConfirmRequest = mock<ConfirmAnnunciation>()
        mockComposer.compose(mockConfirmRequest, mockDataWriter)

        val mockCancelTbr = mock<CancelTbrAdjustment>()
        mockComposer.compose(mockCancelTbr, mockDataWriter)

        val mockSetTbrAjustment = mock<SetTbrAdjustment>()
        mockComposer.compose(mockSetTbrAjustment, mockDataWriter)

        val mockGetAvailableBolus = mock<GetAvailableBolus>()
        mockComposer.compose(mockGetAvailableBolus, mockDataWriter)

        val mockCancelBolus = mock<CancelBolus>()
        mockComposer.compose(mockCancelBolus, mockDataWriter)

        val mockSetBolus = mock<SetBolus>()
        mockComposer.compose(mockSetBolus, mockDataWriter)


        inOrder(mockComposer) {
            verify(mockComposer, times(1)).composeSnoozeAnnunciation(mockSnoozeRequest.operand, mockDataWriter)
            verify(mockComposer, times(1)).composeWriteProfileTemplate(mockWriteBasalRateProfileTemplate, mockDataWriter)
            verify(mockComposer, times(1)).composeWriteProfileTemplate(mockWriteIsfProfileTemplate, mockDataWriter)
            verify(mockComposer, times(1)).composeWriteProfileTemplate(mockWriteI2CHORatioProfileTemplate, mockDataWriter)
            verify(mockComposer, times(1)).composeConfirmAnnunciation(mockConfirmRequest.operand, mockDataWriter)
            verify(mockComposer, times(1)).composeSimpleCommand(mockCancelTbr, mockDataWriter)
            verify(mockComposer, times(1)).composeSetTbrAdjustment(mockSetTbrAjustment.operand, mockDataWriter)
            verify(mockComposer, times(1)).composeSimpleCommand(mockGetAvailableBolus, mockDataWriter)
            verify(mockComposer, times(1)).composeCancelBolus(mockCancelBolus, mockDataWriter)
            verify(mockComposer, times(1)).composeSetBolus(mockSetBolus, mockDataWriter)
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
    fun composeWriteBasalRateProfileTemplateTest() {
        val testWriter = StubDataWriter(
                uint16(Opcode.WRITE_BASAL_RATE_PROFILE_TEMPLATE.key),
                uint8(0b0011), uint8(1), uint8(2), uint16(3), sfloate(4.0f, -1), uint16(4), sfloate(5.0f, -1)
        )
        val request = WriteBasalRateProfileTemplate(WriteProfileTemplateOperand(true, 1, 2, SingleValueTimeBlock(3, 4.0f), SingleValueTimeBlock(4, 5.0f)))
        IddCommandControlPointComposer().composeWriteProfileTemplate(request, testWriter)
        testWriter.checkWriteComplete()

    }

    @Test
    fun composeWriteIsfProfileTemplateTest() {
        val testWriter = StubDataWriter(
                uint16(Opcode.WRITE_ISF_PROFILE_TEMPLATE.key),
                uint8(0b0010), uint8(1), uint8(2), uint16(3), sfloate(4.0f, -1), uint16(4), sfloate(5.0f, -1)
        )
        val request = WriteIsfProfileTemplate(WriteProfileTemplateOperand(false, 1, 2, SingleValueTimeBlock(3, 4.0f), SingleValueTimeBlock(4, 5.0f)))
        IddCommandControlPointComposer().composeWriteProfileTemplate(request, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composeWriteI2CHORatioProfileTemplateTest() {
        val testWriter = StubDataWriter(
                uint16(Opcode.WRITE_I2CHO_RATIO_PROFILE_TEMPLATE.key),
                uint8(0b0111), uint8(1), uint8(2), uint16(3), sfloate(4.0f, -1), uint16(4), sfloate(5.0f, -1), uint16(5), sfloate(6.0f, -1)
        )
        val request = WriteI2CHORatioProfileTemplate(WriteProfileTemplateOperand(true, 1, 2, SingleValueTimeBlock(3, 4.0f), SingleValueTimeBlock(4, 5.0f), SingleValueTimeBlock(5, 6.0f)))
        IddCommandControlPointComposer().composeWriteProfileTemplate(request, testWriter)
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

    @Test
    fun composeCancelBolusTest() {
        val testWriter = StubDataWriter(uint16(Opcode.CANCEL_BOLUS.key), uint16(2))
        val command = CancelBolus(BolusId(2))
        IddCommandControlPointComposer().composeCancelBolus(command, testWriter)
        testWriter.checkWriteComplete()
    }
}