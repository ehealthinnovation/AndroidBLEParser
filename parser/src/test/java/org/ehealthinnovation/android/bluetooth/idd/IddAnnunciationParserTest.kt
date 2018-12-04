package org.ehealthinnovation.android.bluetooth.idd

import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test

class IddAnnunciationParserTest {

    @Test
    fun canParse() {
        testMatcherForSpecificBluetoothUuid("00002B22-0000-1000-8000-00805F9B34FB", IddAnnunciationParser()::canParse)
    }

    @Test
    fun parse() {
        val testDataReader = MockCharacteristicPacket.mockPacketForRead(
                uint8(0x07),
                uint16(1),
                uint16(AnnunciationType.BATTERY_LOW.key),
                uint16(AnnunciationStatus.PENDING.key),
                uint16(23),
                uint16(34)
        )
        val expectedAnnunciation = IddAnnunciation(Annunciation(1, AnnunciationType.BATTERY_LOW, AnnunciationStatus.PENDING, arrayListOf(AuxiliaryInformation(23), AuxiliaryInformation(34))))
        val actualOutput = IddAnnunciationParser().parse(testDataReader)
        Assert.assertEquals(expectedAnnunciation, actualOutput)

    }

    @Test
    fun readAnnunciation() {
        val testDataReaderSomeFlags = IddAnnunciationParser.Flags(true, true, true, false, false, false)
        val testDataReader = StubDataReader(
                uint16(1),
                uint16(AnnunciationType.BATTERY_LOW.key),
                uint16(AnnunciationStatus.PENDING.key),
                uint16(23),
                uint16(34)
        )
        val expectedAnnunciation = Annunciation(1, AnnunciationType.BATTERY_LOW, AnnunciationStatus.PENDING, arrayListOf(AuxiliaryInformation(23), AuxiliaryInformation(34)))
        val actualOutput = IddAnnunciationParser().readAnnunciation(testDataReaderSomeFlags, testDataReader)
        Assert.assertEquals(expectedAnnunciation, actualOutput)
    }

    @Test
    fun readAuxiliaryInformationList() {
        val testDataReaderSomeFlags = IddAnnunciationParser.Flags(true, true, true, false, false, false)
        val testAuxiliaryBuffer = StubDataReader(uint16(23), uint16(34))
        val output = IddAnnunciationParser().readAuxiliaryInformationList(testDataReaderSomeFlags, testAuxiliaryBuffer)
        Assert.assertEquals(23, output[0].rawData)
        Assert.assertEquals(34, output[1].rawData)
    }

    @Test
    fun readNextAuxiliaryInformation() {
        val testAuxiliaryBuffer = StubDataReader(uint16(23))
        val expectedAuxiliaryInformation = AuxiliaryInformation(23)
        Assert.assertEquals(expectedAuxiliaryInformation.rawData, IddAnnunciationParser().readNextAuxiliaryInformation(testAuxiliaryBuffer).rawData)
    }

    @Test
    fun readFlag() {
        val testDataReaderNoFlags = StubDataReader(uint8(0))
        val expectOutput1 = IddAnnunciationParser.Flags(false, false, false, false, false, false)
        Assert.assertEquals(expectOutput1, IddAnnunciationParser().readFlag(testDataReaderNoFlags))

        val testDataReaderSomeFlags = StubDataReader(uint8(0x11))
        val expectOutput2 = IddAnnunciationParser.Flags(true, false, false, false, true, false)
        Assert.assertEquals(expectOutput2, IddAnnunciationParser().readFlag(testDataReaderSomeFlags))

        val testDataReaderFullFlags = StubDataReader(uint8(0xFF))
        val expectOutput3 = IddAnnunciationParser.Flags(true, true, true, true, true, true)
        Assert.assertEquals(expectOutput3, IddAnnunciationParser().readFlag(testDataReaderFullFlags))
    }
}