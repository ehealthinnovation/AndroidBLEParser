package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.*

/** Base class for all response of IDD command control point commands. Specific response to each command
 * should extend this class. This class is the default output from the [IddCommandControlParser.parse]
 */
abstract class IddCommandControlResponse

/** Response to the [SnoozeAnnunciation] command, if it was executed successfully.
 * @param id the annunciation id that was snoozed.
 */
data class SnoozeAnnunciationResponse(val id: Int) : IddCommandControlResponse()

/** Response to the [ConfirmAnnunciation] command, if it was executed successfully.
 * @param id the annunciation id that was confirmed.
 */
data class ConfirmAnnunciationResponse(val id: Int) : IddCommandControlResponse()

/**
 * General response to commands. This form of response is returned when a command fails or a command
 * just need a simple indication for success.
 * @property request the opcode of the original command
 * @property result the result of the command operation
 */
data class GeneralResponse(val request: Opcode, val result: ResponseCode): IddCommandControlResponse()

/**
 * The response to [GetTbrTemplate] command.
 * @property templateNumber the template number of TBR template
 * @property type the tbr type
 * @property value the value of the tbr. Depends on the TBR type, it has unit IU/h if it is [TbrType.ABSOLUTE] or has no unit if it is [TbrType.RELATIVE]
 * @property duration the duration of the TBR duration in minute
 */
data class GetTbrTemplateResponse (
        val templateNumber: Int,
        val type: TbrType,
        val value: Float,
        val duration: Int
): IddCommandControlResponse()

/**
 * The reponse to [CancelBolus]
 * @property id the id of bolus that get cancelled
 */
data class CancelBolusResponse(
        val id: Int
): IddCommandControlResponse()