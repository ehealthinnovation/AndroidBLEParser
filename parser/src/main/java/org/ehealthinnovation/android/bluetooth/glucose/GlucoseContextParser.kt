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
        val extendedFlags = readExtendedFlags(contextFlagsConditions.C1, data)
        val carbohydrate = readCarboHydrateInfo(contextFlagsConditions.C2, data)
        val meal = readMeal(contextFlagsConditions.C3, data)
        val (tester, health) = readTesterAndHealth(contextFlagsConditions.C4, data)
        val exercise = readExerciseInfo(contextFlagsConditions.C5, data)
        val medication = readMedicationInfo(contextFlagsConditions.C6, contextFlagsConditions.C8, data)
        val hbA1c = readHbA1c(contextFlagsConditions.C7, data)

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
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains extended flags
 * @return An enum set of [ExtendedFlags] containing the parsed result. Null if [isPresent] is false
 */
internal fun readExtendedFlags(isPresent: Boolean, data: DataReader): EnumSet<ExtendedFlags>? {
    return when (isPresent) {
        true -> parseFlags(data.getNextInt(IntFormat.FORMAT_UINT8), ExtendedFlags::class.java)
        false -> null
    }
}


/**
 * read in and parse the data field of carbohydrate information
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains carbohydrate data
 * @return A data class of [Carbohydrate] containing the parsed result. [null] if [isPresent] is false
 */
internal fun readCarboHydrateInfo(isPresent: Boolean, data: DataReader): Carbohydrate? {
    if (isPresent) {
        return Carbohydrate(
                type = readEnumeration(
                        rawValue = data.getNextInt(IntFormat.FORMAT_UINT8),
                        enumType = CarbohydrateId::class.java,
                        defaultValue = CarbohydrateId.RESERVED_FOR_FUTURE_USE),
                amount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
                unit = Unit.KG
        )
    } else {
        return null
    }
}

/**
 * read in and parse the data field of meal information
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains meal data
 * @return A data class of [Meal] containing the parsed result. [null] if [isPresent] is false
 */
internal fun readMeal(isPresent: Boolean, data: DataReader): Meal? {
    if (isPresent) {
        return readEnumeration(
                rawValue = data.getNextInt(IntFormat.FORMAT_UINT8),
                enumType = Meal::class.java,
                defaultValue = Meal.RESERVED_FOR_FUTURE_USE
        )
    } else {
        return null
    }
}

/**
 * read in and parse the data field of tester and health information
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains tester and health data
 * @return A [Pair] of [Meal] and [Health] containing the parsed result. [null] if [isPresent] is false
 */
internal fun readTesterAndHealth(isPresent: Boolean, data: DataReader): Pair<Tester?, Health?> {
    if (isPresent) {
        val (lowerNibble, upperNibble) = readNibbles(data.getNextInt(IntFormat.FORMAT_UINT8))
        return Pair(
                readEnumeration(lowerNibble, Tester::class.java, Tester.RESERVED_FOR_FUTURE_USE),
                readEnumeration(upperNibble, Health::class.java, Health.RESERVED_FOR_FUTURE_USE)
        )
    } else {
        return Pair(null, null)
    }
}

/**
 * read in and parse the data field of
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains exercise information
 * @return A data class of [ExerciseInfo] containing the parsed result. [null] if [isPresent] is false
 */
internal fun readExerciseInfo(isPresent: Boolean, data: DataReader): ExerciseInfo? {
    if (isPresent) {
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
    } else {
        return null
    }
}

/**
 * read in and parse the data field of medication information
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains medication information
 * @return A data class of [Medication] containing the parsed result. [null] if [isPresent] is false
 */
internal fun readMedicationInfo(isPresent: Boolean, C8: Boolean, data: DataReader): Medication? {
    if (isPresent) {
        return Medication(
                type = readEnumeration(data.getNextInt(IntFormat.FORMAT_UINT8), MedicationId::class.java, MedicationId.RESERVED_FOR_FUTURE_USE),
                amount = data.getNextFloat(FloatFormat.FORMAT_SFLOAT),
                unit = when (C8) {
                    true -> Unit.KG
                    false -> Unit.L
                }
        )
    } else {
        return null
    }
}

/**
 * read in and parse the data field of tester and health information
 * @param isPresent whether the field is present in the data packet. If not this function returns [null]
 * @param data the [DataReader] containing the raw data. It is assume that the immediate next byte values potentially contains HbA1c
 * @return Float representing HbA1c containing the parsed result. [null] if [isPresent] is false
 */
internal fun readHbA1c(isPresent: Boolean, data: DataReader): Float? {
    if (isPresent) {
        return data.getNextFloat(FloatFormat.FORMAT_SFLOAT)
    } else {
        return null
    }
}
