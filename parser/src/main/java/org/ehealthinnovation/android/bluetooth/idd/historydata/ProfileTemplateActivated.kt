package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration

/**
 * Event data for [ProfileTemplateActivatedEvent]
 * @property type the type of template being activated
 * @property oldTemplateNumber the previous profile template number
 * @property newTemplateNumber the new profile template number to activate
 */
data class ProfileTemplateActivated(
        val type: ProfileTemplateType,
        val oldTemplateNumber: Int,
        val newTemplateNumber: Int
)

/**
 * This class is used by [ProfileTemplateActivated] event data to indicate the type of profile
 * template being activated.
 */
enum class ProfileTemplateType(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    UNDETERMINED(0x0F),
    BASAL_RATE_PROFILE_TEMPLATE(0x33),
    ISF_PROFILE_TEMPLATE(0x3C),
    I2CHO_RATIO_PROFILE_TEMPLATE(0x55),
    TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE(0x5A);
}

internal class ProfileTemplateActivatedEventParser : HistoryEventDataParser<ProfileTemplateActivated>() {

    override fun readData(dataReader: DataReader): ProfileTemplateActivated {
        val type = readEnumeration(dataReader.getNextInt(IntFormat.FORMAT_UINT8), ProfileTemplateType::class.java, ProfileTemplateType.RESERVED_FOR_FUTURE_USE)
        val oldTemplateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val newTemplateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        return ProfileTemplateActivated(type, oldTemplateNumber, newTemplateNumber)
    }
}

