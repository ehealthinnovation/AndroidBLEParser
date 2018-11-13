package org.ehealthinnovation.android.bluetooth.parser

import java.util.*

/**
 * Glucose characteristics are assigned a unit id by Bluetooth Sig.
 *
 * These 32 bits [shortUuid] is part of a longer 128 bits Bluetooth [UUID], which has the rest 92 bits
 * fixed
 */
enum class GlucoseUuid constructor(val shortUuid: Int) {
    GLUCOSE_FEATURE(0x2A51),
    GLUCOSE_MEASUREMENT(0x2A18),
    GLUCOSE_MEASUREMENT_CONTEXT(0x2A34),
    RECORD_ACCESS_CONTROL_POINT( 0x2A52);
    val uuid: UUID = BluetoothUuidHelper.createUuidFromShortUuid(shortUuid)
}


