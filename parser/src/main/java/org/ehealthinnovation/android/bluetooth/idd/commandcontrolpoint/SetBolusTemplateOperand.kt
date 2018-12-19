package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Storing a bolus template
 * @property number bolus template number
 * @property bolus the bolus information
 * @property isCorrectionBolus set to true if the reason for the bolus is the correction of a high blood glucose level.
 * @property isMealBolus set to true if the reason for the bolus is to cover the intake of food.
 * @property delayMinute if not null, it set the delay between before a bolus is active
 */
data class BolusTemplate(
        val number: Int,
        val bolus: Bolus,
        val isCorrectionBolus: Boolean = false,
        val isMealBolus: Boolean = false,
        val delayMinute: Int? = null
)

/**
 * The response to [GetBolusTemplate]
 * @property template the bolus template
 */
data class GetBolusTemplateResponse(
        val template: BolusTemplate
) : IddCommandControlResponse()

/**
 * Operand for [SetBolusTemplate] command
 * @property template the bolus template to set
 */
data class SetBolusTemplateOperand(
        val template: BolusTemplate
) : CommandControlOperand()

/**
 * Parser for parsing the get bolus template response
 */
class GetBolusTemplateResponseParser {

    internal fun readGetBolusTemplateResponse(dataReader: DataReader): GetBolusTemplateResponse {
        val templateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val bolus = readBolus(dataReader)
        val isMealBolus = flags.contains(Flag.BOLUS_DELIVERY_REASON_MEAL)
        val isCorrectionBolus = flags.contains(Flag.BOLUS_DELIVERY_REASON_CORRECTION)
        val delayMinute =
                if (flags.contains(Flag.BOLUS_DELAY_TIME_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null

        return GetBolusTemplateResponse(BolusTemplate(templateNumber, bolus, isCorrectionBolus, isMealBolus, delayMinute))
    }

    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Bolus Delay Time field is present. */
        BOLUS_DELAY_TIME_PRESENT(0),
        /**If this bit is set, the reason for the bolus is the correction of a high blood glucose level. */
        BOLUS_DELIVERY_REASON_CORRECTION(1),
        /**If this bit is set, the reason for the bolus is to cover the intake of food. */
        BOLUS_DELIVERY_REASON_MEAL(2);
    }
}

/**
 * Compose operand for [SetBolusTEmplate] command.
 */
class SetBolusTemplateComposer {
    internal fun composeOperand(operand: SetBolusTemplateOperand, dataWriter: DataWriter) {
        dataWriter.putInt(operand.template.number, IntFormat.FORMAT_UINT8)
        val flags = getFlags(operand.template)
        writeEnumFlags(flags, IntFormat.FORMAT_UINT8, dataWriter)
        writeBolus(operand.template.bolus, dataWriter)
        if (operand.template.delayMinute != null) {
            dataWriter.putInt(operand.template.delayMinute, IntFormat.FORMAT_UINT16)
        }
    }

    internal fun getFlags(input: BolusTemplate): EnumSet<GetBolusTemplateResponseParser.Flag> {
        val output = EnumSet.noneOf(GetBolusTemplateResponseParser.Flag::class.java)
        if (input.isCorrectionBolus) output.add(GetBolusTemplateResponseParser.Flag.BOLUS_DELIVERY_REASON_CORRECTION)
        if (input.isMealBolus) output.add(GetBolusTemplateResponseParser.Flag.BOLUS_DELIVERY_REASON_MEAL)
        if (input.delayMinute != null) output.add(GetBolusTemplateResponseParser.Flag.BOLUS_DELAY_TIME_PRESENT)
        return output
    }
}

/** method to read bolus from a data buffer*/
internal fun readBolus(dataReader: DataReader): Bolus {
    val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), BolusType::class.java, BolusType.RESERVED_FOR_FUTURE_USE)
    val fastAmount = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
    val extendedAmount = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
    val duration = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
    return Bolus(type, fastAmount, extendedAmount, duration)
}
