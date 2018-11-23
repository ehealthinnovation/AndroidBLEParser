package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.CgmUuid
import java.util.*

/**
 * Represent the class of command written to CGM specific control point(CGMCP). Any commands that
 * write to CGMCP should extend this class
 */
abstract class CamControlCommand {
    val uuid: UUID = CgmUuid.SPECIFIC_CONTROL_POINT.uuid
}

class StartSession : CamControlCommand()

class StopSession : CamControlCommand()
