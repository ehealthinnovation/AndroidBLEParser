package org.ehealthinnovation.android.bluetooth.parser


/**
 * The day time offset constants defined in the Bluetooth specification
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.dst_offset.xml
 */
enum class DstOffset(override val key: Int):EnumerationValue {
    STANDARD_TIME(0),
    HALF_AN_HOUR_DAYLIGHT_TIME(2),
    DAYLIGHT_TIME(4),
    DOUBLE_DAYLIGHT_TIME(8),
    DST_IS_NOT_KNOWN(255),
    RESERVE_FOR_FUTURE_USE(-1);
}

internal fun readDst(data: DataReader): DstOffset = readEnumeration(
        rawValue = data.getNextInt(IntFormat.FORMAT_UINT8),
        enumType = DstOffset::class.java,
        defaultValue = DstOffset.RESERVE_FOR_FUTURE_USE)