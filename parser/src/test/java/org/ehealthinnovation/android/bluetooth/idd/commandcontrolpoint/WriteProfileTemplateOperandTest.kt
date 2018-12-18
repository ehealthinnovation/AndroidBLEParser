package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.junit.Assert
import org.junit.Test

class WriteProfileTemplateOperandTest{
    @Test
    internal fun PrimaryConstructorTest(){
        val oneTimeblockOperand = WriteProfileTemplateOperand(true, 1, 2, SingleValueTimeBlock(4,5f))
        Assert.assertEquals(true, oneTimeblockOperand.isEndTransaction)
        Assert.assertEquals(1, oneTimeblockOperand.templateNumber)
        Assert.assertEquals(2, oneTimeblockOperand.firstTimeBlockIndex)
        Assert.assertEquals(1, oneTimeblockOperand.timeblocks.size)
        Assert.assertEquals(4, oneTimeblockOperand.timeblocks[0].duration)
        Assert.assertEquals(5f, oneTimeblockOperand.timeblocks[0].value)

    }
}