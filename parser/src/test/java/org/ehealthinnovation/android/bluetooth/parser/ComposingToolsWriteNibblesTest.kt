package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert.*
import org.junit.Test

class ComposingToolsWriteNibblesTest{
    @Test
    fun writeInRangeNibble(){
        val dataWriter = StubDataWriter(uint8(1,2))
        writeByteFromNibbles(1,2,dataWriter)
        dataWriter.checkWriteComplete()
    }

    @Test(expected = Exception::class)
    fun writeInvalidNibble(){
        val dataWriter = StubDataWriter(uint8(1,2))
        writeByteFromNibbles(16,2,dataWriter)
    }

}