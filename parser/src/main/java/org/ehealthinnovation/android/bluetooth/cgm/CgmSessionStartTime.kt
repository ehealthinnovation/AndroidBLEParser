package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.parser.BluetoothDateTime
import org.ehealthinnovation.android.bluetooth.parser.BluetoothTimeZone
import org.ehealthinnovation.android.bluetooth.parser.DstOffset

/**
 * The starting time of the current CGM measurement session.
 *
 * @property time the session start time without the DST adjustment
 * @property timeZone the timezone
 * @property dstOffset the Daylight Saving Time adjustment
 *
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.cgm_session_start_time.xml
 */
data class CgmSessionStartTime (
        val time: BluetoothDateTime,
        val timeZone: BluetoothTimeZone,
        val dstOffset: DstOffset
)