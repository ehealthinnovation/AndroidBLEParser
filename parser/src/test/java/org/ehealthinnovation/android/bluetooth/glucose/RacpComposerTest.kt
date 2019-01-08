package org.ehealthinnovation.android.bluetooth.glucose

import com.nhaarman.mockito_kotlin.*
import org.ehealthinnovation.android.bluetooth.cgm.CgmRacpOperandComposer
import org.ehealthinnovation.android.bluetooth.cgm.FilteredByTimeOffset
import org.ehealthinnovation.android.bluetooth.cgm.FilteredByTimeOffsetRange
import org.ehealthinnovation.android.bluetooth.common.racp.*
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

        GlucoseRacpComposer().compose(reportNumberOfRecords, dataWriter)
        dataWriter.checkWriteComplete()
    }

    @Test
    fun composeFunctionTestDeleteRecords() {

        val deleteRecordsCommand = DeleteRecords(FilteredBySequenceNumberRange(12, 34))
        val dataWriter = StubDataWriter(
                uint8(Opcode.DELETE_STORED_RECORDS.key),
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint16(12),
                uint16(34)
        )

        GlucoseRacpComposer().compose(deleteRecordsCommand, dataWriter)
        dataWriter.checkWriteComplete()
    }


    @Test
    fun composeFunctionTestReportRecords() {

        val reportRecords = ReportRecords(
                FilteredByBluetoothDateTime(
                        BluetoothDateTimeUtility.createBluetoothDateTime(2018, 11, 12, 1, 2, 3),
                        SingleBoundOperation.GREATER_THAN_OR_EQUAL_TO)
        )


        val dataWriter = StubDataWriter(
                uint8(Opcode.REPORT_STORED_RECORDS.key),
                uint8(Operator.GREATER_THAN_OR_EQUAL_TO.key),
                uint8(Filter.USER_FACING_TIME.key),
                uint16(2018),
                uint8(11),
                uint8(12),
                uint8(1),
                uint8(2),
                uint8(3)
        )

        GlucoseRacpComposer().compose(reportRecords, dataWriter)
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

        GlucoseRacpComposer().composeAbortOperation(testWriter)
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


        inOrder(mockRacpComposer){
            verify(mockRacpComposer, times(1)).composeAbortOperation(any())
            verify(mockRacpComposer, times(1)).composeReportNumberOfRecords(any(),any())
            verify(mockRacpComposer, times(1)).composeDeleteRecords(any(), any())
            verify(mockRacpComposer, times(1)).composeReportRecords(any(), any())
            verifyNoMoreInteractions()
        }
    }


    @Test(expected = Exception::class)
    fun unsupportedCommandThrowsException() {
        val testRacpComposer = GlucoseRacpComposer()
        testRacpComposer.compose(mock(), mock())
    }

    @Test
    fun composeReportNumberOfRecordsUnitSingleBoundFiltering() {
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                    uint8(operation.key),
                    uint8(Filter.SEQUENCE_NUMBER.key),
                    uint16(12)
            )

            val testRacpComposer = GlucoseRacpComposer()
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

        val testRacpComposer = GlucoseRacpComposer()
        val sequenceCommandRange = FilteredBySequenceNumberRange(12, 34)
        testRacpComposer.composeReportNumberOfRecords(sequenceCommandRange, testWriter)
        testWriter.checkWriteComplete()
    }


    @Test
    fun composeReportNumberOfRecordsUnitSingleBoundFilteringOnBluetoothDateTime() {
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(Opcode.REPORT_NUMBER_OF_STORED_RECORDS.key),
                    uint8(operation.key),
                    uint8(Filter.USER_FACING_TIME.key),
                    uint16(2019), uint8(11), uint8(12), uint8(1), uint8(2), uint8(3)
            )

            val testRacpComposer = GlucoseRacpComposer()
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

        val testRacpComposer = GlucoseRacpComposer()
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

        GlucoseRacpOperandComposer().compose(testClass(), dataWriter)
    }

    //Sanity check of compose simple operand
    @Test
    fun composeSimpleOperandTest() {

        for (operation in SimpleOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key)
            )
            GlucoseRacpOperandComposer().composeSimpleOperand(SimpleOperand(operation), testWriter)
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
                    uint16(sequenceNumberBound))
            GlucoseRacpOperandComposer().composeSequenceNumberOperand(FilteredBySequenceNumber(sequenceNumberBound, operation), testWriter)
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

        GlucoseRacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber), testWriter)
        testWriter.checkWriteComplete()
    }


    //Check Invalid Range is caught
    @Test(expected = Exception::class)
    fun composeSequenceNumberRangeOperandTest2() {
        val lowerSequenceNumber = 257
        val higherSequenceNumber = 256

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.SEQUENCE_NUMBER.key),
                uint16(lowerSequenceNumber),
                uint16(higherSequenceNumber)
        )

        GlucoseRacpOperandComposer().composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber), testWriter)
        testWriter.checkWriteComplete()
    }


    //Sanity check of compose single bound time offset filter
    @Test
    fun composeTimeOffsetOperandTest() {
        val timeOffsetBound = 129
        for (operation in SingleBoundOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key),
                    uint8(org.ehealthinnovation.android.bluetooth.cgm.Filter.TIME_OFFSET.key),
                    uint16(timeOffsetBound))
            CgmRacpOperandComposer().composeTimeOffsetOperand(FilteredByTimeOffset(timeOffsetBound, operation), testWriter)
            testWriter.checkWriteComplete()
        }
    }

    //Sanity check of compose time offset range
    @Test
    fun composeTimeOffsetRangeOperandTest() {
        val startTimeOffset = 5
        val endTimeOffset = 128

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(org.ehealthinnovation.android.bluetooth.cgm.Filter.TIME_OFFSET.key),
                uint16(startTimeOffset),
                uint16(endTimeOffset)
        )

        CgmRacpOperandComposer().composeTimeOffsetRangeOperand(FilteredByTimeOffsetRange(startTimeOffset, endTimeOffset), testWriter)
        testWriter.checkWriteComplete()
    }

    //Check Invalid Time offset Range is caught
    @Test(expected = Exception::class)
    fun composeTimeOffsetRangeOperandWithInvalidOperand() {
        val startOffset = 257
        val endOffset = 256

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(org.ehealthinnovation.android.bluetooth.cgm.Filter.TIME_OFFSET.key),
                uint16(startOffset),
                uint16(endOffset)
        )

        CgmRacpOperandComposer().composeTimeOffsetRangeOperand(FilteredByTimeOffsetRange(startOffset, endOffset), testWriter)
        testWriter.checkWriteComplete()
    }


}