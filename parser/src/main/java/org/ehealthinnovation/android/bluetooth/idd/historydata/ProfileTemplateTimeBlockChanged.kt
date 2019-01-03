package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.FloatFormat
import org.ehealthinnovation.android.bluetooth.parser.IntFormat

/**
 * Event data for history events recording time block changes in a profile template. It is the event
 * data for [EventType.ISF_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED], [EventType.I2CHO_RATIO_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED]
 * or [EventType.BASAL_RATE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED]
 *
 * The exact meaning/type of time block should be determined by the history event type.
 *
 * @property profileTemplateNumber the profile template number of the changed profile
 * @property timeblockNumber the number of the time block changed
 * @property duration the new duration of the time block in Minute
 * @property value the new value of the time block
 */
data class SingleValueTimeBlockChangeData(
        val profileTemplateNumber: Int,
        val timeblockNumber: Int,
        val duration: Int,
        val value: Float
)

/**
 * Event data for history events recording time block changes in a profile template
 *
 * It is the event data for [EventType.TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_TIME_BLOCK_CHANGED].
 *
 * @property profileTemplateNumber the profile template number of the changed profile
 * @property timeblockNumber the number of the time block changed
 * @property duration the new duration of the time block in Minute
 * @property lowerValue the lower bound of new value of the time block
 * @property higherValue the higher bound of new value of the time block
 */
data class PairedValueTimeBlockChangeData(
        val profileTemplateNumber: Int,
        val timeblockNumber: Int,
        val duration: Int,
        val lowerValue: Float,
        val higherValue: Float
)


internal class SingleValueProfileTemplateTimeBlockChangedEventParser : HistoryEventDataParser<SingleValueTimeBlockChangeData>() {
    override fun readData(dataReader: DataReader): SingleValueTimeBlockChangeData =
            SingleValueTimeBlockChangeData(
                    dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                    dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                    dataReader.getNextInt(IntFormat.FORMAT_UINT16),
                    dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
            )
}

internal class PairedValueProfileTemplateTimeBlockChangedEventParser : HistoryEventDataParser<PairedValueTimeBlockChangeData>() {
    override fun readData(dataReader: DataReader): PairedValueTimeBlockChangeData =
            PairedValueTimeBlockChangeData(
                    dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                    dataReader.getNextInt(IntFormat.FORMAT_UINT8),
                    dataReader.getNextInt(IntFormat.FORMAT_UINT16),
                    dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT),
                    dataReader.getNextFloat(FloatFormat.FORMAT_SFLOAT)
            )
}
