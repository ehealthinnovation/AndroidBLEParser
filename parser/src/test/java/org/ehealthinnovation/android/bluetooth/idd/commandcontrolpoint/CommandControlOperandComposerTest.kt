package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test
import java.lang.Exception

class CommandControlOperandComposerTest {

    @Test
    fun composeTbrTemplateOperand() {
        val tbrTemplateNumber = TemplateNumber(2)
        val testWriter = StubDataWriter(uint8(2))
        CommandControlOperandComposer().composeTemplateNumberOperand(tbrTemplateNumber, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composeResetTemplateStatusOperandTest() {
        val testoperand = ResetTemplateStatusOperand(listOf(TemplateNumber(1), TemplateNumber(2), TemplateNumber(3)))
        val testWriter = StubDataWriter(uint8(3), uint8(1), uint8(2), uint8(3))
        CommandControlOperandComposer().composeResetTemplateStatusOperand(testoperand, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test(expected = Exception::class)
    fun testResetTemplateStatusIllegalConstruction(){
        val listOfNumber = mutableListOf<TemplateNumber>()
        for (i  in 0..15){
            listOfNumber.add(TemplateNumber(i))
        }
        ResetTemplateStatusOperand(listOfNumber)
    }

    @Test(expected = Exception::class)
    fun testResetTemplateStatusIllegalConstructionCase2(){
        val listOfNumber = mutableListOf<TemplateNumber>()
        ResetTemplateStatusOperand(listOfNumber)
    }
}