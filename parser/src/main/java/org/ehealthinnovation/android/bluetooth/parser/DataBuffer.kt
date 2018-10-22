package org.ehealthinnovation.android.bluetooth.parser

import android.bluetooth.BluetoothGattCharacteristic
import java.util.*

class DataBuffer: DataBufferInterface {

    var offset: Int = 0
    var gattCharacteristic: BluetoothGattCharacteristic = BluetoothGattCharacteristic(UUID.randomUUID(), BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ)

    //Use this constructor when we are writing to the data buffer
    constructor()

    //Use this constructor when we are parsing data from this buffer
    constructor(data: ByteArray){
        gattCharacteristic.value = data
    }

    override fun getNextInt(formatType: BLEDataFormatType): Int =
            gattCharacteristic.getIntValue(formatType.value, offset)?.let{ returnResult->
                offset += formatType.getFormatTypeLen()
                return returnResult
            }?: throw NullPointerException("getNextInt returns null")

    override fun getNextFloat(formatType: BLEDataFormatType): Float =
            gattCharacteristic.getFloatValue(formatType.value, offset)?.let{ returnReult->
                offset += formatType.getFormatTypeLen()
                return returnReult
            }?: throw NullPointerException("getNextFloat returns null")

    override fun getNextDataBytes(length: Int): ByteArray =
            gattCharacteristic.value?.let { rawData->
                 val toReturn = rawData.copyOfRange(offset, offset+length)
                offset += length
                return toReturn
            }?: throw NullPointerException("getNextDataBytes returns null")

}