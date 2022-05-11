package local.hapra.pongxxkotlin.kernel;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * Verwaltet die Geschwindigkeit und Richtung der Spielelemente
 * @author Jan Bauerdick
 *
 */
public class Speed implements Serializable, Cloneable {

    public static final int X_DIRECTION_RIGHT = 1;
    public static final int X_DIRECTION_LEFT = -1;
    public static final int Y_DIRECTION_DOWN = 1;
    public static final int Y_DIRECTION_UP = -1;

    private int _x = 0;
    private int _y = 0;

    private int _xDirection = X_DIRECTION_RIGHT;
    private int _yDirection = Y_DIRECTION_DOWN;

    int getXDirection() {
        return _xDirection;
    }

    /**
     * updated on Feb 2020 by andbra
     * @param direction direction left or right
     */
    public void setXDirection(int direction) {
        if(direction == X_DIRECTION_LEFT || direction == X_DIRECTION_RIGHT){
            _xDirection = direction;
        }
    }

    public void toggleXDirection() {
        if (_xDirection == X_DIRECTION_RIGHT) {
            _xDirection = X_DIRECTION_LEFT;
        } else {
            _xDirection = X_DIRECTION_RIGHT;
        }
    }

    public int getYDirection() {
        return _yDirection;
    }

    /**
     * updated on Feb 2020 by andbra
     * @param direction direction up and down
     */
    public void setYDirection(int direction) {
        if(direction == Y_DIRECTION_DOWN || direction == Y_DIRECTION_UP){
            _yDirection = direction;
        }
    }

    public void toggleYDirection() {
        if (_yDirection == Y_DIRECTION_DOWN) {
            _yDirection = Y_DIRECTION_UP;
        } else {
            _yDirection = Y_DIRECTION_DOWN;
        }
    }

    public int getX() {
        return _x;
    }

    public void setX(int speed) {
        _x = speed;
    }

    public int getY() {
        return _y;
    }

    public void setY(int speed) {
        _y = speed;
    }


    /**
     * Erstellt einen Klon von dem Objekt
     * hinzugefügt von andbra im Feb 2020
     * @return einen Klon von dem Objekt
     */
    @NonNull
    @Override
    public Speed clone() {
        Speed cloneObj = null;
        try {
            cloneObj = (Speed) super.clone();
        } catch (CloneNotSupportedException ignored) { }
        return Objects.requireNonNull(cloneObj);
    }
}
