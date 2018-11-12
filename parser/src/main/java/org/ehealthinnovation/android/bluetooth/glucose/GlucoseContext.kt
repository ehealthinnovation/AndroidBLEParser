package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.parser.EnumerationValue
import org.ehealthinnovation.android.bluetooth.parser.FlagValue

/**
 * A glucose measurement context reported by the meter.
 *
 * Regarding to data field, usually they are not [null]. However, [null] will be assigned to property
 * fields if the packet does not contain the related data (the device does not transmit these data),
 * which is completely normal, don't freak out. The user should regard such information is not available
 * for use. In the case of parsing errors an exception will be thrown, the details of exception are
 * in the specific parser.
 *
 * @property sequenceNumber a unique id of a record. It corresponds to [GlucoseMeasurement.sequenceNumber]. Context information for the same measurement share the same [sequenceNumber].
 * @property carbohydrate carbohydrate information associated with the measurement context.
 * @property meal meal information associated with the measurement context.
 * @property tester the tester of the measurement.
 * @property health the health information of the subject.
 * @property exerciseInfo the exercise information.
 * @property medication the medication information.
 * @property hbA1c the hba1c information in the unit of percentage.
 * @see https://www.bluetooth.org/docman/handlers/downloaddoc.ashx?doc_id=248026&_ga=2.213812759.2068517937.1540314258-1269508550.1529698942 section 3.2
 */
data class GlucoseContext(
        val sequenceNumber: Int,
        val carbohydrate: Carbohydrate?,
        val meal: Meal?,
        val tester: Tester?,
        val health: Health?,
        val exerciseInfo: ExerciseInfo?,
        val medication: Medication?,
        val hbA1c: Float?
)

/**
 * The carbohydrate information data class
 * @property type the type of carbohydrate
 * @property amount the amount of carbohydrate intake
 * @property unit the unit of the carbohydrate amount, it is default to be KG
 */
data class Carbohydrate(
        val type: CarbohydrateId,
        val amount: Float,
        val unit: Unit
)

/**
 * The medication information data class
 * @property type the type of medication
 * @property amount the amount of medication intake
 * @property unit the unit of the medication amount
 */
data class Medication(
        val type: MedicationId,
        val amount: Float,
        val unit: Unit
)

/**
 * The exercise the information
 * @property duration the duration of exercise in second. This value will be set to [Int.MAX_VALUE] if overrun happens.
 * @property intensity the intensity of exercise in percentage. For example 90 means 90% of intensity.
 */
data class ExerciseInfo(
        val duration: Int,
        val intensity: Int
)


/**
 * The meal information associated with the context
 * @property key the key value of the enum
 */
enum class Meal(override val key: Int):EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    PREPRANDIAL(1),
    POSTPRANDIAL(2),
    FASTING(3),
    CASUAL(4),
    BEDTIME(5);
}

/**
 * The tester of a measurement
 * @property key the key value of the enum
 */
enum class Tester(override val key: Int):EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    SELF(1),
    HEALTH_CARE_PROFESSIONAL(2),
    LAB_TEST(3),
    TESTER_VALUE_NOT_AVAILABLE(15);
}

/**
 * The health information of the test subject
 * @property key the key value of the enum
 */
enum class Health(override val key: Int):EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    MINOR_HEALTH_ISSUES(1),
    MAJOR_HEALTH_ISSUES(2),
    DURING_MENSES(3),
    UNDER_STRESS(4),
    NO_HEALTH_ISSUES(5),
    NOT_AVAILABLE(15);
}


/**
 * The enum of the medication type
 * @property key the key value of the enum
 */
enum class MedicationId(override val key: Int): EnumerationValue {
    RESERVED_FOR_FUTURE_USE(-1),
    RAPID_ACTING_INSULIN(1),
    SHORT_ACTING_INSULIN(2),
    INTERMEDIATE_ACTING_INSULIN(3),
    LONG_ACTING_INSULIN(4),
    PRE_MIXED_INSULIN(5);
}

enum class Unit {
    /**
     * Unit of Liters
     */
    L,
    /**
     * Unit of Kg
     */
    KG
}

/**
 * These flags define which data fields are present in the Context Characteristic value.
 * @property bitOffset The offset used to represent this flag.
 * @see https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.characteristic.glucose_measurement_context.xml
 */
enum class ContextFlags(override val bitOffset: Int) : FlagValue {
    CARBOHYDRATE_ID_AND_CARBOHYDRATE_PRESENT(0),
    MEAL_PRESENT(1),
    TESTER_HEALTH_PRESENT(2),
    EXERCISE_DURATION_AND_EXERCISE_INTENSITY_PRESENT(3),
    MEDICATION_ID_AND_MEDICATION_PRESENT(4),
    MEDICATION_VALUE_UNITS(5),
    HBA1C_PRESENT(6),
    EXTENDED_FLAGS_PRESENT(7);
}

/**
 * This flag is in place for future extension for manufacturer. This field is not used in the parsing at the current version.
 * @property bitOffset The offset used to represent this flag.
 *
 */
enum class ExtendedFlags(override val bitOffset: Int) : FlagValue {
    RESERVED_FOR_FUTURE_USE(-1)
}

/**
 * The enum class of carbohydrate type
 * @property key the key value of the enum
 */
enum class CarbohydrateId(override val key: Int) : EnumerationValue {
    BREAKFAST(1),
    LUNCH(2),
    DINNER(3),
    SNACK(4),
    DRINK(5),
    SUPPER(6),
    BRUNCH(7),
    RESERVED_FOR_FUTURE_USE(-1);
}

