package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.*
import java.util.*

/**
 * Parser for glucose context characteristic
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_measurement_context.xml
 */
class GlucoseContextParser : CharacteristicParser<GlucoseContext> {
    override fun canParse(packet: CharacteristicPacket): Boolean {
        //todo  implement this function
        return false
    }

    override fun parse(packet: CharacteristicPacket): GlucoseContext {
        val data = packet.readData()
        val contextFlagsConditions = readFlags(data)
        val sequenceNumber = data.getNextInt(IntFormat.FORMAT_UINT16)
        //extended flag is not present in our output, since it is up to the manufacturer to define the values and meanings in this flag.

        val extendedFlags = if (contextFlagsConditions.C1) readExtendedFlags(data) else null
        val carbohydrate = if (contextFlagsConditions.C2) readCarboHydrateInfo(data) else null
        val meal = if (contextFlagsConditions.C3) readMeal(data) else null
        val (tester, health) = if (contextFlagsConditions.C4) readTesterAndHealth(data) else Pair(null, null)
        val exercise = if (contextFlagsConditions.C5) readExerciseInfo(data) else null
        val medication = if (contextFlagsConditions.C6) readMedicationInfo(contextFlagsConditions.C8, data) else null
        val hbA1c = if (contextFlagsConditions.C7) readHbA1c(data) else null

        return GlucoseContext(
                sequenceNumber,
                carbohydrate,
                meal,
                tester,
                health,
                exercise,
                medication,
                hbA1c
        )
    }
}

/**
 * Data Structure for storing the values of the context flags.
 */
internal data class ContextFlagsConditions(
        val C1: Boolean, //Condition 1: Extended flags present
        val C2: Boolean, //Condition 2: Carbohydrate info present
        val C3: Boolean, //Condition 3: Meal present
        val C4: Boolean, //Condition 4: Tester-Health info present
        val C5: Boolean, //Condition 5: Exercise duration and exercise intensity present
        val C6: Boolean, //Condition 6: medication present
        val C7: Boolean, //Condition 7: HbA1c Present
        val C8: Boolean, //Condition 8: medication units set to kilograms
        val C9: Boolean //Condition 9: medication units set to liters
)

/**
 * read in and parse the flag field of the context packet
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values contains the flag data
 * @return [ContextFlagsConditions] containing the parsed result
 */
internal fun readFlags(data: DataReader): ContextFlagsConditions {
    val flagValueHolder = data.getNextInt(IntFormat.FORMAT_UINT8)
    val flagsSet = parseFlags(flagValueHolder, ContextFlags::class.java)
    return ContextFlagsConditions(
            C1 = flagsSet.contains(ContextFlags.EXTENDED_FLAGS_PRESENT),
            C2 = flagsSet.contains(ContextFlags.CARBOHYDRATE_ID_AND_CARBOHYDRATE_PRESENT),
            C3 = flagsSet.contains(ContextFlags.MEAL_PRESENT),
            C4 = flagsSet.contains(ContextFlags.TESTER_HEALTH_PRESENT),
            C5 = flagsSet.contains(ContextFlags.EXERCISE_DURATION_AND_EXERCISE_INTENSITY_PRESENT),
            C6 = flagsSet.contains(ContextFlags.MEDICATION_ID_AND_MEDICATION_PRESENT),
            C7 = flagsSet.contains(ContextFlags.HBA1C_PRESENT),
            C8 = !flagsSet.contains(ContextFlags.MEDICATION_VALUE_UNITS),
            C9 = flagsSet.contains(ContextFlags.MEDICATION_VALUE_UNITS)
    )
}

/**
 * read in and parse the data field of the context packet
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains extended flags
 * @return An enum set of [ExtendedFlags] containing the parsed result.
 */
internal fun readExtendedFlags(data: DataReader): EnumSet<ExtendedFlags> {
    return parseFlags(data.getNextInt(IntFormat.FORMAT_UINT8), ExtendedFlags::class.java)
}


/**
 * read in and parse the data field of carbohydrate information
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains carbohydrate data
 * @return A data class of [Carbohydrate] containing the parsed result.
 */
internal fun readCarboHydrateInfo(data: DataReader): Carbohydrate {
    return Carbohydrate(
            type = readEnumeration(
                    rawValue = data.getNextInt(IntFormat.FORMAT_UINT8),
                    enumType = CarbohydrateId::class.java,
                    defaultValue = CarbohydrateId.RESERVED_FOR_FUTURE_USE),
            amount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
            unit = Unit.KG
    )
}

/**
 * read in and parse the data field of meal information
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains meal data
 * @return A data class of [Meal] containing the parsed result.
 */
internal fun readMeal(data: DataReader): Meal {
    return readEnumeration(
            rawValue = data.getNextInt(IntFormat.FORMAT_UINT8),
            enumType = Meal::class.java,
            defaultValue = Meal.RESERVED_FOR_FUTURE_USE
    )
}

/**
 * read in and parse the data field of tester and health information
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains tester and health data
 * @return A [Pair] of [Meal] and [Health] containing the parsed result.
 */
internal fun readTesterAndHealth(data: DataReader): Pair<Tester, Health> {
    val (lowerNibble, upperNibble) = readNibbles(data.getNextInt(IntFormat.FORMAT_UINT8))
    return Pair(
            readEnumeration(lowerNibble, Tester::class.java, Tester.RESERVED_FOR_FUTURE_USE),
            readEnumeration(upperNibble, Health::class.java, Health.RESERVED_FOR_FUTURE_USE)
    )
}

/**
 * read in and parse the data field of
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains exercise information
 * @return A data class of [ExerciseInfo] containing the parsed result.
 */
internal fun readExerciseInfo(data: DataReader): ExerciseInfo {
    return ExerciseInfo(
            duration = data.getNextInt(IntFormat.FORMAT_UINT16).let { durationValue ->
                if (durationValue >= 65536) {
                    Int.MAX_VALUE
                } else {
                    durationValue
                }
            },
            intensity = data.getNextInt(IntFormat.FORMAT_UINT8)
    )
}

/**
 * read in and parse the data field of medication information
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains medication information
 * @return A data class of [Medication] containing the parsed result.
 */
internal fun readMedicationInfo(C8: Boolean, data: DataReader): Medication {
    return Medication(
            type = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), MedicationId::class.java, MedicationId.RESERVED_FOR_FUTURE_USE),
            amount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
            unit = when (C8) {
                true -> Unit.KG
                false -> Unit.L
            }
    )
}

/**
 * read in and parse the data field of tester and health information
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains HbA1c
 * @return Float representing HbA1c containing the parsed result.
 */
internal fun readHbA1c(data: DataReader): Float {
    return data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
}
