package org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint

import org.ehealthinnovation.android.bluetooth.idd.TbrType
import org.ehealthinnovation.android.bluetooth.parser.DataWriter
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat
import org.ehealthinnovation.android.bluetooth.parser.IntFormat


/**
 * Operand for [SetTbrTemplate] command
 *
 * @property templateNumber the template number to set
 * @property type the TBR type
 * @property value If the TBR is absolute (i.e., TBR Type is set to “Absolute”), the TBR Adjustment Value field contains the temporary basal rate as the absolute value in IU/h. If the TBR is relative (i.e., TBR Type is set to “Relative”), the TBR Adjustment Value field contains a dimensionless scaling factor.
 * @property duration the programmed duration for the TBR in minutes
 */
data class SetTbrAdjustmentTemplateOperand(
        val templateNumber: Int,
        val type: TbrType,
        val value: Float,
        val duration: Int
)

