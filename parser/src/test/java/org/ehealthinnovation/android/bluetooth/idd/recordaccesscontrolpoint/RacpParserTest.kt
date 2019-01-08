package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class RacpParserTest{

    @Test
    fun canParseSmokeTest(){
        testMatcherForSpecificBluetoothUuid("00002B24-0000-1000-8000-00805F9B34FB", RacpParser()::canParse)
    }

    @Test
    fun parsingGenericResponseSanityCheckWithPacket() {
        val data = StubDataReader(uint8(Operator.NULL.key), uint8(Opcode.DELETE_STORED_RECORDS.key), uint8(RacpResponseCode.SUCCESS.key))

        val actualOutput = RacpParser().readGeneralResponse(data)
        val expectedOutput = RacpGeneralResponse(Opcode.DELETE_STORED_RECORDS, RacpResponseCode.SUCCESS)

        Assert.assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun parsingMethodGenericResponseIntegrationSanityCheck() {
        val mockDataPacket: CharacteristicPacket = mockResponsePacket(uint8(Opcode.RESPONSE_CODE.key), uint8(Operator.NULL.key), uint8(Opcode.DELETE_STORED_RECORDS.key), uint8(RacpResponseCode.SUCCESS.key))

        val actualOutput = RacpParser().parse(mockDataPacket)
        val expectedOutput = RacpGeneralResponse(Opcode.DELETE_STORED_RECORDS, RacpResponseCode.SUCCESS)

        Assert.assertEquals(expectedOutput, actualOutput)
    }

    private fun mockResponsePacket(vararg testValues: StubValue): CharacteristicPacket {
        val data = StubDataReader(*testValues)
        val mockPacket = mock<CharacteristicPacket>()
        whenever(mockPacket.readData()).thenReturn(data)
        return mockPacket
    }

    @Test
    fun readGetRecordNumberResponseSanityCheckBlackBox() {
        val mockPacket = mockResponsePacket(uint8(Opcode.NUMBER_OF_STORED_RECORDS_RESPONSE.key), uint8(Operator.NULL.key), uint32(123))

        val expectedOutput = RacpGetRecordNumberResponse(123)
        val actualOutput = RacpParser().parse(mockPacket)

        Assert.assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun readGetRecordNumberResponseSanityCheck() {
        val dataReader = StubDataReader(uint8(Operator.NULL.key), uint32(123))
        val expectedOutput = RacpGetRecordNumberResponse(123)
        val actualOutput = RacpParser().readGetRecordNumberResponse(dataReader)

        Assert.assertEquals(true, expectedOutput == actualOutput)
    }

    @Test(expected = Exception::class)
    fun parsingMethodWithUnsupportedOpcode() {
        val mockPacket = mockResponsePacket((uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key)))
        RacpParser().parse(mockPacket)
    }

    @Test(expected = Exception::class)
    fun readGetRecordNumberResponseThrowExceptionWhenResponseInvalid() {
        val dataReader = StubDataReader(uint8(Operator.GREATER_THAN_OR_EQUAL_TO.key), uint32(123))
        RacpParser().readGetRecordNumberResponse(dataReader)
    }

    @Test(expected = Exception::class)
    fun parsingGenericResponseInvalidArgument() {
        val mockRacpParser: RacpParser = mock()
        whenever(mockRacpParser.readGeneralResponse(any())).thenCallRealMethod()
        whenever(mockRacpParser.genericResponseValid(any(), any(), any())).thenReturn(false)
        mockRacpParser.readGeneralResponse(mock())
    }


    @Test
    fun recordNumberResponseValid() {
        Assert.assertEquals(true, RacpParser().recordNumberResponseValid(Operator.NULL, 3))
        Assert.assertEquals(false, RacpParser().recordNumberResponseValid(Operator.RESERVED_FOR_FUTURE_USE, 3))
        Assert.assertEquals(false, RacpParser().recordNumberResponseValid(Operator.NULL, -1))
    }

}