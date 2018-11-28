package org.ehealthinnovation.android.bluetooth.cgm

abstract class CgmControlOperand

/**
 * Operand for the [SetCommunicationInterval] command.
 *
 * @property intervalMinute the period at which a CGM device is set to communicate with the
 * collector. It should fall in the range of 0 to 255. 0 has special meaning that
 * communication is paused. 255 means to communicate at highest frequency possible
 */
data class CommunicationInterval(val intervalMinute: Int) : CgmControlOperand() {
    init {
        if (intervalMinute < 0 || intervalMinute > 0x00FF) {
            throw IllegalArgumentException("$intervalMinute should not be negative or larger than 0xff")
        }
    }
}




