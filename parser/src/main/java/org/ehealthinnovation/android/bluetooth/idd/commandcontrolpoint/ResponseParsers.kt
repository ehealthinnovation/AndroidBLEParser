package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * This class contains a set of Command control point response parsing functions that are structurally
 * simple (only a single function is needed for parsing). If the parsing process is more complicated, a
 * dedicated class will be create for those responses.
 */
class SimpleResponseParser {

    /** Parse the response of a [SnoozeAnnunciation] command*/
    internal fun parseSnoozeAnnunciationResponse(data: DataReader): SnoozeAnnunciationResponse =
            SnoozeAnnunciationResponse(data.getNextInt(IntFormat.FORMAT_UINT16))


}