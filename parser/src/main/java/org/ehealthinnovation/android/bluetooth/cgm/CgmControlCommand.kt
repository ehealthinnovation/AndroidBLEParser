package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.CgmUuid
import java.util.*

/**
 * Represent the class of command written to CGM specific control point(CGMCP). Any commands that
 * write to CGMCP should extend this class
 */
abstract class CgmControlCommand {
    val uuid: UUID = CgmUuid.SPECIFIC_CONTROL_POINT.uuid
}

/**
 * Send this command to the Cgm Command Control Point to start a cgm measurement session.
 * If this command is sent while the session is started, the session will restart
 */
class StartSession : CgmControlCommand()

/**
 * Send this command to stop a currently running cgm measurement session.
 */
class StopSession : CgmControlCommand()

/**
 * Use this command to configure the reporting interval of glucose measurement.
 * [CommunicationInterval] specify the time between consecutive glucose measurement and estimation.
 */
class SetCommunicationInterval(val operand: CommunicationInterval) : CgmControlCommand()

/**
 * Get the reporting interval of the glucose measurement currently configured in the remote cgm
 * device
 */
class GetCommunicationInterval : CgmControlCommand()

