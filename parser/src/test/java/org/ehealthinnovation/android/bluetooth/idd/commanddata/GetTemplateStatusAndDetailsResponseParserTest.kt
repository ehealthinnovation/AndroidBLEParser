package org.ehealthinnovation.android.bluetooth.idd.commanddata

import org.ehealthinnovation.android.bluetooth.idd.GetTemplate
import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class GetTemplateStatusAndDetailsResponseParserTest {

    @Test
    fun parseResponseOperand() {
        val testData = StubDataReader(
                uint8(TemplateType.BASAL_RATE_PROFILE_TEMPLATE.key),
                uint8(1),
                uint8(2),
                uint8(3),
                uint8(0b001001)
                )
        val expected = GetTemplateStatusAndDetailsResponse(
                TemplateType.BASAL_RATE_PROFILE_TEMPLATE,
                1,2,3,
                listOf(TemplateConfigurationStatus(true, false), TemplateConfigurationStatus(false, true))
        )
        Assert.assertEquals(expected, GetTemplateStatusAndDetailsResponseParser().parseResponseOperand(testData))
    }

    @Test
    fun readTemplateType() {
        val testData = StubDataReader(uint8(TemplateType.BOLUS_TEMPLATE.key))
        val expected = TemplateType.BOLUS_TEMPLATE
        Assert.assertEquals(expected, GetTemplateStatusAndDetailsResponseParser().readTemplateType(testData))

    }

    @Test
    fun readConfigurationStatus() {
        val testNumberOfTemplate1 = 1
        val testData1 = StubDataReader(uint8(0x03))
        val expected1 = listOf(TemplateConfigurationStatus(true, true))
        Assert.assertEquals(expected1, GetTemplateStatusAndDetailsResponseParser().readConfigurationStatus(testNumberOfTemplate1, testData1))

        val testNumberOfTemplate2 = 4
        val testData2 = StubDataReader(uint8(0b00011011))
        val expected2 = listOf(
                TemplateConfigurationStatus(true, true),
                TemplateConfigurationStatus(false, true),
                TemplateConfigurationStatus(true, false),
                TemplateConfigurationStatus(false, false)
        )
        Assert.assertEquals(expected2, GetTemplateStatusAndDetailsResponseParser().readConfigurationStatus(testNumberOfTemplate2, testData2))


        val testNumberOfTemplate3 = 6
        val testData3 = StubDataReader(uint8(0b00011011), uint8(0b00001110))
        val expected3 = listOf(
                TemplateConfigurationStatus(true, true),
                TemplateConfigurationStatus(false, true),
                TemplateConfigurationStatus(true, false),
                TemplateConfigurationStatus(false, false),
                TemplateConfigurationStatus(false, true),
                TemplateConfigurationStatus(true, true)

        )
        Assert.assertEquals(expected3, GetTemplateStatusAndDetailsResponseParser().readConfigurationStatus(testNumberOfTemplate3, testData3))

        val testNumberOfTemplate4 = 11
        val testData4 = StubDataReader(uint8(0b00011011), uint8(0b01001110), uint8(0b00111001))
        val expected4 = listOf(
                TemplateConfigurationStatus(true, true),
                TemplateConfigurationStatus(false, true),
                TemplateConfigurationStatus(true, false),
                TemplateConfigurationStatus(false, false),
                TemplateConfigurationStatus(false, true),
                TemplateConfigurationStatus(true, true),
                TemplateConfigurationStatus(false, false),
                TemplateConfigurationStatus(true, false),
                TemplateConfigurationStatus(true, false),
                TemplateConfigurationStatus(false, true),
                TemplateConfigurationStatus(true, true)
        )
        Assert.assertEquals(expected4, GetTemplateStatusAndDetailsResponseParser().readConfigurationStatus(testNumberOfTemplate4, testData4))

    }

}