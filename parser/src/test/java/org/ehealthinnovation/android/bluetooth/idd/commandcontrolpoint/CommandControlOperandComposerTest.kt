package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

class CommandControlOperandComposerTest {

    @Test
    fun composeTbrTemplateOperand() {
        val tbrTemplateNumber = TemplateNumber(2)
        val testWriter = StubDataWriter(uint8(2))
        CommandControlOperandComposer().composeTemplateNumberOperand(tbrTemplateNumber, testWriter)
        testWriter.checkWriteComplete()
    }
}