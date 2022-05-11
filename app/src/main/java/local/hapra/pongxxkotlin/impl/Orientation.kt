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
    var timeGyro: Long = 0
    var isNotinitialized: Boolean = true
    private val lisSensorEvent: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                if (isNotinitialized) {
                    timeGyro = System.currentTimeMillis()
                    isNotinitialized = false
                } else {
                    val time = (System.currentTimeMillis() - timeGyro) / 1000f
                    gyroX += (event.values[1] * time * 180.0 / Math.PI).toFloat()
                    gyroY += (event.values[0] * time * 180.0 / Math.PI).toFloat()
                    timeGyro = System.currentTimeMillis()
                }
            }
        }
    }


    override fun getXAcceleration(): Float {
        // TODO XAcceleration zurückgeben (Hinweis in der Oberklasse beachten!)
        if (gyroX > 60 || gyroX < -60) {
            return 10f
        }
        if (gyroX > -3 && gyroX < 3) {
            return 0f
        }
        return abs(gyroX) / 60 * 10
    }

    override fun getYAcceleration(): Float {
        // TODO YAcceleration zurückgeben (Hinweis in der Oberklasse beachten!)
        if (gyroY > 40 || gyroY < -40) {
            return 10f
        }
        if (gyroY > -3 && gyroY < 3) {
            return 0f
        }
        return abs(gyroY) / 40 * 10
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
        manSensorManager.unregisterListener(lisSensorEvent)
    }


}

