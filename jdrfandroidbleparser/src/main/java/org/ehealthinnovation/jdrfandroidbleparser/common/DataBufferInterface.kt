package org.ehealthinnovation.jdrfandroidbleparser.common

interface DataBufferInterface {
    fun getNextInt(formatType: BLEDataFormatType):Int
    fun getNextFloat(formatType: BLEDataFormatType):Float
    fun getNextDataBytes(length: Int):ByteArray
}