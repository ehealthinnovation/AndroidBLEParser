package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.*

/** Base class for all response of IDD command control point commands. Specific response to each command
 * should extend this class. This class is the default output from the [IddCommandControlParser.parse]
 */
abstract class IddCommandControlResponse

/** Response to the [SnoozeAnnunciation] command, if it was executed successfully.
 * @property id the annunciation id that was snoozed.
 */
data class SnoozeAnnunciationResponse(val id: Int) : IddCommandControlResponse()

/**
 * General response to commands. This form of response is returned when a command fails or a command
 * just need a simple indication for success.
 * @property request the opcode of the original command
 * @property result the result of the command operation
 */
data class GeneralResponse(val request: Opcode, val result: ResponseCode): IddCommandControlResponse()