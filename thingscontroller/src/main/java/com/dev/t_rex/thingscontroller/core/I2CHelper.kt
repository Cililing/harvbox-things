package com.dev.t_rex.thingscontroller.core

import android.util.Log
import com.google.android.things.pio.PeripheralManager
import java.io.IOException

object I2CHelper {

    fun getDeviceList(peripheralManager: PeripheralManager): List<String> {
        return peripheralManager.i2cBusList
    }

    fun scanI2cAvailableAddresses(peripheralManager: PeripheralManager, i2cName: String): List<Int> {
        return (0..127).filter {
            val k = peripheralManager.openI2cDevice(i2cName, it)
            with(k) {
                try {
                    Log.d(this@I2CHelper.javaClass.simpleName, "Checking $it")
                    write(ByteArray(1), 1)
                    true
                } catch (ex: IOException) {
                    false
                } catch (ex: RuntimeException) {
                    false
                } finally {
                    close()
                }
            }
        }

    }

}