package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.SetTherapyControlState
import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.idd.TherapyControlState
import org.ehealthinnovation.android.bluetooth.parser.*
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
        val testoperand = TemplatesOperand(listOf(TemplateNumber(1), TemplateNumber(2), TemplateNumber(3)))
        val testWriter = StubDataWriter(uint8(3), uint8(1), uint8(2), uint8(3))
        CommandControlOperandComposer().composeTemplatesNumberListOperand(testoperand, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test(expected = Exception::class)
    fun testResetTemplateStatusIllegalConstruction(){
        val listOfNumber = mutableListOf<TemplateNumber>()
        for (i  in 0..15){
            listOfNumber.add(TemplateNumber(i))
        }
        TemplatesOperand(listOfNumber)
    }

    @Test(expected = Exception::class)
    fun testResetTemplateStatusIllegalConstructionCase2(){
        val listOfNumber = mutableListOf<TemplateNumber>()
        TemplatesOperand(listOfNumber)
    }

    @Test
    fun composeTherapyControlStateOperandTest() {
        val therapyStateToSet = TherapyControlState.RUN
        val testWriter = StubDataWriter(uint8(TherapyControlState.RUN.key))
        CommandControlOperandComposer().composeTherapyControlStateOperand(therapyStateToSet, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composeTbrTemplateOperandTest(){
        val setTbrTemplate = SetTbrAdjustmentTemplateOperand(1, TbrType.ABSOLUTE, 3f, 4)
        val testWriter = StubDataWriter(uint8(1), uint8(TbrType.ABSOLUTE.key), sfloate(3f, -1), uint16(4))
        CommandControlOperandComposer().composeSetTbrTemplate(setTbrTemplate, testWriter)
        testWriter.checkWriteComplete()
    }
}