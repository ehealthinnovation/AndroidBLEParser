package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

class CgmControlOperandComposer {

    /**
     * Compose the operand [CommunicationInterval] in to data buffer [DataWriter]
     */
    internal fun composeCommunicationInterval(interval: CommunicationInterval, writer: DataWriter){
       writer.putInt(interval.intervalMinute, IntFormat.FORMAT_UINT8)
    }

}