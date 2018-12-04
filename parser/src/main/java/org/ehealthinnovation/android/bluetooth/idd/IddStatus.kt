package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.FlagValue

/**
 * Represent the Idd Status Characteristic
 */
data class IddStatus(
        val therapyControlState: TherapyControlState,
        val operationalState: OperationalState,
        val reservoirRemainingAmountIU: Float,
        val isReservoirAttached: Boolean
)

enum class TherapyControlState(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),

    /**The operational state is undetermined. */
    UNDETERMINED(0x0F),

    /**The insulin infusion therapy is stopped but the Insulin Delivery Device can still be configured (e.g., priming). */
    STOP(0x33),

    /**The insulin infusion therapy is paused (i.e., the device does not deliver insulin related to the therapy but delivers the missed amount of insulin after leaving state Pause and entering state Run). Typically the Pause state is limited to several minutes and can be used, for example, to bridge the time during a reservoir change. */
    PAUSE(0x3C),

    /**The insulin infusion therapy is running (i.e., the device delivers insulin related to the therapy). The Insulin Delivery Device cannot be configured in the Run state (e.g., priming). */
    RUN(0x55);
}

enum class OperationalState(override val key: Int) : EnumerationValue {

    RESERVED_FOR_FUTURE_USE(-1),

    /**The operational state is undetermined. */
    UNDETERMINED(0x0F),

    /**The Insulin Delivery Device is switched off and no functionality is available (i.e., no delivery and no configuration is possible).*/
    OFF(0x33),

    /**No insulin is delivered and resuming from this state is faster than from state Off (e.g., the device is being set to a state to save energy).*/
    STANDBY(0x3C),

    /**The Insulin Delivery Device prepares the insulin infusion therapy. For example, the Insulin Delivery Device rewinds the piston rod to enable the insertion of a new reservoir or performs a sniffing by detecting the position of the plunger in the reservoir (related to insulin pumps with cartridge). */
    PREPARING(0x55),

    /**The Insulin Delivery Device fills the fluidic path from the reservoir to the body with insulin (e.g., after replacement of the reservoir and/or infusion set). */
    PRIMING(0x5A),

    /**The Insulin Delivery Device waits for an interaction (e.g., waiting for the infusion set to be connected to the body after priming or waiting for a user confirmation of a reported error). */
    WAITING(0x66),

    /**The Insulin Delivery Device is ready for the insulin infusion therapy. */
    READY(0x96);
}

enum class IddStatusFlags(override val bitOffset: Int) : FlagValue {
    /**If this bit is set, If this bit is set, the reservoir is attached to the Insulin Delivery Device.*/
    RESERVOIR_ATTACHED(0);
}
