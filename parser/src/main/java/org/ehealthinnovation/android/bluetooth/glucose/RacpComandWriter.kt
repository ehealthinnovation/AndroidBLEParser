package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import java.util.*

/**
 * An Interface for creating glucose RACP commands
 */
class RacpComandWriter {

    /**
     * Report all records stored in a device
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     */
    fun allRecords(writer: DataWriter): Boolean {

        return true
    }

    /**
     * Report the last(newest) record stored in a device
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     */
    fun lastRecord(writer: DataWriter): Boolean {
        return true

    }


    /**
     * Report the first(oldest) record stored in a device
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     */
    fun firstRecord(writer: DataWriter): Boolean {
        return true

    }

    /**
     * Report records based on filtering criteria specified in [Operator], [lowerSequenceNumber] and [upperSequenceNumber]
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @param operator specify the filtering mechanism.
     * Choose [Operator.LESS_THAN_OR_EQUAL_TO], [Operator.GREATER_THAN_OR_EQUAL_TO] or [Operator.WITHIN_RANGE_OF_INCLUSIVE],
     * other values will fail the write process.
     *
     * @param lowerSequenceNumber the lower bound of filter parameter, input null if the operator does not need a lower bound,
     * for example [Operator.LESS_THAN_OR_EQUAL_TO]
     *
     * @param upperSequenceNumber the upper bound of filter parameter, input null if the operator does not need a upper bound
     * for example [Operator.GREATER_THAN_OR_EQUAL_TO]
     *
     * @return true if command is written to the data writer; false if not
     */
    fun recordsWithSeuqnceNumber(writer: DataWriter, operator: Operator, lowerSequenceNumber: Int?, upperSequenceNumber: Int?): Boolean {
        return true
    }

    /**
     * Report records based on filtering criteria specified in [Operator], [lowerDate] and [upperDate]
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @param operator specify the filtering mechanism.
     * Choose [Operator.LESS_THAN_OR_EQUAL_TO], [Operator.GREATER_THAN_OR_EQUAL_TO] or [Operator.WITHIN_RANGE_OF_INCLUSIVE],
     * other values will fail the write process.
     *
     * @param lowerDate the lower bound of filter parameter, input null if the operator does not need a lower bound,
     * for example [Operator.LESS_THAN_OR_EQUAL_TO]
     *
     * @param upperDate the upper bound of filter parameter, input null if the operator does not need a upper bound
     * for example [Operator.GREATER_THAN_OR_EQUAL_TO]
     *
     * @return true if command is written to the data writer; false if not
     */
    fun recordsWithDate(writer: DataWriter, operator: Operator, lowerDate: Date?, upperDate: Date?): Boolean {
        return true

    }

    /**
     * report the number of all records stored in a device
     *
     * @param writer  a [datawriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     */
    fun numberOfAllRecords(writer: DataWriter): Boolean {
        return true

    }

    /**
     * Report number of records based on filtering criteria specified in [Operator], [lowerSequenceNumber] and [upperSequenceNumber]
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @param operator specify the filtering mechanism.
     * Choose [Operator.LESS_THAN_OR_EQUAL_TO], [Operator.GREATER_THAN_OR_EQUAL_TO] or [Operator.WITHIN_RANGE_OF_INCLUSIVE],
     * other values will fail the write process.
     *
     * @param lowerSequenceNumber the lower bound of filter parameter, input null if the operator does not need a lower bound,
     * for example [Operator.LESS_THAN_OR_EQUAL_TO]
     *
     * @param upperSequenceNumber the upper bound of filter parameter, input null if the operator does not need a upper bound
     * for example [Operator.GREATER_THAN_OR_EQUAL_TO]
     *
     * @return true if command is written to the data writer; false if not
     */
    fun numberOfRecordsWithSeuqnceNumber(writer: DataWriter, operator: Operator, lowerSequenceNumber: Int?, upperSequenceNumber: Int?): Boolean {
        return true

    }

    /**
     * Report number of records based on filtering criteria specified in [Operator], [lowerDate] and [upperDate]
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @param operator specify the filtering mechanism.
     * Choose [Operator.LESS_THAN_OR_EQUAL_TO], [Operator.GREATER_THAN_OR_EQUAL_TO] or [Operator.WITHIN_RANGE_OF_INCLUSIVE],
     * other values will fail the write process.
     *
     * @param lowerDate the lower bound of filter parameter, input null if the operator does not need a lower bound,
     * for example [Operator.LESS_THAN_OR_EQUAL_TO]
     *
     * @param upperDate the upper bound of filter parameter, input null if the operator does not need a upper bound
     * for example [Operator.GREATER_THAN_OR_EQUAL_TO]
     *
     * @return true if command is written to the data writer; false if not
     *
     */
    fun numberOfRecordsWithDate(writer: DataWriter, operator: Operator, lowerDate: Date?, upperDate: Date?): Boolean {
        return true

    }

    /**
     * Delete all records stored in a device
     *
     * @param writer a [datawriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     */
    fun deleteAllRecords(writer: DataWriter): Boolean {
        return true

    }

    /**
     * Delete the last(newest) record stored in a device
     *
     * @param writer a [datawriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     *
     */
    fun deleteLastRecord(writer: DataWriter): Boolean {
        return true

    }

    /**
     * Delete the first(oldest) record stored in a device
     *
     * @param writer a [datawriter] to write the command to
     *
     * @return true if command is written to the data writer; false if not
     */
    fun deleteFirstRecord(writer: DataWriter): Boolean {
        return true

    }

    /**
     * Delete records based on filtering criteria specified in [Operator], [lowerSequenceNumber] and [upperSequenceNumber]
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @param operator specify the filtering mechanism.
     * Choose [Operator.LESS_THAN_OR_EQUAL_TO], [Operator.GREATER_THAN_OR_EQUAL_TO] or [Operator.WITHIN_RANGE_OF_INCLUSIVE],
     * other values will fail the write process.
     *
     * @param lowerSequenceNumber the lower bound of filter parameter, input null if the operator does not need a lower bound,
     * for example [Operator.LESS_THAN_OR_EQUAL_TO]
     *
     * @param upperSequenceNumber the upper bound of filter parameter, input null if the operator does not need a upper bound
     * for example [Operator.GREATER_THAN_OR_EQUAL_TO]
     *
     * @return true if command is written to the data writer; false if not
     *
     */
    fun deleteRecordsWithSeuqnceNumber(writer: DataWriter, operator: Operator, lowerSequenceNumber: Int?, upperSequenceNumber: Int?): Boolean {
        return true

    }

    /**
     *
     * Delete records based on filtering criteria specified in [Operator], [lowerDate] and [upperDate]
     *
     * @param writer  a [DataWriter] to write the command to
     *
     * @param operator specify the filtering mechanism.
     * Choose [Operator.LESS_THAN_OR_EQUAL_TO], [Operator.GREATER_THAN_OR_EQUAL_TO] or [Operator.WITHIN_RANGE_OF_INCLUSIVE],
     * other values will fail the write process.
     *
     * @param lowerDate the lower bound of filter parameter, input null if the operator does not need a lower bound,
     * for example [Operator.LESS_THAN_OR_EQUAL_TO]
     *
     * @param upperDate the upper bound of filter parameter, input null if the operator does not need a upper bound
     * for example [Operator.GREATER_THAN_OR_EQUAL_TO]
     *
     * @return true if command is written to the data writer; false if not
     *
     */
    fun deleteRecordsWithDate(writer: DataWriter, operator: Operator, lowerDate: Date?, upperDate: Date?): Boolean {
        return true
    }
}
