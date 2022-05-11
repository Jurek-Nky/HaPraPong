package local.hapra.pongxxkotlin.kernel;

import android.graphics.Bitmap;

import java.io.Serializable;

import local.hapra.pongxxkotlin.kernel.BitMapGameObject;
import local.hapra.pongxxkotlin.kernel.Coordinates;

/**
 * Zeichnet einen Schlaeger auf dem Display
 * aktualisiert von andbra im Feb 2020
 * - cpu in bottom umgenannt
 * - Spielerbereich hinzugefügt
 * @author Jan Bauerdick
 *
 */
public class Paddle extends BitMapGameObject implements Serializable {

    private boolean positionTop;

    public Paddle(Bitmap bitmap, int contextWidth, int contextHeight, boolean positionTop) {
        super(bitmap, contextWidth, contextHeight);
        this.positionTop = positionTop;
        Coordinates c = getCoordinates();
        // Startposition x-Achse (bei beiden Paddles gleich)
        c.setX(contextWidth / 2 - getGraphic().getWidth() / 2);
        // Spielerbereich x-Achse (bei beiden Paddles gleich)
        c.setXMin(0);
        c.setXMax(contextWidth - getGraphic().getWidth());

        if(positionTop){
            // Startposition y-Achse
            c.setY(10);
            // Spielerbereich y-Achse
            c.setYMin(0);
            c.setYMax(contextHeight / 4 - getGraphic().getHeight());
        }else{
            // Startposition y-Achse
            c.setY(contextHeight - 10 - getGraphic().getHeight() / 2);
            // Spielerbereich y-Achse
            c.setYMin(contextHeight * 3 / 4);
            c.setYMax(contextHeight - getGraphic().getHeight());
        }
    }

    public boolean isPositionTop(){
        return positionTop;
    }
}