package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class IddStatusParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B21-0000-1000-8000-00805F9B34FB", IddStatusParser()::canParse)

    }

    @Test
    fun parse() {
        val testPacket = MockCharacteristicPacket.mockPacketForRead(
                uint8(TherapyControlState.RUN.key),
                uint8(OperationalState.READY.key),
                sfloat(2.3f),
                uint8(1)
        )
        val expectedOutput = IddStatus(
                TherapyControlState.RUN,
                OperationalState.READY,
                2.3f,
                true
        )
        Assert.assertEquals(expectedOutput, IddStatusParser().parse(testPacket))

    }

    @Test
    fun readFlagsForReservoirState() {
        val testReader = StubDataReader(uint8(1))
        Assert.assertTrue(IddStatusParser().readFlagsForReservoirState(testReader))

        val testReader2 = StubDataReader(uint8(0))
        Assert.assertFalse(IddStatusParser().readFlagsForReservoirState(testReader2))
    }
}