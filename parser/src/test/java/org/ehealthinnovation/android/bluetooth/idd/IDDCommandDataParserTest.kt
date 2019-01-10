package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.Opcode
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.RangedTimeBlock
import org.ehealthinnovation.android.bluetooth.idd.commandcontrolpoint.SingleValueTimeBlock
import org.ehealthinnovation.android.bluetooth.idd.commanddata.*
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class IDDCommandDataParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B26-0000-1000-8000-00805F9B34FB", IddCommandDataParser()::canParse)
    }

    @Test
    fun parseReadBasalRateProfileTemplateResponse() {
        val expectedBasalProfileTemplateResponse = ReadBasalProfileTemplateResponse(ReadSingleValueProfileTemplateResponseOperand(1, 2,
                SingleValueTimeBlock(3, 4f),
                SingleValueTimeBlock(5, 6f),
                SingleValueTimeBlock(7, 8f)
        ))
        val testData = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.READ_BASAL_RATE_PROFILE_TEMPLATE_RESPONSE.key),
                uint8(0b011), uint8(1), uint8(2),
                uint16(3), sfloat(4f),
                uint16(5), sfloat(6f),
                uint16(7), sfloat(8f))

        Assert.assertEquals(expectedBasalProfileTemplateResponse, IddCommandDataParser().parse(testData))
    }

    @Test
    fun parseResponseOperandI2CHORateProfileTemplateResponse() {
        val expectedI2CHOProfileTemplateResponse = ReadI2CHOProfileTemplateResponse(
                ReadSingleValueProfileTemplateResponseOperand(
                        1, 2,
                        SingleValueTimeBlock(3, 4f),
                        SingleValueTimeBlock(5, 6f),
                        null))

        val testData = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.READ_I2CHO_RATIO_PROFILE_TEMPLATE_RESPONSE.key),
                uint8(0b001), uint8(1), uint8(2),
                uint16(3), sfloat(4f),
                uint16(5), sfloat(6f))

        Assert.assertEquals(expectedI2CHOProfileTemplateResponse, IddCommandDataParser().parse(testData))
    }

    @Test
    fun parseResponseOperandISFProfileTemplateResponse() {
        val expectedISFProfileTemplateResponse = ReadISFProfileTemplateResponse(
                ReadSingleValueProfileTemplateResponseOperand(
                        1, 2,
                        SingleValueTimeBlock(3, 4f),
                        null,
                        null))

        val testData = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.READ_ISF_PROFILE_TEMPLATE_RESPONSE.key),
                uint8(0b000), uint8(1), uint8(2),
                uint16(3), sfloat(4f))

        Assert.assertEquals(expectedISFProfileTemplateResponse, IddCommandDataParser().parse(testData))
    }

    @Test
    fun parseResponseOperandTargetGlucoseRangeProfileTemplateResponse() {
        val expectedTargetGlucoseRangeProfileTemplateResponse = ReadTargetGlucoseRangeProfileTemplateResponse(
                ReadRangedValuesProfileTemplateResponseOperand(
                        1, 2,
                        RangedTimeBlock(3, 4f, 5f),
                        null))

        val testData = MockCharacteristicPacket.mockPacketForRead(uint16(Opcode.READ_TARGET_GLUCOSE_RANGE_PROFILE_TEMPLATE_RESPONSE.key),
                uint8(0b000), uint8(1), uint8(2),
                uint16(3), sfloat(4f), sfloat(5f))

        Assert.assertEquals(expectedTargetGlucoseRangeProfileTemplateResponse, IddCommandDataParser().parse(testData))
    }

    @Test
    fun parseGetTemplateStatusAndDetailsResponse() {
        val testData = MockCharacteristicPacket.mockPacketForRead(
                uint16(Opcode.GET_TEMPLATE_STATUS_AND_DETAILS_RESPONSE.key),
                uint8(TemplateType.BASAL_RATE_PROFILE_TEMPLATE.key),
                uint8(1),
                uint8(2),
                uint8(3),
                uint8(0b001001)
        )
        val expected = GetTemplateStatusAndDetailsResponse(
                TemplateType.BASAL_RATE_PROFILE_TEMPLATE,
                1, 2, 3,
                listOf(TemplateConfigurationStatus(true, false), TemplateConfigurationStatus(false, true))
        )
        Assert.assertEquals(expected, IddCommandDataParser().parse(testData))
    }
}