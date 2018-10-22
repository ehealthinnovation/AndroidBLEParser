package org.ehealthinnovation.jdrfandroidbleparser.common

enum class BLEDataFormatType( val value:Int) {

    /**
     * Characteristic value format type uint8
     */
    FORMAT_UINT8(0x11),

    /**
     * Characteristic value format type uint16
     */
    FORMAT_UINT16(0x12),

    /**
     * Characteristic value format type uint32
     */
    FORMAT_UINT32(0x14),

    /**
     * Characteristic value format type sint8
     */
    FORMAT_SINT8(0x21),

    /**
     * Characteristic value format type sint16
     */
    FORMAT_SINT16(0x22),

    /**
     * Characteristic value format type sint32
     */
    FORMAT_SINT32(0x24),

    /**
     * Characteristic value format type sfloat (16-bit float)
     */
    FORMAT_SFLOAT(0x32),

    /**
     * Characteristic value format type float (32-bit float)
     */
    FORMAT_FLOAT(0x34);

    fun getFormatTypeLen(): Int{
        return value.and(0x0F)
    }


}