package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.FlagValue
import java.util.*


/**
 * Response to [GetActiveBolusIds]
 * @property ids A list of all currently active bolus ids.
 */
data class ActiveBolusIds(
        val ids: List<Int>
) : StatusReaderControlResponse()


/**
 * The basic information for a bolus
 * @property id the id of a programmed bolus
 * @property type the type of the bolus
 * @property fastAmountIU the fast bolus amount in IU
 * @property extendedAmountIU the extended amount in IU
 * @property durationMinute the duration of the extended amount in minutes
 */
data class Bolus(
        val id: Int,
        val type: BolusType,
        val fastAmountIU: Float,
        val extendedAmountIU: Float,
        val durationMinute: Int
)

/**
 * The configuration of a bolus. It includes the programming configuration of a bolus
 * @property bolus a bolus to be programmed
 * @property delayMinute If not null, it indicate the minute delay between programming and activating a bolus
 * @property templateNumber If not null, it indicates the current configuration is set through a template with the stated number
 * @property activationType If not null, it states the activation type
 * @property deliveryReason A set containing all the reasons related to the current bolus programming.
 */
data class BolusConfiguration(
        val bolus: Bolus,
        val delayMinute: Int?,
        val templateNumber: Int?,
        val activationType: BolusActivationType?,
        val deliveryReason: EnumSet<BolusDeliveryReason>
)

/**
 * The response to [GetActiveBolusDelivery]
 * @property activeBolus contains the information of the queried active bolus.
 */
data class ActiveBolusDeliveryResponse(
        val activeBolus: BolusConfiguration
) : StatusReaderControlResponse()

enum class BolusFlag(override val bitOffset: Int) : FlagValue {
    /**If this bit is set, the Bolus Delay Time field is present. */
    DELAY_TIME_PRESENT(0),
    /**If this bit is set, the Bolus Template Number field is present. */
    TEMPLATE_NUMBER_PRESENT( 1),
    /**If this bit is set, the Bolus Activation Type field is present. */
    ACTIVATION_TYPE_PRESENT( 2),
    /**If this bit is set, the reason for the bolus is the correction of a high blood glucose level. */
    DELIVERY_REASON_CORRECTION( 3),
    /**If this bit is set, the reason for the bolus is to cover the intake of food. */
    DELIVERY_REASON_MEAL( 4);
}

enum class BolusType(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),

    UNDETERMINED(0x0F),
    FAST(0x33),
    EXTENDED(0x3C),
    MULTIWAVE(0x55);
}

enum class BolusActivationType(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),

    /**The activation type is undetermined. */
    UNDETERMINED(0x0F),
    /**The bolus was defined by the user. */
    MANUAL_BOLUS(0x33),
    /**The bolus was recommended by a calculation algorithm (e.g., a bolus calculator) and confirmed by the user. */
    RECOMMENDED_BOLUS(0x3C),
    /**The user changed a recommended bolus. */
    MANUALLY_CHANGED_RECOMMENDED_BOLUS(0x55),
    /**The bolus was activated without user interaction (e.g., by an APDS)*/
    COMMANDED_BOLUS(0x5A);
}

enum class BolusDeliveryReason{
    CORRECTION,
    MEAL
}