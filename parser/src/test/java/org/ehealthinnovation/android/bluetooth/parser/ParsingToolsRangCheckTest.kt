package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import org.junit.Test

class ParsingToolsRangCheckTest {
    @Test
    fun intWithinRangeTest() {
        for(format in IntFormat.values()){
            Assert.assertEquals(true, isIntWithinRange(0, format))
            Assert.assertEquals(true, isIntWithinRange(format.maxValue, format))
            Assert.assertEquals(true, isIntWithinRange(format.minValue, format))
            Assert.assertEquals(false, isIntWithinRange(format.minValue-1, format))
            Assert.assertEquals(false, isIntWithinRange(format.maxValue+1, format))
        }
    }

    @Test
    fun intRangeValidityTest(){
        Assert.assertEquals(true, isIntRangeValid(1,2))
        Assert.assertEquals(false, isIntRangeValid(1,1))
        Assert.assertEquals(false, isIntRangeValid(2,1))
    }

}