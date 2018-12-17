package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Contains a set of functions to compose a [TbrAdjustmentOperand]] into a data buffer
 */
class SetTbrAdjustmentOperandComposer {

    /**
     * Call this function with a valid [TbrAdjustmentOperand], and it will be serialized and written into
     * [DataWriter]
     */
    internal fun composeOperand(operand: TbrAdjustmentOperand, dataWriter: DataWriter){
        val flags =  getFlagSet(operand)
        dataWriter.putInt(writeEnumFlagsToInteger(flags), IntFormat.FORMAT_UINT8)
        dataWriter.putInt(operand.type.key, IntFormat.FORMAT_UINT8)
        dataWriter.putFloat(operand.value, -1, FloatFormat.FORMAT_SFLOAT)
        dataWriter.putInt(operand.duration, IntFormat.FORMAT_UINT16)
        if (operand.templateNumber != null){
            dataWriter.putInt(operand.templateNumber, IntFormat.FORMAT_UINT8)
        }
        if (operand.deliveryContext != null){
            dataWriter.putInt(operand.deliveryContext.key, IntFormat.FORMAT_UINT8)
        }
    }

    internal fun getFlagSet(operand: TbrAdjustmentOperand) : EnumSet<Flag>{
        val output = EnumSet.noneOf(Flag::class.java)
        if (operand.overrideCurrentTbr) output.add(Flag.CHANGE_TBR)
        if (operand.deliveryContext != null) output.add(Flag.DELIVERY_CONTEXT_PRESENT)
        if (operand.templateNumber != null) output.add(Flag.TEMPLATE_NUMBER_PRESENT)
        return  output
    }



    internal enum class Flag(override val bitOffset: Int) : FlagValue {
        /**If this bit is set, the TBR Template Number field is present. */
        TEMPLATE_NUMBER_PRESENT(0),
        /**If this bit is set, the TBR Delivery Context field is present. */
        DELIVERY_CONTEXT_PRESENT(1),
        /**If this bit is set, a currently active TBR shall be completely overwritten with the changed settings; otherwise a new TBR shall be activated (i.e., the changed settings are new settings and are not based on the settings of the currently active TBR). */
        CHANGE_TBR(2);
    }
}

/** Operand for [SetTbrAdjustment] command
 * @property overrideCurrentTbr  If true, a currently active TBR shall be completely overwritten with the changed settings; otherwise a new TBR shall be activated (i.e., the changed settings are new settings and are not based on the settings of the currently active TBR).
 * @property type the tbr type
 * @property value If the TBR is absolute (i.e., TBR Type is set to “Absolute”), the TBR Adjustment Value field contains the temporary basal rate as the absolute value in IU/h. If the TBR is relative (i.e., TBR Type is set to “Relative”), the TBR Adjustment Value field contains a dimensionless scaling factor.
 * @property duration the programmed duration for the TBR
 * @property templateNumber The default value is null. If set to a number, it indicates the template number to use in setting a Tbr
 * @property deliveryContext The default value is null. If set to a enum value, it indicates the Delivery context of the current TBR
 */
data class TbrAdjustmentOperand(
        val overrideCurrentTbr: Boolean,
        val type: TbrType,
        val value: Float,
        val duration: Int,
        val templateNumber: Int? = null,
        val deliveryContext: TbrDeliveryContext? = null
)

enum class TbrDeliveryContext(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    /**The delivery context is undetermined. */
    UNDETERMINED(0x0F),
    /**The TBR was initiated directly on the Insulin Delivery Device (e.g., the user triggers a TBR directly on the device). */
    DEVICE_BASED(0x33),
    /**The TBR was initiated via a remote control (i.e., an external device). For example, the user triggers a TBR via a remote control. */
    REMOTE_CONTROL(0x3C),
    /**The TBR was initiated by an AP Controller (i.e., an external automated device) as part of an APDS. */
    AP_CONTROLLER(0x55);
}