package org.ehealthinnovation.android.bluetooth.idd.commanddata

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.parser.DataReader
import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.IntFormat
import org.ehealthinnovation.android.bluetooth.parser.readEnumeration
import java.util.*
import kotlin.math.ceil

/**
 * This structure holds the response to [Opcode.GET_TEMPLATE_STATUS_AND_DETAILS] for a specific type [templateType].
 *
 * @property templateType the type of template under query
 *
 * @property startingTemplateNumber the template number of the first template within the specified [templateType].
 * In the specification, all template types' numbers do not overlap.
 *
 * @property numberOfTemplates the number of templates of the specified type.
 *
 * @property maxNumberOfSupportedTimeBlocks the maximum time blocks that can be contained in a profile template
 *
 * @property configurationStatus the configuration status of all the templates of the kind [templateType]
 */
data class GetTemplateStatusAndDetailsResponse(
        val templateType: TemplateType,
        val startingTemplateNumber: Int,
        val numberOfTemplates: Int,
        val maxNumberOfSupportedTimeBlocks: Int,
        val configurationStatus: List<TemplateConfigurationStatus>
) : IddCommandDataResponse()

/**
 * The configuration status of a profile template
 * @property configurable if true, the template is configurable.
 * @property configured  if true, the template is configured, and contains meaningful data.
 */
data class TemplateConfigurationStatus(
        val configurable: Boolean,
        val configured: Boolean
)

enum class TemplateType constructor(override val key: Int) : EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    UNDETERMINED(0x0F),
    BASAL_RATE_PROFILE_TEMPLATE(0x33),
    TBR_TEMPLATE(0x3C),
    BOLUS_TEMPLATE(0x55),
    ISF_PROFILE_TEMPLATE(0x5A),
    I2CHO_RATIO_PROFILE_TEMPLATE(0x66),
    TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE(0x96);
}

/**
 * Use an instance of this class to parse operand of [Opcode.GET_TEMPLATE_STATUS_AND_DETAILS_RESPONSE].
 * Call [parseResponseOperand] with a [DataReader] containing the operand binaries into a [GetTemplateStatusAndDetailsResponse]
 * object
 */
class GetTemplateStatusAndDetailsResponseParser {

    /**
     * The main method for parsing binary data into [GetTemplateStatusAndDetailsResponse] object.
     */
    internal fun parseResponseOperand(dataReader: DataReader): GetTemplateStatusAndDetailsResponse {
        val templateType = readTemplateType(dataReader)
        val startingTemplateNumber = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val numberOfTemplates = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val maxNumberOfSupportedTimeBlocks = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
        val configurationStatus = readConfigurationStatus(numberOfTemplates, dataReader)
        return GetTemplateStatusAndDetailsResponse(templateType, startingTemplateNumber, numberOfTemplates, maxNumberOfSupportedTimeBlocks, configurationStatus)


    }

    /**
     * Helper method to read the template type
     */
    internal fun readTemplateType(dataReader: DataReader): TemplateType = readEnumeration(
            dataReader.getNextInt(IntFormat.FORMAT_UINT8),
            TemplateType::class.java,
            TemplateType.RESERVED_FOR_FUTURE_USE
    )

    /**
     * Helper method to read the configuration status list from [DataReader].
     *
     * Status data for each template consists of 2 bits. Least significant bit
     * for [TemplateConfigurationStatus.configurable], most significant bit for
     * [TemplateConfigurationStatus.configured].
     *
     * The status bit pairs are packed into octets, with one octet contains at most 4
     * bit pair. If there aren't enough bit pairs to fill up a whole byte, the unused
     * bits are set to zero.
     *
     * For example, If [numberOfTemplates] is 6, the resulting bytes in the [DataReader]
     * will be byte0: 0b44332211, byte1: 0b00006655 (the numbers in the byte example are
     * the template id).
     *
     * @param numberOfTemplates the number of template status contained in the buffer.
     *
     */
     internal fun readConfigurationStatus(numberOfTemplates: Int, dataReader: DataReader): List<TemplateConfigurationStatus> {
        val outputList = mutableListOf<TemplateConfigurationStatus>()

        val numberOfByteToRead = ceil(numberOfTemplates / 4f).toInt()
        val configurationBits = readBytesIntoBitSet(numberOfByteToRead, dataReader)

        for (i in 0 until numberOfTemplates){
            outputList.add(extractBitPair(configurationBits, i))
        }
        return outputList
    }

    /**
     * Extract the bit pair specified by [offset] from [inputBitSet], and creates a [TemplateConfigurationStatus]
     *
     * @param inputBitSet the bit set containing all the bit pairs
     * @param offset the index offset of the bit pair to extract, offset starts from 0 to (the number of bit pairs -1)
     */
    private fun extractBitPair(inputBitSet: BitSet, offset: Int): TemplateConfigurationStatus {
        return TemplateConfigurationStatus(inputBitSet[offset * 2], inputBitSet[offset * 2 + 1])
    }

    /**
     * Read [numberOfByte] from [DataReader] and convert them into a [BitSet]
     */
    private fun readBytesIntoBitSet(numberOfByte: Int, dataReader: DataReader): BitSet {
        val bytes = ByteArray(numberOfByte)
        for (i in 0 until numberOfByte) {
            val byteValue = dataReader.getNextInt(IntFormat.FORMAT_UINT8)
            bytes[i] = byteValue.toByte()
        }
        return BitSet.valueOf(bytes)
    }
}
