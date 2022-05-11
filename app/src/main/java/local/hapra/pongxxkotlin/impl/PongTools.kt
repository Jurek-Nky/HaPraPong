package local.hapra.pongxxkotlin.impl

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Die Klasse enthält statische Funktionen
 * - erzeugt zwei verschiedene Vibrationen
 * hinzugefügt von andbra im Feb 2020
 * siehe https://developer.android.com/reference/android/os/VibrationEffect
 */
class PongTools {

    /**
     * Diese Funktion wird bei einem Goal aufgerufen und löst einen längeren Impuls aus
     */
    companion object {
        fun vibrateGoal(context: Context) {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(1500)
        }

        /**
         * Diese Funktion wird bei einer Kollision mit einem Paddle aufgeruden und löst einen kürzeren Impuls aus
         * @param context der Context für den Zugriff auf den SystemService
         */
        fun vibratePaddle(context: Context){
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(100)
        }
    }

}
