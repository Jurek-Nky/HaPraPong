package local.hapra.pongxxkotlin.impl

import local.hapra.pongxxkotlin.kernel.Speed
import local.hapra.pongxxkotlin.kernel.Coordinates
import local.hapra.pongxxkotlin.kernel.KIEngineAbstract
import kotlin.math.abs

/**
 * KI Engine implementation
 * hinzugefügt von andbra im Geb 2020
 */
@SuppressWarnings("FieldCanBeLocal")
class KIEngine(p2GraphicWidth: Int, p2GraphicHeight: Int, contextWidth:Int, contextHeight: Int) : KIEngineAbstract(p2GraphicWidth, p2GraphicHeight, contextWidth, contextHeight) {

    // unbesiegbarer Spieler
    override fun updateSpeed(paddle2Speed: Speed, paddle2Coord: Coordinates, ballSpeed: Speed, ballCoord: Coordinates) {
        // beschreibar: paddle2Speed
        // nur lesend: paddle2Coord, ballSpeed, ballCoord
        // TODO KIEngine

        val paddle2Xmiddle = paddle2Coord.x + p2GraphicHeight/2

        // Paddle wird immer in Richtung des Balls  beschleunigt.
        if (ballCoord.x > paddle2Xmiddle) {
            // Der Ball ist rechts vom Paddle
            paddle2Speed.setXDirection(Speed.X_DIRECTION_RIGHT)
        } else {
            // Der Ball ist links vom Paddle
            paddle2Speed.setXDirection(Speed.X_DIRECTION_LEFT)
        }

        // Geschwindigkeit abhängig von der Entfernung zum Ball
        paddle2Speed.x = abs(paddle2Xmiddle - ballCoord.x) /10

    }
}
