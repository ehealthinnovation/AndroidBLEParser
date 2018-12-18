package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

import org.junit.Assert.*

class CommandControlOperandComposerTest {

    @Test
    fun composeTbrTemplateOperand() {
        val tbrTemplateNumber = TbrTemplateNumber(2)
        val testWriter = StubDataWriter(uint8(2))
        CommandControlOperandComposer().composeTbrTemplateOperand(tbrTemplateNumber, testWriter)
        testWriter.checkWriteComplete()
    }
}