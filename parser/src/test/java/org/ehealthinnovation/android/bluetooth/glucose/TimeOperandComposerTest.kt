package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.BluetoothDateTime
import org.ehealthinnovation.android.bluetooth.parser.StubDataWriter
import org.ehealthinnovation.android.bluetooth.parser.uint16
import org.ehealthinnovation.android.bluetooth.parser.uint8
import org.junit.Test

class TimeOperandComposerTest {
    private lateinit var mockRacpTimeComposer: RacpTimeComposer
    private lateinit var testDataWriter: StubDataWriter
    private lateinit var startDateTime: BluetoothDateTime
    private lateinit var endDateTime: BluetoothDateTime


    @Test
    fun composeTimeRangeOperandSmokeTest() {
        testDataWriter = StubDataWriter(
                uint8(Operator.WITHIN_RANGE_OF_INCLUSIVE.key),
                uint8(Filter.USER_FACING_TIME.key),
                uint16(2018), uint8(1), uint8(2), uint8(3), uint8(4), uint8(5),
                uint16(2018), uint8(5), uint8(4), uint8(3), uint8(2), uint8(1)
        )

        mockRacpTimeComposer = RacpTimeComposer()
        startDateTime = BluetoothDateTime(2018, 1, 2, 3, 4, 5)
        endDateTime = BluetoothDateTime(2018, 5, 4, 3, 2,1)

        mockRacpTimeComposer.composeTimeRangeOperand(startDateTime, endDateTime, testDataWriter)
        testDataWriter.checkWriteComplete()
    }


}