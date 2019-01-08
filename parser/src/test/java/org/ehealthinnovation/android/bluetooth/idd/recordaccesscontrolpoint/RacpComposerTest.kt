package org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.common.racp.CommandOperand
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint32
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

class RacpComposerTest {

    @Test
    fun composeFunctionTest() {

        val reportNumberOfRecords = ReportNumberOfRecords(FilteredBySequenceNumberRange(12, 34, Filter.SEQUENCE_NUMBER_FILTERED_BY_NON_REFERENCE_TIME_EVENT))
        val dataWriter = StubDataWriter(
                uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER_FILTERED_BY_NON_REFERENCE_TIME_EVENT.key),
                uint32(12),
                uint32(34)
        )

        RacpComposer().compose(reportNumberOfRecords, dataWriter)
        dataWriter.checkWriteComplete()
    }


    @Test
    fun composeFunctionTestDeleteRecords() {

        val deleteRecordsCommand = DeleteRecords(FilteredBySequenceNumberRange(12, 34, Filter.SEQUENCE_NUMBER))
        val dataWriter = StubDataWriter(
                uint8(Opcode.DELETE_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint32(12),
                uint32(34)
        )

        RacpComposer().compose(deleteRecordsCommand, dataWriter)
        dataWriter.checkWriteComplete()
    }


    @Test
    fun composeFunctionTestReportRecords() {

        val reportRecords = ReportRecords(
                FilteredBySequenceNumber(
                        5,
                        Filter.SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT,
                        org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint.SingleBoundOperation.GREATER_THAN_OR_EQUAL_TO)
        )


        val dataWriter = StubDataWriter(
                uint8(Opcode.REPORT_STORED_RECORDS.key),
                uint8(Operator.GREATER_THAN_OR_EQUAL_TO.key),
                uint8(Filter.SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT.key),
                uint32(5)
        )

        RacpComposer().compose(reportRecords, dataWriter)
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

        //test dispatch report number of records correctly
        val mockReportNumberOfRecords = mock<ReportNumberOfRecords>()
        whenever(mockReportNumberOfRecords.operand).thenReturn(mock())
        mockRacpComposer.compose(mockReportNumberOfRecords, mock())

        //test dispatch delete records correctly
        val mockDeleteRecords = mock<DeleteRecords>()
        whenever(mockDeleteRecords.operand).thenReturn(mock())
        mockRacpComposer.compose(mockDeleteRecords, mock())

        //test dispatch reports records correctly
        val mockReportRecords = mock<ReportRecords>()
        whenever(mockReportRecords.operand).thenReturn(mock())
        mockRacpComposer.compose(mockReportRecords, mock())


        inOrder(mockRacpComposer) {
            verify(mockRacpComposer, times(1)).composeAbortOperation(any())
            verify(mockRacpComposer, times(1)).composeReportNumberOfRecords(any(), any())
            verify(mockRacpComposer, times(1)).composeDeleteRecords(any(), any())
            verify(mockRacpComposer, times(1)).composeReportRecords(any(), any())
            verifyNoMoreInteractions()
        }
    }


    @Test(expected = Exception::class)
    fun unsupportedCommandThrowsException() {
        val testRacpComposer = RacpComposer()
        testRacpComposer.compose(mock(), mock())
    }

    @Test
    fun composeReportNumberOfRecordsUnitSingleBoundFiltering() {
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER.key),
                    uint32(12)
            )

            val testRacpComposer = RacpComposer()
            val sequenceCommandOperand = FilteredBySequenceNumber(12, Filter.SEQUENCE_NUMBER, operation)

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
                uint32(12),
                uint32(34)
        )

        val testRacpComposer = RacpComposer()
        val sequenceCommandRange = FilteredBySequenceNumberRange(12, 34, Filter.SEQUENCE_NUMBER)
        testRacpComposer.composeReportNumberOfRecords(sequenceCommandRange, testWriter)
        testWriter.checkWriteComplete()
    }


    @Test
    fun composeReportNumberOfRecordsUnitSingleBoundFilteringOnSequenceNumberReferenceTimeEvent() {
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT.key),
                    uint32(7)
            )

            val testRacpComposer = RacpComposer()
            val commandOperand = FilteredBySequenceNumber(7, Filter.SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT, operation)

            testRacpComposer.composeReportNumberOfRecords(commandOperand, testWriter)
            testWriter.checkWriteComplete()
        }
    }

    @Test
    fun composeReportNumberOfRecordsUnitDoubleBoundFilteringSequenceNumberReferenceTimeEvent() {

        val testWriter = StubDataWriter(
                uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT.key),
                uint32(45),
                uint32(48)
        )

        val testRacpComposer = RacpComposer()
        val dateTimeCommandRange = FilteredBySequenceNumberRange(
                45,
                48,
                Filter.SEQUENCE_NUMBER_FILTERED_BY_REFERENCE_TIME_EVENT
        )
        testRacpComposer.composeReportNumberOfRecords(dateTimeCommandRange, testWriter)
        testWriter.checkWriteComplete()
    }


    @Test(expected = Exception::class)
    fun composeOperandTestUnsupportedType() {
        //check compose operand
        class testClass() : CommandOperand()

        val dataWriter = StubDataWriter()

        RacpOperandComposer().compose(testClass(), dataWriter)
    }

    //Sanity check of compose simple operand
    @Test
    fun composeSimpleOperandTest() {

        for (operation in SimpleOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER.key)
            )
            RacpOperandComposer().composeFilteredSimpleOperand(SimpleOperationWithFilter(operation, Filter.SEQUENCE_NUMBER), testWriter)
            testWriter.checkWriteComplete()
        }

    }


    //Sanity check of compose single bound sequence number filter
    @Test
    fun composeSequenceNumberOperandTest1() {
        val sequenceNumberBound = 256
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER.key),
                    uint32(sequenceNumberBound))
            RacpOperandComposer().composeSequenceNumberOperand(FilteredBySequenceNumber(sequenceNumberBound, Filter.SEQUENCE_NUMBER, operation), testWriter)
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
                uint32(lowerSequenceNumber),
                uint32(higherSequenceNumber)
        )

        RacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber, Filter.SEQUENCE_NUMBER), testWriter)
        testWriter.checkWriteComplete()
    }


    //Check Invalid Range is caught
    @Test(expected = Exception::class)
    fun composeSequenceNumberRangeOperandTest2() {
        val lowerSequenceNumber = 256
        val higherSequenceNumber = 255

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint32(lowerSequenceNumber),
                uint32(higherSequenceNumber)
        )

        RacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber, Filter.SEQUENCE_NUMBER), testWriter)
        testWriter.checkWriteComplete()
    }


    //Sanity check of compose single bound time offset filter
    @Test
    fun composeSequenceNumberOperandTest() {
        val sequenceNumberBound = 129
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key),
                    uint8(org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint.Filter.SEQUENCE_NUMBER.key),
                    uint32(sequenceNumberBound))
            RacpOperandComposer().composeSequenceNumberOperand(FilteredBySequenceNumber(sequenceNumberBound, Filter.SEQUENCE_NUMBER, operation), testWriter)
            testWriter.checkWriteComplete()
        }
    }

    //Sanity check of compose Sequence Number range
    @Test
    fun composeSequenceNumberRangeOperandTest() {
        val startSequenceNumber = 5
        val endSequenceNumber = 128

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint.Filter.SEQUENCE_NUMBER.key),
                uint32(startSequenceNumber),
                uint32(endSequenceNumber)
        )

        RacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(startSequenceNumber, endSequenceNumber, Filter.SEQUENCE_NUMBER), testWriter)
        testWriter.checkWriteComplete()
    }

     //Sanity check of compose Sequence Number range
    @Test
    fun composeSequenceNumberRangeOperandSpecifySingleValueTest() {
        val startSequenceNumber = 128
        val endSequenceNumber = 128

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint.Filter.SEQUENCE_NUMBER.key),
                uint32(startSequenceNumber),
                uint32(endSequenceNumber)
        )

        RacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(startSequenceNumber, endSequenceNumber, Filter.SEQUENCE_NUMBER), testWriter)
        testWriter.checkWriteComplete()
    }


    //Check Invalid Sequence Number Range is caught
    @Test(expected = Exception::class)
    fun composeSequenceNumberRangeOperandWithInvalidOperand() {
        val startSequenceNumber = 256
        val endSequenceNumber = 255

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(org.ehealthinnovation.android.bluetooth.idd.recordaccesscontrolpoint.Filter.SEQUENCE_NUMBER.key),
                uint32(startSequenceNumber),
                uint32(endSequenceNumber)
        )

        RacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(startSequenceNumber, endSequenceNumber, Filter.SEQUENCE_NUMBER), testWriter)
        testWriter.checkWriteComplete()
    }


}
