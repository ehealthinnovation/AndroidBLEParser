package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.sfloate
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class WriteProfileTemplateOperandComposerTest {

    @Test
    fun composeOperandTest() {
        val operand1 = WriteProfileTemplateOperand(true, 1, 2, SingleValueTimeBlock(3,4f))
        val expectFlags1 = EnumSet.of(WriteProfileTemplateOperandComposer.Flag.END_TRANSACTION)
        val testWriter1 = StubDataWriter(uint8(1), uint8(1), uint8(2), uint16(3), sfloate(4f,-1))
        WriteProfileTemplateOperandComposer().composeOperand(operand1, testWriter1)
        testWriter1.checkWriteComplete()
    }

    @Test
    fun getFlagsTest(){
        val operand1 = WriteProfileTemplateOperand(true, 1, 1, SingleValueTimeBlock(1,1f))
        val expectFlags1 = EnumSet.of(WriteProfileTemplateOperandComposer.Flag.END_TRANSACTION)
        Assert.assertEquals(expectFlags1, WriteProfileTemplateOperandComposer().getFlags(operand1))

        val operand2 = WriteProfileTemplateOperand(true, 1, 1, SingleValueTimeBlock(1,1f), SingleValueTimeBlock(1,2f))
        val expectFlags2 = EnumSet.of(WriteProfileTemplateOperandComposer.Flag.END_TRANSACTION, WriteProfileTemplateOperandComposer.Flag.SECOND_TIME_BLOCK_PRESENT)
        Assert.assertEquals(expectFlags2, WriteProfileTemplateOperandComposer().getFlags(operand2))

        val operand3 = WriteProfileTemplateOperand(true, 1, 1, SingleValueTimeBlock(1,1f), SingleValueTimeBlock(1,2f),  SingleValueTimeBlock(1,3f))
        val expectFlags3 = EnumSet.allOf(WriteProfileTemplateOperandComposer.Flag::class.java)
        Assert.assertEquals(expectFlags3, WriteProfileTemplateOperandComposer().getFlags(operand3))
    }

    @Test
    fun writeTimeBlockTest() {
        val testWriter = StubDataWriter(uint16(1), sfloate(2.3f, -1))
        val timeblockToWrite = SingleValueTimeBlock(1, 2.3f)
        WriteProfileTemplateOperandComposer().writeTimeBlock(timeblockToWrite, testWriter)
    }

}