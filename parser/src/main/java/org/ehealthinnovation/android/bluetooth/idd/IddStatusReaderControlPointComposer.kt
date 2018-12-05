package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

class IddStatusReaderControlComposer : CharacteristicComposer<StatusReaderControlCommand> {
    override fun canCompose(request: StatusReaderControlCommand): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun compose(request: StatusReaderControlCommand, dataWriter: DataWriter) {
        when (request) {
            is ResetStatus -> composeResetStatusCommand(request.operand, dataWriter)
            else -> IllegalAccessException("The command $request is not supported")
        }
    }

    internal fun composeResetStatusCommand(resetStatus: StatusFlagToReset, writer: DataWriter) {
        writer.putInt(StatusReaderControlOpcode.RESET_STATUS.key, IntFormat.FORMAT_UINT16)
        StatusReaderControlOperandComposer().composeStatusFlagToReset(resetStatus, writer)
    }

}


