package local.hapra.pongxxkotlin.kernel;


import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Die Koordinaten eines Objekts auf dem Display
 * aktualisiert von andbra im Feb 2020
 * - Bitmap entfernt
 * - max/min hinzugefügt
 *
 * @author Jan Bauerdick
 *
 */
public class Coordinates implements Serializable, Cloneable {

    private int x;
    private int y;
    private int xMax = Integer.MAX_VALUE;
    private int xMin = Integer.MIN_VALUE;
    private int yMax = Integer.MAX_VALUE;
    private int yMin = Integer.MIN_VALUE;

    public Coordinates() {
        x = 0;
        y = 0;
    }

    /**
     * aktualisiert von andbra im Feb 2020
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y ;
    }



    /**
     * hinzugefügt von andbra im Feb 2020
     */
    public void setX(int value) {
        if(value >= xMax){
            x = xMax;
        }else {
            x = Math.max(value, xMin);
        }
    }

    /**
     * hinzugefügt von andbra im Feb 2020
     */
    public void setY(int value) {
        if(value >= yMax){
            y = yMax;
        }else {
            y = Math.max(value, yMin);
        }
    }

    /**
     * Die Funktion gibt true zurück, wenn der maximale Wert von x erreicht ist
     * hinzugefügt von andbra im Feb 2020
     *
     */
    public boolean isXMax(){
        return (x == xMax);
    }

    /**
     * Die Funktion gibt true zurück, wenn der minimale Wert von x erreicht ist
     * hinzugefügt von andbra im Feb 2020
     */
    public boolean isXMin(){
        return (x == xMin);
    }

    /**
     * Die Funktion gibt true zurück, wenn x im Bereich des minimalsten und minimalsten Wert ist
     * hinzugefügt von andbra im Feb 2020
     */
    public boolean isXbetweenMinMax(){
        return (x >= xMin) && (x <= xMax);
    }

    /**
     * Die Funktion gibt true zurück, wenn der maximale Wert von y erreicht ist
     * hinzugefügt von andbra im Feb 2020
     */
    public boolean isYMax(){
        return (y == yMax);
    }

    /**
     * Die Funktion gibt true zurück, wenn der minimale Wert von x erreicht ist
     * hinzugefügt von andbra im Feb 2020
     */
    public boolean isYMin(){
        return (y == yMin);
    }

    /**
     * Die Funktion gibt true zurück, wenn y im Bereich des minimalsten und minimalsten Wert ist
     * hinzugefügt von andbra im Feb 2020
     */
    public boolean isYbetweenMinMax(){
        return (y >= yMin) && (y <= yMax);
    }

/*
    public int getXMin() {
        return xMin;
    }
    public int getXMax() {
        return xMax;
    }

    public int getYMin() {
        return yMin ;
    }
    public int getYMax() {
        return yMax ;
    }
    */

    /**
     * Die Funktion setzt den minimalen Wert von x. Wenn dieser bereits kleiner ist, wird er auf den minimalsten Wert gesetzt
     * hinzugefügt von andbra im Feb 2020
     */
    public void setXMin(int value) {
        if(value > xMax){
            throw new RuntimeException("value of setXMin higher then xMax");
        }
        xMin = value;
        if(x < value) x = value;
    }

    /**
     * Die Funktion setzt den maximalen Wert von x. Wenn dieser bereits größer ist, wird er auf den maximalen Wert gesetzt
     * hinzugefügt von andbra im Feb 2020
     */
    public void setXMax(int value) {
        if(value < xMin){
            throw new RuntimeException("value of setXMax lower then xMin");
        }
        xMax = value;
        if(x > value) x = value;
    }

    /**
     * Die Funktion setzt den minimalen Wert von y. Wenn dieser bereits kleiner ist, wird er auf den minimalsten Wert gesetzt
     * hinzugefügt von andbra im Feb 2020
     */
    public void setYMin(int value) {
        if(value > yMax){
            throw new RuntimeException("value of setYMin higher then yMax");
        }
        yMin = value;
        if(y < value) y = value;
    }

    /**
     * Die Funktion setzt den maximalen Wert von y. Wenn dieser bereits größer ist, wird er auf den maximalen Wert gesetzt
     * hinzugefügt von andbra im Feb 2020
     */
    public void setYMax(int value) {
        if(value < yMin){
            throw new RuntimeException("value of setYMax lower then yMin");
        }
        yMax = value;
        if(y > value) y = value;
    }


    @NonNull
    @Override
    public String toString() {
        return "Coordinates: (" + x + ", " + y + ")";
    }

    /**
     * Erstellt einen Klon von dem Objekt
     * hinzugefügt von andbra im Feb 2020
     * @return einen Klon von dem Objekt
     */
    @NonNull
    @Override
    public Coordinates clone() {
        Coordinates cloneObj = null;
        try {
            cloneObj = (Coordinates) super.clone();
        } catch (CloneNotSupportedException ignored) { }
        return cloneObj;
    }

}
