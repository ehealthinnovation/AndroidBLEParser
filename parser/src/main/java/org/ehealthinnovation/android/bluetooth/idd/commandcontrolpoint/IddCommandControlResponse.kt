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