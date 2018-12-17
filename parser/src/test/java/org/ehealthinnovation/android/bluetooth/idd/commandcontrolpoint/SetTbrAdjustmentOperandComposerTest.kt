package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.sfloate
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class SetTbrAdjustmentOperandComposerTest {

    @Test
    fun composeOperand() {
        val operand = TbrAdjustmentOperand(true, TbrType.RELATIVE, 0.9f, 12, 13, TbrDeliveryContext.AP_CONTROLLER)
        val testWriter = StubDataWriter(uint8(7), uint8(TbrType.RELATIVE.key), sfloate(0.9f, -1), uint16(12), uint8(13), uint8(TbrDeliveryContext.AP_CONTROLLER.key))
        SetTbrAdjustmentOperandComposer().composeOperand(operand, testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun getFlagSetTest() {
        val operandWithNoFlag = TbrAdjustmentOperand(false, TbrType.ABSOLUTE, 0f, 12)
        val expected1 = EnumSet.noneOf(SetTbrAdjustmentOperandComposer.Flag::class.java)
        Assert.assertEquals(expected1, SetTbrAdjustmentOperandComposer().getFlagSet(operandWithNoFlag))

        val operandWithAllFlags = TbrAdjustmentOperand(true, TbrType.ABSOLUTE, 0f, 2, 12, TbrDeliveryContext.AP_CONTROLLER)
        val expected2 = EnumSet.allOf(SetTbrAdjustmentOperandComposer.Flag::class.java)
        Assert.assertEquals(expected2, SetTbrAdjustmentOperandComposer().getFlagSet(operandWithAllFlags))
    }
}