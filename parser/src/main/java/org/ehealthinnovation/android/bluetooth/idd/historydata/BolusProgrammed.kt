package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.idd.Bolus
import org.ehealthinnovation.android.bluetooth.idd.BolusActivationType
import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.parser.*


/**
 * Data for [BolusProgrammedPart2Event]
 * @property isMealBolus set to true if the reason for the bolus is to cover the intake of food.
 * @property isCorrectionBolus set to true if the reason for the bolus is the correction of a high blood glucose level.
 * @property delayMinute if not null, it represents the delay between before a bolus is active
 * @property templateNumber if not null, it records the template number used to program a bolus
 * @property activationType if not null, it records the bolus activation method.
 *
 * @note
 * Data for [BolusProgrammedPart1Event] is [Bolus], which is not redeclared here.
 * The Bolus Programmed events should be recorded if a Bolus is programmed on the Insulin Delivery
 * Device or via a Client (i.e., procedure Set Bolus is executed on the IDD Command CP).
 */
data class BolusProgrammedConfiguration(
        val isMealBolus: Boolean,
        val isCorrectionBolus: Boolean,
        val delayMinute: Int?,
        val templateNumber: Int?,
        val activationType: BolusActivationType?
)

/**
 * method to read [Bolus] from a data buffer [DataReader]
 */
internal fun readBolus(dataReader: DataReader): Bolus {
    val id = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
    val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), BolusType::class.java, BolusType.RESERVED_FOR_FUTURE_USE)
    val fastAmount = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
    val extendedAmount = dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
    val duration = dataReader.getNextInt(IntFormat.FORMAT_UINT16)
    return Bolus(id, type, fastAmount, extendedAmount, duration)
}

internal class BolusProgrammedPart1EventParser : HistoryEventDataParser<Bolus>() {

    override fun readData(dataReader: DataReader): Bolus = readBolus(dataReader)
}


internal class BolusProgrammedPart2EventParser : HistoryEventDataParser<BolusProgrammedConfiguration>() {

    override fun readData(dataReader: DataReader): BolusProgrammedConfiguration {
        val flagRawValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val flags = parseFlags(flagRawValue, Flag::class.java)
        val isMealBolus = flags.contains(Flag.BOLUS_DELIVERY_REASON_MEAL)
        val isCorrectionBolus = flags.contains(Flag.BOLUS_DELIVERY_REASON_CORRECTION)
        val delayMinute =
                if (flags.contains(Flag.BOLUS_DELAY_TIME_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT16) else null
        val templateNumber =
                if (flags.contains(Flag.BOLUS_TEMPLATE_NUMBER_PRESENT)) dataReader.getNextInt(IntFormat.FORMAT_UINT8) else null
        val activationType =
                if (flags.contains(Flag.BOLUS_ACTIVATION_TYPE_PRESENT)) {
                    readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), BolusActivationType::class.java, BolusActivationType.RESERVED_FOR_FUTURE_USE)
                } else null

        return BolusProgrammedConfiguration(isMealBolus, isCorrectionBolus, delayMinute, templateNumber, activationType)

    }

    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the Bolus Delay Time field is present. */
        BOLUS_DELAY_TIME_PRESENT(0),
        /**If this bit is set, the Bolus Template Number field is present. */
        BOLUS_TEMPLATE_NUMBER_PRESENT(1),
        /**If this bit is set, the Bolus Activation Type field is present. */
        BOLUS_ACTIVATION_TYPE_PRESENT(2),
        /**If this bit is set, the reason for the bolus is the correction of a high blood glucose level. */
        BOLUS_DELIVERY_REASON_CORRECTION(3),
        /**If this bit is set, the reason for the bolus is to cover the intake of food. */
        BOLUS_DELIVERY_REASON_MEAL(4);
    }


}
