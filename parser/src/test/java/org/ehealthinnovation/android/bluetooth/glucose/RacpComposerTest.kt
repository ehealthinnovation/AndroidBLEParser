package org.ehealthinnovation.android.bluetooth.glucose

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Test

class RacpComposerTest {

    @Test
    fun composeFunctionTest() {

        val reportNumberOfRecords = ReportNumberOfRecords(FilteredBySequenceNumberRange(12, 34))
        val dataWriter = StubDataWriter(
                uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint16(12),
                uint16(34)
        )

        RacpComposer().compose(reportNumberOfRecords, dataWriter)
        dataWriter.checkWriteComplete()
    }


    @Test
    fun composeAbortOperation() {
        //preload the data writer with expected data, and pass it to a composer
        //the writer will verify the composed data against the expected one

        val testWriter = StubDataWriter(
                uint8(Opcode.ABORT_OPERATION.key),
                uint8(Operator.NULL.key)
        )

        RacpComposer().composeAbortOperation(testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composeFunctionDispatchCorrectly() {
        val mockRacpComposer: RacpComposer = mock()
        whenever(mockRacpComposer.compose(any(), any())).thenCallRealMethod()

        //test dispatch abort operation
        mockRacpComposer.compose(mock<AbortOperation>(), mock())
        verify(mockRacpComposer).composeAbortOperation(any())

        //test dispatch report number of records correctly
        val mockReportNumberOfRecords = mock<ReportNumberOfRecords>()
        whenever(mockReportNumberOfRecords.operand).thenReturn(mock())
        mockRacpComposer.compose(mockReportNumberOfRecords, mock())
        verify(mockRacpComposer).composeReportNumberOfRecords(any(), any())
    }


    @Test(expected = Exception::class)
    fun unsupportedCommandThrowsException() {
        val testRacpComposer = RacpComposer()
        testRacpComposer.compose(mock(), mock())
    }

    @Test
    fun composeReportNumberOfRecordsUnitSingleBoundFiltering() {
        for (operation in GlucoseOperatorBound.values()) {
            val testWriter = StubDataWriter(
                    uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER.key),
                    uint16(12)
            )

            val testRacpComposer = RacpComposer()
            val sequenceCommandOperand = FilteredBySequenceNumber(12, operation)

            testRacpComposer.composeReportNumberOfRecords(sequenceCommandOperand, testWriter)
            testWriter.checkWriteComplete()
        }
    }

    @Test
    fun composeReportNumberOfRecordsUnitDoubleBoundFiltering() {

        val testWriter = StubDataWriter(
                uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint16(12),
                uint16(34)
        )

        val testRacpComposer = RacpComposer()
        val sequenceCommandRange = FilteredBySequenceNumberRange(12, 34)
        testRacpComposer.composeReportNumberOfRecords(sequenceCommandRange, testWriter)
        testWriter.checkWriteComplete()
    }


    @Test
    fun composeReportNumberOfRecordsUnitSingleBoundFilteringOnBluetoothDateTime() {
        for (operation in GlucoseOperatorBound.values()) {
            val testWriter = StubDataWriter(
                    uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                    uint8(operation.key),
                    uint8(Filter.USER_FACING_TIME.key),
                    uint16(2019), uint8(11), uint8(12), uint8(1), uint8(2), uint8(3)
            )

            val testRacpComposer = RacpComposer()
            val dateTimeCommandOperand = FilteredByBluetoothDateTime(BluetoothDateTime(2019, 11, 12, 1, 2, 3), operation)

            testRacpComposer.composeReportNumberOfRecords(dateTimeCommandOperand, testWriter)
            testWriter.checkWriteComplete()
        }
    }

    @Test
    fun composeReportNumberOfRecordsUnitDoubleBoundFilteringOnBluetoothDateTime() {

        val testWriter = StubDataWriter(
                uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.USER_FACING_TIME.key),
                uint16(2019), uint8(11), uint8(12), uint8(1), uint8(2), uint8(3),
                uint16(2019), uint8(11), uint8(12), uint8(1), uint8(2), uint8(4)
        )

        val testRacpComposer = RacpComposer()
        val dateTimeCommandRange = FilteredByBluetoothDateTimeRange(
                BluetoothDateTimeUtility.createBluetoothDateTime(2019, 11, 12, 1, 2, 3),
                BluetoothDateTimeUtility.createBluetoothDateTime(2019, 11, 12, 1, 2, 4))
        testRacpComposer.composeReportNumberOfRecords(dateTimeCommandRange, testWriter)
        testWriter.checkWriteComplete()
    }


    @Test(expected = Exception::class)
    fun composeOperandTestUnsupportedType() {
        //check compose operand
        class testClass() : CommandOperand()

        val dataWriter = StubDataWriter()

        RacpOperandComposer.composeOperand(testClass(), dataWriter)
    }

    //Sanity check of compose simple operand
    @Test
    fun composeSimpleOperandTest() {

        for (operation in GlucoseSimpleOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key)
            )
            RacpOperandComposer.composeSimpleOperand(SimpleOperand(operation), testWriter)
            testWriter.checkWriteComplete()
        }

    }

    //Sanity check of compose single bound sequence number filter
    @Test
    fun composeSequenceNumberOperandTest1() {
        val sequenceNumberBound = 256
        for (operation in GlucoseOperatorBound.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER.key),
                    uint16(sequenceNumberBound))
            RacpOperandComposer.composeSequenceNumberOperand(FilteredBySequenceNumber(sequenceNumberBound, operation), testWriter)
            testWriter.checkWriteComplete()
        }
    }

    //Sanity check of compose sequence number range
    @Test
    fun composeSequenceNumberRangeOperandTest1() {
        val lowerSequenceNumber = 2
        val higherSequenceNumber = 256

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint16(lowerSequenceNumber),
                uint16(higherSequenceNumber)
        )

        RacpOperandComposer.composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber), testWriter)
        testWriter.checkWriteComplete()
    }


    //Check Invalid Range is caught
    @Test(expected = Exception::class)
    fun composeSequenceNumberRangeOperandTest2() {
        val lowerSequenceNumber = 256
        val higherSequenceNumber = 256

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint16(lowerSequenceNumber),
                uint16(higherSequenceNumber)
        )

        RacpOperandComposer.composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber), testWriter)
        testWriter.checkWriteComplete()
    }


}