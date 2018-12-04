package org.ehealthinnovation.android.bluetooth.parser

import org.junit.Assert
import java.util.*



fun testMatcherForSpecificBluetoothUuid(uuidString: String, uuidMatcher: (packet: CharacteristicPacket) -> Boolean) {
    val correctUuid = UUID.fromString(uuidString)
    val testPacket = MockCharacteristicPacket.mockPacketWithUuid(correctUuid)
    Assert.assertTrue(uuidMatcher(testPacket))

    val incorrectUuid = UUID.fromString( "00002B22-0000-1000-8000-00805F9B34AA")
    val testPacket2 = MockCharacteristicPacket.mockPacketWithUuid(incorrectUuid)
    Assert.assertFalse(uuidMatcher(testPacket2))
}