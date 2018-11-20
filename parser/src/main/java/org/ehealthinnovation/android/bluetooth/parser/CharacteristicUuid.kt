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

/**
 * Cgm characteristics are assigned a unit id by Bluetooth Sig.
 *
 * These 32 bits [shortUuid] is part of a longer 128 bits Bluetooth [UUID], which has the rest 92 bits
 * fixed
 */
enum class CgmUuid constructor(val shortUuid: Int) {
    FEATURE(0x2AA8),
    MEASUREMENT(0x2AA7),
    SESSION_RUN_TIME(0x2AAB),
    SESSION_START_TIME(0x2AAA),
    SPECIFIC_CONTROL_POINT(0x2AAC),
    STATUS(0x2AA9),
    RECORD_ACCESS_CONTROL_POINT( 0x2A52);
    val uuid: UUID = BluetoothUuidHelper.createUuidFromShortUuid(shortUuid)
}


