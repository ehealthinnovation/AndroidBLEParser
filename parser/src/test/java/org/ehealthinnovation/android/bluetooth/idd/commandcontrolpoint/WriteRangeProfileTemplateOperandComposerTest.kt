package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.sfloate
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test
import java.util.*

class WriteRangeProfileTemplateOperandComposerTest {

    @Test
    fun composeOperandTest() {
        val operand1 = WriteRangeProfileTemplateOperand(true, 1, 2, RangedTimeBlock(3, 4f, 5f))
        val expectFlags1 = EnumSet.of(WriteProfileTemplateOperandComposer.Flag.END_TRANSACTION)
        val testWriter1 = StubDataWriter(uint8(1), uint8(1), uint8(2), uint16(3), sfloate(4f, -1), sfloate(5f, -1))
        WriteRangeProfileTemplateOperandComposer().composeOperand(operand1, testWriter1)
        testWriter1.checkWriteComplete()

    }

    @Test
    fun getFlagsTest() {
        val operand1 = WriteRangeProfileTemplateOperand(true, 1, 1, RangedTimeBlock(1, 1f, 1.1f))
        val expectFlags1 = EnumSet.of(WriteRangeProfileTemplateOperandComposer.Flag.END_TRANSACTION)
        Assert.assertEquals(expectFlags1, WriteRangeProfileTemplateOperandComposer().getFlags(operand1))

        val operand2 = WriteRangeProfileTemplateOperand(true, 1, 1, RangedTimeBlock(1, 1f, 1.1f), RangedTimeBlock(1, 2f, 2.2f))
        val expectFlags2 = EnumSet.of(WriteRangeProfileTemplateOperandComposer.Flag.END_TRANSACTION, WriteRangeProfileTemplateOperandComposer.Flag.SECOND_TIME_BLOCK_PRESENT)
        Assert.assertEquals(expectFlags2, WriteRangeProfileTemplateOperandComposer().getFlags(operand2))
    }

    @Test
    fun writeTimeBlockTest() {
        val testWriter = StubDataWriter(uint16(1), sfloate(2.3f, -1), sfloate(4.5f, -1))
        val timeblockToWrite = RangedTimeBlock(1, 2.3f, 4.5f)
        WriteRangeProfileTemplateOperandComposer().writeTimeBlock(timeblockToWrite, testWriter)
    }
}