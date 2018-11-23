package org.ehealthinnovation.android.bluetooth.cgm

import org.ehealthinnovation.android.bluetooth.common.racp.RacpComposer

/**
 * Use to compose RACP request for CGM records.
 */
class CgmRacpComposer : RacpComposer(CgmRacpOperandComposer())