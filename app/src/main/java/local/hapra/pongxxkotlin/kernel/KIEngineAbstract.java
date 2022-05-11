package local.hapra.pongxxkotlin.kernel;

import java.io.Serializable;

/**
 * Abstrakte Klasse für die KI Engine
 * hinzugefügt von andbra im Geb 2020
 */
public abstract class KIEngineAbstract implements Serializable {

    private final int p2GraphicWidth;
    private final int p2GraphicHeight;
    private final int contextWidth;
    private final int contextHeight;

    public KIEngineAbstract(int p2GraphicWidth, int p2GraphicHeight, int contextWidth, int contextHeight){
        this.p2GraphicWidth = p2GraphicWidth;
        this.p2GraphicHeight = p2GraphicHeight;
        this.contextWidth = contextWidth;
        this.contextHeight = contextHeight;
    }

    /**
     * Diese Funktion berechnet die Geschwindigkeit von paddle2 neu
     * @param paddle2Speed Paddle2-Geschwindigkeit
     * @param paddle2Coord Paddle2-Koordinaten (Kopie)
     * @param ballSpeed Ball-Geschwindigkeit (Kopie)
     * @param ballCoord Ball-Koordinaten (Kopie)
     */
    public abstract void updateSpeed(Speed paddle2Speed, Coordinates paddle2Coord, Speed ballSpeed, Coordinates ballCoord);

    /**
     *
     * @return height of paddle2 bitmap
     */
    protected int getP2GraphicHeight() {
        return p2GraphicHeight;
    }

    /**
     *
     * @return width of paddle2 bitmap
     */
    int getP2GraphicWidth() {
        return p2GraphicWidth;
    }

    /**
     *
     * @return context width
     */
    int getContextWidth() {
        return contextWidth;
    }

    /**
     *
     * @return context height
     */
    protected int getContextHeight() {
        return contextHeight;
    }
}
