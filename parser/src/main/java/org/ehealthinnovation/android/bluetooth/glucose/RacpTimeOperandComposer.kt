package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.BluetoothDateTime
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import java.util.*

/**
 * The operands of filtering glucose records based on one sided criterion on Bluetooth DateTime for glucose devices
 *
 * @property date the Bluetooth DateTime used in filtering
 *
 * @property operation choose one of the operations enumerated in [GlucoseOperatorBound]
 */
class FilteredByBluetoothDateTime(val date: BluetoothDateTime, val operation: GlucoseOperatorBound) : CommandOperand()

class RacpTimeComposer {

    /**
     * Compose the [operand] for filtering with single bounded [Date] into
     * a buffer through [dataWriter]
     *
     * The [BluetoothDateTime] operand in [FilteredByBluetoothDateTime] must be created by
     * [createBluetoothDateTime] where the validity check of data takes place
     */
    fun composeTimeOperand(operand: FilteredByBluetoothDateTime, dataWriter: DataWriter) {
        dataWriter.putInt(operand.operation.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(Filter.USER_FACING_TIME.key, IntFormat.FORMAT_UINT8)
        composeBluetoothTime(operand.date, dataWriter)
    }

    /**
     * Uses Bluetooth DateTime range defined by [startDate] and [endDate] as filtering criteria for records.
     * And compose the range filer as operand into the buffer through [dataWriter].
     *
     * This function does not check the validity of the date range. The user of this function needs
     * to make sure the [startDate] is before the [endDate]. If an invalid range is input and send to
     * a target device, a response indicating invalid operand is likely returned.
     *
     * The [BluetoothDateTime] of [startDate] and [endDate] must be created by
     * [createBluetoothDateTime] where the validity check of data takes place
     *
     *
     */
    fun composeTimeRangeOperand(startDate: BluetoothDateTime, endDate: BluetoothDateTime, dataWriter: DataWriter) {
        dataWriter.putInt(Operator.WITHIN_RANGE_OF_INCLUSIVE.key, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(Filter.USER_FACING_TIME.key, IntFormat.FORMAT_UINT8)
        composeBluetoothTime(startDate, dataWriter)
        composeBluetoothTime(endDate, dataWriter)
    }


    /**
     * Writes [BluetoothDateTime] into the buffer through [dataWriter]
     *
     * Any fields in [BluetoothDateTime] that is null will be replaced by 0. Meaning the field in unknown.
     *
     * @throws RuntimeException if the underlying buffer fails to process the write operation
     */
    fun composeBluetoothTime(time: BluetoothDateTime, dataWriter: DataWriter) {
        dataWriter.putInt(time.year ?: 0, IntFormat.FORMAT_UINT16)
        dataWriter.putInt(time.month ?: 0, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(time.day ?: 0, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(time.hours, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(time.minutes, IntFormat.FORMAT_UINT8)
        dataWriter.putInt(time.seconds, IntFormat.FORMAT_UINT8)
    }
}

