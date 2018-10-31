package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import java.lang.IllegalArgumentException

/**
 * Composer for the Record Access Control Point
 *
 */
class RacpComposer : CharacteristicComposer<GlucoseCommand> {

    override fun canCompose(request: GlucoseCommand): Boolean {
        //This test if the request meets requirements
        return false
    }


    override fun compose(request: GlucoseCommand, dataWriter: DataWriter) {
        when (request) {
            is AbortOperation -> composeAbortOperation(dataWriter)
            else -> {
                throw IllegalArgumentException("operation not recognized")
            }
        }
    }
}

/**
 * Compose the abort operation into the buffer through [dataWriter]
 *
 * @throws RuntimeException if the underlying buffer fails to process the write operation
 */
internal fun composeAbortOperation(dataWriter: DataWriter) {
    dataWriter.putInt(Opcode.ABORT_OPERATION.key, IntFormat.FORMAT_UINT8)
    dataWriter.putInt(Operator.NULL.key, IntFormat.FORMAT_UINT8)
}


