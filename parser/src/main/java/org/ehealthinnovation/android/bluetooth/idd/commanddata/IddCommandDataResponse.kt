package org.ehealthinnovation.android.bluetooth.idd.commanddata

import org.ehealthinnovation.android.bluetooth.idd.IddCommandDataParser

/** Base class for all response of IDD command data characteristic. Specific response to each command
 * should extend this class. This class is the default output from the [IddCommandDataParser.parse]
 */
abstract class IddCommandDataResponse


