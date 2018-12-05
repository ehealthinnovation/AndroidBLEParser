package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.writeEnumFlags

/**
 * Compose [StatusReaderControlOperand] to the data buffer through [DataWriter]
 */
class StatusReaderControlOperandComposer {

    internal fun composeStatusFlagToReset(resetStatus: StatusFlagToReset, writer: DataWriter) {
        writeEnumFlags(resetStatus.flagsToReset, IntFormat.FORMAT_UINT16, writer)
    }
}