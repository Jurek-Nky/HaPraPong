package local.hapra.pongxxkotlin.impl

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


import local.hapra.pongxxkotlin.kernel.OrientationAbstract
import kotlin.math.abs

/**
 * Zugriff auf den Beschleunigunssensor
 * aktualisiert im Feb 2020 von andbra
 * @author Jan Bauerdick
 *
 */
class Orientation(context: Context) : OrientationAbstract(context) {
    var gyroX: Float = 0.0f
    var gyroY: Float = 0.0f
    var gyroZ: Float = 0.0f
    var timeGyro: Long = 0
    var visit: Boolean = false
    private val lisSensorEvent: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            //wenn der Sensor existiert und ein Beschleunigungs-Sensor ist, werden die Textfelder mit Hilfe der Funktion handleAccelUpdate(..) aktualisiert
            if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                if (!visit) {
                    timeGyro = System.currentTimeMillis()
                    visit = true
                } else {
                    val time = (System.currentTimeMillis() - timeGyro) / 1000f
                    gyroX += ((event.values[1] * time) * (180.0 / Math.PI)).toFloat()
                    gyroY += ((event.values[0] * time) * (180.0 / Math.PI)).toFloat()
                    gyroZ += ((event.values[2] * time) * (180.0 / Math.PI)).toFloat()
                    timeGyro = System.currentTimeMillis()
                    println("$gyroX , $gyroY , $gyroZ")
                }
            }
        }
    }


    override fun getXAcceleration(): Float {
        // TODO XAcceleration zurückgeben (Hinweis in der Oberklasse beachten!)
        if (gyroX >= 45) {
            return 10f
        }
        if (gyroX <= -45) {
            return 10f
        }
        if (gyroX > -3 && gyroX < 3) {
            return 0f
        }
        return abs(gyroX) / 45 * 10
    }

    override fun getYAcceleration(): Float {
        // TODO YAcceleration zurückgeben (Hinweis in der Oberklasse beachten!)
        if (gyroY >= 45) {
            return 10f
        }
        if (gyroY <= -45) {
            return 10f
        }
        if (gyroY > -3 && gyroY < 3) {
            return 0f
        }
        return abs(gyroY) / 45 * 10
    }

    override fun getXDirection(): Int {
        // TODO XDirection zurückgeben (Hinweis in der Oberklasse beachten!)
        if (gyroX < 0) {
            return -1
        }
        return 1
    }

    override fun getYDirection(): Int {
        // TODO YDirection zurückgeben (Hinweis in der Oberklasse beachten!)
        if (gyroY < 0) {
            return -1
        }
        return 1
    }

    override fun startAccelSensor() {
        val manSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensGyro = manSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        manSensorManager.registerListener(
            lisSensorEvent,
            sensGyro,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun stopAccelSensor() {
        val manSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // entferne den Listener aus dem SensorManager
        manSensorManager.unregisterListener(lisSensorEvent)
    }


}

