package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Composer for the control commands to the CGM Specific Command Control Point
 *
 * Call [canCompose] on your [CamControlCommand] to make sure it is supported. If it does,
 * [canCompose] returns true.
 *
 * Then call [compose] with the [CamControlCommand] and the target data buffer [DataWriter] to
 * write the serialized request into the target buffer.
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_specific_ops_control_point.xml
 *
 * @throws Exception if write operation is unsuccessful
 */
class CgmControlComposer : CharacteristicComposer<CamControlCommand> {
    override fun canCompose(request: CamControlCommand): Boolean {
        //todo  update this method
        return false
    }

    override fun compose(request: CamControlCommand, dataWriter: DataWriter) {
        when (request) {
            is StartSession -> composeStartSession(dataWriter)
            is StopSession -> composeStopSession(dataWriter)
            else -> throw IllegalArgumentException("operation not recognized")
        }
    }

    internal fun composeStartSession(dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.START_THE_SESSION.key, IntFormat.FORMAT_UINT8)
    }

    internal fun composeStopSession(dataWriter: DataWriter) {
        dataWriter.putInt(Opcode.STOP_THE_SESSION.key, IntFormat.FORMAT_UINT8)
    }

}