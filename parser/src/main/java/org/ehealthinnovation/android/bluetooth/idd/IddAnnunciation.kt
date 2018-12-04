package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.FlagValue

/**
 * The characteristic indicates the Insulin Delivery Device (IDD) status changed.
 * @property annunciation The annunciation content. This field can be null if the a packet does not contain annunciation information.
 */
data class IddAnnunciation(
        val annunciation: Annunciation?
)

/**
 * @property id the instance ID of the annunciation
 * @property type the annunciation event type
 * @property status the status of annunciation notice. It indicate if an annunciation is acknowledged.
 * @property auxiliaryInformation a list of manufacturer defined information regarding the annunciation
 */
data class Annunciation(
        val id: Int,
        val type: AnnunciationType,
        val status: AnnunciationStatus,
        val auxiliaryInformation: List<AuxiliaryInformation>
)

enum class AnnunciationType(override val key: Int) : EnumerationValue {

    RESERVED_FOR_FUTURE_USE(-1),

    /**A general device fault or system error occurred (e.g., electronical or software error). */
    SYSTEM_ISSUE(0x000F),

    /**A mechanical error occurred.*/
    MECHANICAL_ISSUE(0x0033),

    /**An occlusion occurred (e.g., clogging of infusion set). */
    OCCLUSION_DETECTED(0x003C),

    /**An error related to the replacement or functioning of the reservoir occurred. */
    RESERVOIR_ISSUE(0x0055),

    /**The reservoir is empty. */
    RESERVOIR_EMPTY(0x005A),

    /**The reservoir fill level reached a defined low threshold. */
    RESERVOIR_LOW(0x0066),

    /**There is a priming issue after replacement of reservoir and/or infusion set (e.g., infusion set has not been primed). */
    PRIMING_ISSUE(0x0069),

    /**The physical connection between infusion set (including tubing and/or cannula) and the Insulin Delivery Device is incomplete. */
    INFUSION_SET_INCOMPLETE(0x0096),

    /**The infusion set (including tubing and/or cannula) is not attached to the body. */
    INFUSION_SET_DETACHED(0x0099),

    /**The Insulin Delivery Device has insufficient power to charge the device (i.e., the device cannot properly function). */
    POWER_SOURCE_INSUFFICIENT(0x00A5),

    /**The Insulin Delivery Device has no operational runtime left. The user shall be informed. */
    BATTERY_EMPTY(0x00AA),

    /**The Insulin Delivery Device has a low operational runtime (e.g., the battery charge level reached a defined low threshold, the battery is depleted, or the battery voltage is less than full strength). The user shall be informed. */
    BATTERY_LOW(0x00C3),

    /**The Insulin Delivery Device has a medium operational runtime. This annunciation should be reported at half of the operational runtime. */
    BATTERY_MEDIUM(0x00CC),

    /**The Insulin Delivery Device has a full operational runtime. This annunciation should be reported at full operational runtime. */
    BATTERY_FULL(0x00F0),

    /**The temperature is outside of the normal operating range.  */
    TEMPERATURE_OUT_OF_RANGE(0x00FF),

    /**The air pressure is outside of the normal operating range (e.g., altitude). */
    AIR_PRESSURE_OUT_OF_RANGE(0x0303),

    /**A running bolus was canceled (e.g., Insulin Delivery Device changed from run to standby mode). */
    BOLUS_CANCELED(0x030C),

    /**The temporary basal rate expired (i.e., the programmed duration is over). */
    TBR_OVER(0x0330),

    /**The temporary basal rate canceled (e.g., device changed from run to standby mode). */
    TBR_CANCELED(0x033F),

    /**The delivery reached a defined high threshold based on maximum bolus and maximum basal rates. */
    MAX_DELIVERY(0x0356),

    /**The date time of the device was never set or has been lost (e.g., due to a battery replacement). */
    DATE_TIME_ISSUE(0x0359),

    /**The Insulin Delivery Device reports a temperature measurement. */
    TEMPERATURE(0x0365);
}

enum class AnnunciationStatus(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),

    /**The status of the annunciation is undetermined. */
    UNDETERMINED(0x0F),

    /**The annunciation is currently pending and requires a user action for snoozing or confirmation. */
    PENDING(0x33),

    /**The annunciation was noticed by the user and is set to pop up again at a short time later (i.e., a snoozed annunciation is still active). The time span shall be defined by the Server application. */
    SNOOZED(0x3C),

    /**The annunciation was confirmed by the user (i.e., a confirmed annunciation is not active anymore). */
    CONFIRMED(0x55);

}

/**
 * Auxiliary information holds manufacturer defined information. The specification does not specify
 *
 */
data class AuxiliaryInformation(val rawData: Int)

enum class IddAnnunciationFlag(override val bitOffset: Int) : FlagValue {
    /**If this bit is set, the fields Annunciation Instance ID, Annunciation Type, and Annunciation Status are present (i.e., there is a currently active or already confirmed annunciation). This bit shall not be set if there has not been an annunciation yet. */
    ANNUNCIATION_PRESENT(0),

    /**If this bit is set, the AuxInfo1 field is present. */
    AUXINFO1_PRESENT(1),

    /**If this bit is set, the AuxInfo2 field is present. */
    AUXINFO2_PRESENT(2),

    /**If this bit is set, the AuxInfo3 field is present. */
    AUXINFO3_PRESENT(3),

    /**If this bit is set, the AuxInfo4 field is present. */
    AUXINFO4_PRESENT(4),

    /**If this bit is set, the AuxInfo5 field is present. */
    AUXINFO5_PRESENT(5);
}