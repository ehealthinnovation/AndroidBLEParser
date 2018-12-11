package org.ehealthinnovation.android.bluetooth.idd.statusreadercontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.*
import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * This class is specifically used to parse a response from the [GetActiveBolusDelivery] command.
 * If a packet from the IDD Status Reader Control Point starts with opcode
 * [StatusReaderControlOpcode.GET_ACTIVE_BOLUS_DELIVERY_RESPONSE]
 */
class ActiveBolusDeliveryResponseParser {

    /**
     * Read and parse the data in the buffer into an [ActiveBolusDeliveryResponse]
     */
    internal fun readGetActiveBolusDeliveryResponse(data: DataReader): ActiveBolusDeliveryResponse {
        val flags = readBolusFlags(data)
        val bolus = readBolus(data)
        val delayTime = if (flags.contains(BolusFlag.DELAY_TIME_PRESENT)) data.getNextInt(IntFormat.FORMAT_UINT16) else null
        val templateNumber = if (flags.contains(BolusFlag.TEMPLATE_NUMBER_PRESENT)) data.getNextInt(IntFormat.FORMAT_UINT8) else null
        val activationType = if (flags.contains(BolusFlag.ACTIVATION_TYPE_PRESENT)) {
            readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), BolusActivationType::class.java, BolusActivationType.RESERVED_FOR_FUTURE_USE)
        } else null

        val reasons = readBolusReason(flags)

        val bolusConfig = BolusConfiguration(bolus, delayTime, templateNumber, activationType, reasons)

        return ActiveBolusDeliveryResponse(bolusConfig)
    }

    internal fun readBolusFlags(data: DataReader): EnumSet<BolusFlag> = parseFlags(
            data.getNextInt(IntFormat.FORMAT_UINT8),
            BolusFlag::class.java)

    internal fun readBolusReason(flags: EnumSet<BolusFlag>): EnumSet<BolusDeliveryReason> {
        val output = EnumSet.noneOf(BolusDeliveryReason::class.java)
        if (flags.contains(BolusFlag.DELIVERY_REASON_CORRECTION)) output.add(BolusDeliveryReason.CORRECTION)
        if (flags.contains(BolusFlag.DELIVERY_REASON_MEAL)) output.add(BolusDeliveryReason.MEAL)
        return output
    }

    internal fun readBolus(data: DataReader): Bolus {
        val id = data.getNextInt(IntFormat.FORMAT_UINT16)
        val bolusType = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), BolusType::class.java, BolusType.RESERVED_FOR_FUTURE_USE)
        val fastAmount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val extendedAmount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
        val duration = data.getNextInt(IntFormat.FORMAT_UINT16)

        return Bolus(id, bolusType, fastAmount, extendedAmount, duration)
    }


}