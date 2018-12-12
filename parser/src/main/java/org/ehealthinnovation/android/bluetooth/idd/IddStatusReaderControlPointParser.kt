package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint.GetActiveBasalRateDeliveryResponseParser
import org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint.GetCounterResponseParser
import org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint.GetDeliveredInsulinResponseParser
import org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint.GetTotalDailyInsulinStatusResponseParser
import org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint.ActiveBolusDeliveryResponseParser
import org.ehealthinnovation.android.bluetooth.parser.*

class IddStatusReaderControlPointParser : CharacteristicParser<StatusReaderControlResponse> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        return packet.uuid == IddUuid.STATUS_READER_CONTROL_POINT.uuid
    }

    override fun parse(packet: CharacteristicPacket): StatusReaderControlResponse {
        val data = packet.readData()
        val opcode = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT16), StatusReaderControlOpcode::class.java, StatusReaderControlOpcode.RESERVED_FOR_FUTURE_USE)
        return when (opcode) {
            StatusReaderControlOpcode.RESPONSE_CODE -> readGeneralResponse(data)
            StatusReaderControlOpcode.GET_ACTIVE_BOLUS_IDS_RESPONSE -> readActiveBolusIdsResponse(data)
            StatusReaderControlOpcode.GET_COUNTER_RESPONSE -> readGetCounterResponse(data)
            StatusReaderControlOpcode.GET_DELIVERED_INSULIN_RESPONSE -> readGetInsulinDeliveredResponse(data)
            StatusReaderControlOpcode.GET_ACTIVE_BASAL_RATE_DELIVERY_RESPONSE -> readGetActiveBasalDeliveryRespoonse(data)
            StatusReaderControlOpcode.GET_TOTAL_DAILY_INSULIN_STATUS_RESPONSE -> readGetTotalDailyInsuinStatusResponse(data)
            StatusReaderControlOpcode.GET_ACTIVE_BOLUS_DELIVERY_RESPONSE -> readActiveBolusDeliveryResponse(data)
            else -> throw IllegalArgumentException("Opcode $opcode not supported by this parser")
        }
    }

    internal fun readGeneralResponse(data: DataReader): StatusReaderControlGeneralResponse {
        val requestOpcode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT16),
                StatusReaderControlOpcode::class.java,
                StatusReaderControlOpcode.RESERVED_FOR_FUTURE_USE
        )

        val responseCode = readEnumeration(
                data.getNextInt(IntFormat.FORMAT_UINT8),
                StatusReaderControlResponseCode::class.java,
                StatusReaderControlResponseCode.RESERVED_FOR_FUTURE_USE
        )

        return StatusReaderControlGeneralResponse(requestOpcode, responseCode)
    }

    internal fun readActiveBolusIdsResponse(data: DataReader): ActiveBolusIds {
        val numberOfActiveBolusIds = data.getNextInt(IntFormat.FORMAT_UINT8)
        val outputIds = arrayListOf<Int>()
        for (i in 1..numberOfActiveBolusIds) {
            outputIds.add(data.getNextInt(IntFormat.FORMAT_UINT16))
        }
        return ActiveBolusIds(outputIds)
    }
    
    internal fun readGetCounterResponse(data: DataReader): CounterResponse = GetCounterResponseParser().parseResponse(data)

    internal fun readGetInsulinDeliveredResponse(data: DataReader): DeliveredInsulinResponse = GetDeliveredInsulinResponseParser().parseResponse(data)

    internal fun readGetTotalDailyInsuinStatusResponse(data: DataReader): TotalDailyInsulinStatusResponse = GetTotalDailyInsulinStatusResponseParser().parseResponse(data)

    internal fun readActiveBolusDeliveryResponse(data: DataReader): ActiveBolusDeliveryResponse =
            ActiveBolusDeliveryResponseParser().readGetActiveBolusDeliveryResponse(data)


    internal fun readGetActiveBasalDeliveryRespoonse(data: DataReader) : ActiveBasalRateDeliveryResponse = GetActiveBasalRateDeliveryResponseParser().readActiveBasalRateDeliveryResponse(data)

}