package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.*

class CgmControlOperandComposer {

    /**
     * Compose the operand [CommunicationInterval] in to data buffer [DataWriter]
     */
    internal fun composeCommunicationInterval(interval: CommunicationInterval, writer: DataWriter){
       writer.putInt(interval.intervalMinute, IntFormat.FORMAT_UINT8)
    }

    /**
     * Compose the calibration record into the [DataWriter]
     */
    internal fun composeCalibrationRecord(record: CalibrationRecord, writer: DataWriter) {
        writer.putFloat(record.concentration, 0, FloatFormat.FORMAT_SFLOAT)
        writer.putInt(record.timeOffsetMinutes, IntFormat.FORMAT_UINT16)
        writeByteFromNibbles(record.sampleType.key, record.sampleLocation.key, writer)
        writer.putInt(record.nextCalibrationTime, IntFormat.FORMAT_UINT16)
        writer.putInt(record.recordNumber, IntFormat.FORMAT_UINT16)
        writeEnumFlags(record.status, IntFormat.FORMAT_UINT8, writer)
    }


}