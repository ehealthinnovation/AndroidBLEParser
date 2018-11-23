package org.ehealthinnovation.android.bluetooth.glucose

import org.ehealthinnovation.android.bluetooth.common.racp.RacpComposer

/**
 * Use to compose RACP request for glucose records.
 */
class GlucoseRacpComposer : RacpComposer(GlucoseRacpOperandComposer())