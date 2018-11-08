package org.ehealthinnovation.android.bluetooth.glucose

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class RacpParserTest {

    private fun mockResponsePacket(vararg testValues: StubValue): CharacteristicPacket {
        val data = StubDataReader(*testValues)
        val mockPacket = mock<CharacteristicPacket>()
        whenever(mockPacket.readData()).thenReturn(data)
        return mockPacket
    }

    @Test
    fun readGetRecordNumberResponseSanityCheckBlackBox() {
        val mockPacket = mockResponsePacket(uint8(Opcode.NUMBER_OF_STORED_RECORDS_RESPONSE.key), uint8(Operator.NULL.key), uint16(123))

        val expectedOutput = RacpGetRecordNumberResponse(123)
        val actualOutput = RacpParser().parse(mockPacket)

        Assert.assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun readGetRecordNumberResponseSanityCheck() {
        val dataReader = StubDataReader(uint8(Operator.NULL.key), uint16(123))
        val parserUnderTest = RacpParser()
        val expectedOutput = RacpGetRecordNumberResponse(123)
        val actualOutput = parserUnderTest.readGetRecordNumberResponse(dataReader)

        Assert.assertEquals(true, expectedOutput == actualOutput)
    }

    @Test(expected = Exception::class)
    fun parsingMethodWithUnsupportedOpcode() {
        val mockPacket = mockResponsePacket((uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key)))
        RacpParser().parse(mockPacket)
    }

    @Test(expected = Exception::class)
    fun readGetRecordNumberResponseThrowExceptionWhenResponseInvalid() {
        val dataReader = StubDataReader(uint8(Operator.GREATER_THAN_OR_EQUAL_TO.key), uint16(123))
        RacpParser().readGetRecordNumberResponse(dataReader)
    }

    @Test
    fun recordNumberResponseValid() {
        Assert.assertEquals(true, RacpParser().recordNumberResponseValid(Operator.NULL, 3))
        Assert.assertEquals(false, RacpParser().recordNumberResponseValid(Operator.RESERVED_FOR_FUTURE_USE, 3))
        Assert.assertEquals(false, RacpParser().recordNumberResponseValid(Operator.NULL, -1))
        Assert.assertEquals(false, RacpParser().recordNumberResponseValid(Operator.NULL, IntFormat.FORMAT_UINT16.maxValue.toInt() + 1))
    }
}