package com.baro.helpers.interfaces

import java.net.InetAddress

interface OnClientInetAddressReceived {
    fun onClientInetAddressReceived(clientInetAddress: InetAddress?)
}