package org.ehealthinnovation.android.bluetooth.idd.historydata

import org.ehealthinnovation.android.bluetooth.parser.StubDataReader
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Assert
import org.junit.Test

class ProfileTemplateActivatedEventParserTest {

    @Test
    fun parseEvent() {
        val eventInfo = EventInfo(EventType.PROFILE_TEMPLATE_ACTIVATED, 1, 2)
        val expecctedData = ProfileTemplateActivated(ProfileTemplateType.BASAL_RATE_PROFILE_TEMPLATE, 3, 4)
        val expectedEvent = HistoryEvent(eventInfo, expecctedData)
        val testData = StubDataReader(uint8(ProfileTemplateType.BASAL_RATE_PROFILE_TEMPLATE.key), uint8(3), uint8(4))
        Assert.assertEquals(expectedEvent, ProfileTemplateActivatedEventParser().parseEvent(eventInfo, testData))
    }
}