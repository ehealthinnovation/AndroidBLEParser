package org.ehealthinnovation.android.bluetooth.parser

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.mockito.Mockito.mock
import java.util.*

class MockCharacteristicPacket {
    companion object {
        internal fun mockPacketWithUuid(uuid: UUID):CharacteristicPacket{
            val mockPacket:CharacteristicPacket = mock()
            whenever(mockPacket.uuid).thenReturn(uuid)
            return mockPacket
        }

        internal fun mockPacketForRead(vararg values: StubValue): CharacteristicPacket{
            val mockPacket:CharacteristicPacket = mock()
            val dataReader= StubDataReader(*values)
            whenever(mockPacket.readData()).thenReturn(dataReader)
            return mockPacket
        }
    }
}