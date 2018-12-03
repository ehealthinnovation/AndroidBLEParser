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
    RECORD_ACCESS_CONTROL_POINT(0x2A52);

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
    RECORD_ACCESS_CONTROL_POINT(0x2A52);

    val uuid: UUID = BluetoothUuidHelper.createUuidFromShortUuid(shortUuid)
}

/**
 * Insulin Delivery Device (IDD) characteristics are assigned a unique id by Bluetooth Sig.
 *
 * These 32 bits [shortUuid] is part of a longer 128 bits Bluetooth [UUID], which was the rest 92 bits
 * fixed
 */
enum class IddUuid constructor(val shortUuid: Int) {
    ANNUNCIATION_STATUS(0x2B22),
    COMMAND_CONTROL_POINT(0x2B25),
    COMMAND_DATA(0x2B26),
    FEATURE(0x2B23),
    HISTORY_DATA(0x2B28),
    RECORD_ACCESS_CONTROL_POINT(0x2B24),
    STATUS(0x2B21),
    STATUS_CHANGED(0x2B20),
    STATUS_READER_CONTROL_POINT(0x2B24);

    val uuid: UUID = BluetoothUuidHelper.createUuidFromShortUuid(shortUuid)
}


