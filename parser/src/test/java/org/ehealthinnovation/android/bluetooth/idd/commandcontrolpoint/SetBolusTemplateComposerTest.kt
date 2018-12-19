package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.sfloate
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test
import java.util.*

class SetBolusTemplateComposerTest {

    @Test
    fun composeOperandTest() {
        val templateToWrite = SetBolusTemplateOperand(BolusTemplate(1, Bolus(BolusType.MULTIWAVE, 2f, 3f, 4), true, false, 3))
        val testWriter = StubDataWriter(uint8(1), uint8(0b011), uint8(BolusType.MULTIWAVE.key), sfloate(2f, -1), sfloate(3f, -1), uint16(4), uint16(3) )
        SetBolusTemplateComposer().composeOperand(templateToWrite,testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun getFlagsTest() {
        val testCaseNoFlag = SetBolusTemplateOperand(BolusTemplate(1, Bolus(BolusType.FAST, 2f, 0f, 0), false, false))
        val expectedFlag = EnumSet.noneOf(GetBolusTemplateResponseParser.Flag::class.java)
        Assert.assertEquals(expectedFlag, SetBolusTemplateComposer().getFlags(testCaseNoFlag.template))

        val testCaseAllFlag = SetBolusTemplateOperand(BolusTemplate(1, Bolus(BolusType.FAST, 2f, 0f, 0), true, true, 3))
        val expectedAllFlag = EnumSet.allOf(GetBolusTemplateResponseParser.Flag::class.java)
        Assert.assertEquals(expectedAllFlag, SetBolusTemplateComposer().getFlags(testCaseAllFlag.template))

        val testCaseSomeFlag = SetBolusTemplateOperand(BolusTemplate(1, Bolus(BolusType.FAST, 2f, 0f, 0), true, false, 3))
        val expectedSomeFlag = EnumSet.of(GetBolusTemplateResponseParser.Flag.BOLUS_DELIVERY_REASON_CORRECTION, GetBolusTemplateResponseParser.Flag.BOLUS_DELAY_TIME_PRESENT)
        Assert.assertEquals(expectedSomeFlag, SetBolusTemplateComposer().getFlags(testCaseSomeFlag.template))

    }
}