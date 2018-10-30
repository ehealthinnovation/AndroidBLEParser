package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.CharacteristicComposer
import org.ehealthinnovation.android.bluetooth.parser.DataWriter

/**
 * Composer for the Record Access Control Point
 *
 */
class RacpComposer : CharacteristicComposer<GlucoseCommand> {

   override fun canCompose(request: GlucoseCommand): Boolean {
        //This test if the request meets requirements
        return false
    }


   override fun compose(request: GlucoseCommand, dataWriter: DataWriter): Boolean {
        return true
    }
}


