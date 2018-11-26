package org.ehealthinnovation.android.bluetooth.cgm


/**
 * The data structure for a control response
 *
 * @property requestOperation the request code a collector sent to the CGM
 *
 * @property response the response from the operation
 *
 */
data class CgmControlResponse(
        val requestOperation: Opcode,
        val response: ResponseCode
)