package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.BolusActivationType
import org.ehealthinnovation.android.bluetooth.idd.BolusType
import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Holding a basic bolus
 * @property type the type of the bolus
 * @property fastAmountIU the fast bolus amount in IU. 0 if [BolusType.EXTENDED] is selected.
 * @property extendedAmountIU the extended bolus amount in IU, 0 if [BolusType.FAST] is selected.
 * @property durationMinute the duration of an extended bolus in Minutes. 0 if [BolusType.FAST] is selected
 */
data class Bolus(
        val type: BolusType,
        val fastAmountIU: Float,
        val extendedAmountIU: Float,
        val durationMinute: Int
)

/**
 * The configuration of bolus setting
 * @property bolus the bolus value
 * @property isCorrectionBolus set to true if the reason for the bolus is the correction of a high blood glucose level.
 * @property isMealBolus set to true if the reason for the bolus is to cover the intake of food.
 * @property delayMinute if not null, it set the delay between before a bolus is active
 * @property templateNumber if not null, it indicates the bolus template a command is used to set the bolus
 * @property activationType if not null, it provide additional information about the source of bolus configuration
 */
data class BolusConfig(
        val bolus: Bolus,
        val isCorrectionBolus: Boolean = false,
        val isMealBolus: Boolean = false,
        val delayMinute: Int? = null,
        val templateNumber: Int? = null,
        val activationType: BolusActivationType? = null
): CommandControlOperand()

/**
 * Use this composer to write an operand for [SetBolus] command into the [DataWriter] buffer
 */
class SetBolusComposer {
    internal fun compose(bolusSetting: BolusConfig, dataWriter: DataWriter) {

        val flags = getFlags(bolusSetting)
        writeEnumFlags(flags, IntFormat.FORMAT_UINT8, dataWriter)

        writeBolus(bolusSetting.bolus, dataWriter)

        if (bolusSetting.delayMinute != null) {
            dataWriter.putInt(bolusSetting.delayMinute, IntFormat.FORMAT_UINT16)
        }

        if (bolusSetting.templateNumber != null) {
            dataWriter.putInt(bolusSetting.templateNumber, IntFormat.FORMAT_UINT8)
        }

        if (bolusSetting.activationType != null) {
            dataWriter.putInt(bolusSetting.activationType.key, IntFormat.FORMAT_UINT8)
        }

    }

    internal fun getFlags(bolusSetting: BolusConfig): EnumSet<Flag> {
        val output = EnumSet.noneOf(Flag::class.java)
        if (bolusSetting.delayMinute != null) output.add(Flag.BOLUS_DELAY_TIME_PRESENT)
        if (bolusSetting.templateNumber != null) output.add(Flag.BOLUS_TEMPLATE_NUMBER_PRESENT)
        if (bolusSetting.activationType != null) output.add(Flag.BOLUS_ACTIVATION_TYPE_PRESENT)
        if (bolusSetting.isCorrectionBolus) output.add(Flag.BOLUS_DELIVERY_REASON_CORRECTION)
        if (bolusSetting.isMealBolus) output.add(Flag.BOLUS_DELIVERY_REASON_MEAL)
        return output
    }

    internal fun writeBolus(bolus: Bolus, dataWriter: DataWriter) {
        dataWriter.putInt(bolus.type.key, IntFormat.FORMAT_UINT8)
        dataWriter.putFloat(bolus.fastAmountIU, -1, FloatFormat.FORMAT_SFLOAT)
        dataWriter.putFloat(bolus.extendedAmountIU, -1, FloatFormat.FORMAT_SFLOAT)
        dataWriter.putInt(bolus.durationMinute, IntFormat.FORMAT_UINT16)
    }

    enum class Flag(override val bitOffset: Int) : FlagValue {
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
