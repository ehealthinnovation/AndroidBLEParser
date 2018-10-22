package org.ehealthinnovation.android.bluetooth.parser

interface DataBufferInterface {
    fun getNextInt(formatType: BLEDataFormatType):Int
    fun getNextFloat(formatType: BLEDataFormatType):Float
    fun getNextDataBytes(length: Int):ByteArray
}