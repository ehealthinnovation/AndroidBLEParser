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

data class WriteBasalRateProfileTemplateResponse(
        val isTransactionCompleted: Boolean,
        val basalRateProfileTemplate: Int,
        val firstTimeBlockNumber: Int
): IddCommandControlResponse()

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
 * Response to [SetTbrTemplate]
 * @property templateNumber the template number of the TBR template being set
 */
data class SetTbrTemplateResponse(val templateNumber: Int): IddCommandControlResponse()

/**
 * The response to command [SetBolus]. The server will response with an assigned bolus ID
 * @property id the unique identifier created by the server in response to a set bolus command
 */
data class SetBolusResponse(val id: Int): IddCommandControlResponse()

/**
 * The response to [GetTemplate] command.
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

/**
 * The response to [GetAvailableBoluses]
 * @property fastBolusAvailable is true if the remote insulin pump can administer a fast bolus.
 * @property extendedBolusAvailable is true if the remote insulin pump can administer an extended bolus.
 * @property multiwaveBolusAvailable is true if the remote insulin pump can administer a multiwave bolus.
 */
data class GetAvailableBolusesResponse(
        val fastBolusAvailable: Boolean,
        val extendedBolusAvailable: Boolean,
        val multiwaveBolusAvailable: Boolean
): IddCommandControlResponse()
