package com.candroid.qrscannerwithfragment

import java.io.Serializable

data class ScannedDevice(val deviceMacAddress: String, val deviceServiceUUID: String, val deviceCharacteristicUUID: String): Serializable
