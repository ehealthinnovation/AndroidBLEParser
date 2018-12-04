package org.ehealthinnovation.android.bluetooth.glucose

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.ehealthinnovation.android.bluetooth.parser.*
import org.junit.Assert
import org.junit.Test
import java.util.*

class GlucoseContextParserTest {

    @Test
    fun canParseSmokeTest(){
        testMatcherForSpecificBluetoothUuid("00002A34-0000-1000-8000-00805F9B34FB", GlucoseContextParser()::canParse)
    }

    @Test
    fun testParsePacketEmpty() {
        val testPacket = StubDataReader(
                uint8(0x0),
                uint16(123)
        )
        val expectedOutput = GlucoseContext(123, null, null, null, null, null, null, null)

        val mockPackage = mockCharacteristicPacket(testPacket)
        val actualOutput = (GlucoseContextParser().parse(mockPackage))

        Assert.assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun testParsePacketAllFields() {
        val testPacket = StubDataReader(
                uint8(0b11111111),
                uint16(123), //sequence number
                uint8(0), //extended flag
                uint8(CarbohydrateId.LUNCH.key),
                sfloat(1.2f),//carbohydrate
                uint8(Meal.PREPRANDIAL.key), //meal
                uint8(Tester.SELF.key, Health.NOT_AVAILABLE.key),//tester and health
                uint16(45),
                uint8(67), //exercise information
                uint8(MedicationId.RAPID_ACTING_INSULIN.key), //medication
                sfloat(9.8f),
                sfloat(7.6f)//hba1c
        )
        val expectedOutput = GlucoseContext(123,
                Carbohydrate(CarbohydrateId.LUNCH, 1.2f, Unit.KG),
                Meal.PREPRANDIAL,
                Tester.SELF,
                Health.NOT_AVAILABLE,
                ExerciseInfo(45, 67),
                Medication(MedicationId.RAPID_ACTING_INSULIN, 9.8f, Unit.L),
                7.6f)


        val mockPackage = mockCharacteristicPacket(testPacket)
        val actualOutput = (GlucoseContextParser().parse(mockPackage))

        Assert.assertEquals(expectedOutput, actualOutput)

    }

    private fun mockCharacteristicPacket(dataReader: DataReader): CharacteristicPacket {
        val mockPackage = mock<CharacteristicPacket>()
        whenever(mockPackage.readData()).thenReturn(dataReader)
        return mockPackage
    }

    @Test
    fun testParsePacketSomeFields() {
        val testPacket = StubDataReader(
                uint8(0b01010101),
                uint16(123), //sequence number
                uint8(CarbohydrateId.LUNCH.key),
                sfloat(1.2f),//carbohydrate
                uint8(Tester.SELF.key, Health.NOT_AVAILABLE.key),//tester and health
                uint8(MedicationId.RAPID_ACTING_INSULIN.key), //medication
                sfloat(9.8f),
                sfloat(7.6f)//hba1c
        )
        val expectedOutput = GlucoseContext(123,
                Carbohydrate(CarbohydrateId.LUNCH, 1.2f, Unit.KG),
                null,
                Tester.SELF,
                Health.NOT_AVAILABLE,
                null,
                Medication(MedicationId.RAPID_ACTING_INSULIN, 9.8f, Unit.KG),
                7.6f)

        val mockPackage = mockCharacteristicPacket(testPacket)
        val actualOutput = (GlucoseContextParser().parse(mockPackage))

        Assert.assertEquals(expectedOutput, actualOutput)

    }


    @Test
    fun testReadFlags() {
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = true, C3 = false, C4 = false, C5 = false, C6 = false, C7 = false, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = false, C3 = true, C4 = false, C5 = false, C6 = false, C7 = false, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1 shl 1))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = false, C3 = false, C4 = true, C5 = false, C6 = false, C7 = false, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1 shl 2))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = false, C3 = false, C4 = false, C5 = true, C6 = false, C7 = false, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1 shl 3))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = false, C3 = false, C4 = false, C5 = false, C6 = true, C7 = false, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1 shl 4))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = false, C3 = false, C4 = false, C5 = false, C6 = false, C7 = true, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1 shl 6))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = false, C2 = false, C3 = false, C4 = false, C5 = false, C6 = false, C7 = false, C8 = false, C9 = true),
                readFlags(StubDataReader(uint8(1 shl 5))))
        Assert.assertEquals(
                ContextFlagsConditions(C1 = true, C2 = false, C3 = false, C4 = false, C5 = false, C6 = false, C7 = false, C8 = true, C9 = false),
                readFlags(StubDataReader(uint8(1 shl 7))))
    }

    @Test
    fun readExtendedFlagsTest() {
        Assert.assertEquals(EnumSet.noneOf(ExtendedFlags::class.java), readExtendedFlags(StubDataReader(uint8(0))))
    }

    @Test
    fun readMealTest() {
        Assert.assertEquals(Meal.PREPRANDIAL, readMeal(StubDataReader(uint8(1))))
    }

    @Test
    fun readCarbohydrateTest() {

        Assert.assertEquals(Carbohydrate(CarbohydrateId.BREAKFAST, 3.4f, Unit.KG),
                readCarboHydrateInfo(StubDataReader(uint8(CarbohydrateId.BREAKFAST.key), sfloat(3.4f))))
    }

    @Test
    fun readeTesterAndHealthTest() {
        Assert.assertEquals(Pair(Tester.SELF, Health.NO_HEALTH_ISSUES), readTesterAndHealth(StubDataReader(uint8(1, 5))))
        Assert.assertEquals(Pair(Tester.TESTER_VALUE_NOT_AVAILABLE, Health.NO_HEALTH_ISSUES), readTesterAndHealth(StubDataReader(uint8(15, 5))))
    }

    @Test
    fun readExcerciseInfoTest() {
        Assert.assertEquals(ExerciseInfo(12, 34), readExerciseInfo(StubDataReader(uint16(12), uint8(34))))
    }

    @Test
    fun readMedicationTest() {
        Assert.assertEquals(Medication(MedicationId.RAPID_ACTING_INSULIN, 12.3f, Unit.KG), readMedicationInfo(true, StubDataReader(uint8(MedicationId.RAPID_ACTING_INSULIN.key), sfloat(12.3f))))
        Assert.assertEquals(Medication(MedicationId.RAPID_ACTING_INSULIN, 12.3f, Unit.L), readMedicationInfo(false, StubDataReader(uint8(MedicationId.RAPID_ACTING_INSULIN.key), sfloat(12.3f))))
    }

    @Test
    fun readHbA1cTest() {
        Assert.assertEquals(12.3f, readHbA1c(StubDataReader(sfloat(12.3f))))
    }


}