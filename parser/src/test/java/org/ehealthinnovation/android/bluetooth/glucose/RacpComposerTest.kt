package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

class RacpComposerTest {

    @Test
    fun composeAbortOperation() {
        //preload the data writer with expected data, and pass it to a composer
        //the writer will verify the composed data against the expected one

        val testWriter = StubDataWriter(
                uint8(Opcode.ABORT_OPERATION.key),
                uint8(Operator.NULL.key)
        )

        composeAbortOperation(testWriter)
        testWriter.checkWriteComplete()
    }

    @Test
    fun composeReportNumberOfRecords() {
        //todo  postpone implementation until we figure out how to verify a top level function is called
    }

    @Test
    fun composeOperandTest() {
        //todo  postpone implementation until we figure out how to verify a top level function is called
    }

    //Sanity check of compose simple operand
    @Test
    fun composeSimpleOperandTest() {
        for (operation in GlucoseSimpleOperation.values()) {
            val testWriter = StubDataWriter(
                    uint8(operation.key)
            )
            composeSimpleOperand(SimpleOperand(operation), testWriter)
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
                    uint16(sequenceNumberBound))
            composeSequenceNumberOperand(FilteredBySequenceNumber(sequenceNumberBound, operation), testWriter)
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
                uint16(lowerSequenceNumber),
                uint16(higherSequenceNumber)
        )

        composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber), testWriter)
        testWriter.checkWriteComplete()
    }


    //Check Invalid Range is caught
    @Test(expected = Exception::class)
    fun composeSequenceNumberRangeOperandTest2() {
        val lowerSequenceNumber = 256
        val higherSequenceNumber = 256

        val testWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint16(lowerSequenceNumber),
                uint16(higherSequenceNumber)
        )

        composeSequenceNumberRangeOperand(FilteredBySequenceNumberRange(lowerSequenceNumber, higherSequenceNumber), testWriter)
    }
}