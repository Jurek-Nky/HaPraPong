package local.hapra.pongxxkotlin.kernel;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Fasst Funktionen für die Paddles und den Ball zusammen
 */
public abstract class BitMapGameObject extends GameObject {

    private Bitmap bitmap;
    private Coordinates coordinates;
    private Speed speed;

    public BitMapGameObject(Bitmap bitmap, int contextWidth, int contextHeight) {
        super(contextWidth, contextHeight);
        this.bitmap = bitmap;
        this.coordinates = new Coordinates();
        this.speed = new Speed();
    }

    public Bitmap getGraphic(){
        return bitmap;
    }

    public Coordinates getCoordinates(){
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    public Speed getSpeed(){
        return speed;
    }

    public void setSpeed(Speed speed){
        this.speed = speed;
    }/**

     /**
     * Die Funktion überprüft, ob das übergebene Objekt cObj mit diesem Objekt kollidiert.
     * hinzugefügt von andbra im Feb 2020
     * @param cObj zu kontrollierendes BitMapGameObject
     * @return true, wenn eine Kollision erkannt wurde
     */
    public boolean hasCollision(BitMapGameObject cObj){
        int x1 = cObj.getCoordinates().getX();
        int x2 = x1 + cObj.getGraphic().getWidth();
        int y1 = cObj.getCoordinates().getY();
        int y2 = y1 + cObj.getGraphic().getHeight();

        int a1 = this.getCoordinates().getX();
        int a2 = a1 + this.getGraphic().getWidth();
        int b1 = this.getCoordinates().getY();
        int b2 = b1 + this.getGraphic().getHeight();

        return (a2 >= x1 && a1 <= x2 && b1 <= y2 && b2 >= y1);
    }

    /**
     * Die Funktion überprüft, ob das übergebene Objekt cObj mit diesem Objekt oben kollidiert.
     * hinzugefügt von andbra im Feb 2020
     * @param cObj zu kontrollierendes BitMapGameObject
     * @return true, wenn eine Kollision erkannt wurde
     */
    public boolean hasCollisionTop(BitMapGameObject cObj){
        if(!hasCollision(cObj)) return false;

        int y1 = cObj.getCoordinates().getY();
        int y2 = y1 + cObj.getGraphic().getHeight();

        int b1 = this.getCoordinates().getY();
        int b2 = b1 + this.getGraphic().getHeight();

        return (b2 > y2);
    }

    /**
     * Die Funktion überprüft, ob das übergebene Objekt cObj mit diesem Objekt unten kollidiert.
     * hinzugefügt von andbra im Feb 2020
     * @param cObj zu kontrollierendes BitMapGameObject
     * @return true, wenn eine Kollision erkannt wurde
     */
    public boolean hasCollisionBottom(BitMapGameObject cObj){
        if(!hasCollision(cObj)) return false;
        int b1 = this.getCoordinates().getY();
        int y1 = cObj.getCoordinates().getY();
        return (b1 < y1);
    }

    /**
     * Diese Funktion bewegt das Objekt anhand der Koordinaten und der Geschwindikeit
     * hinzugefügt von andbra im Feb 2020
     */
    public void updatePosition(){
        // Koordinaten der x-Achse anhand der Geschwindigkeit ändern
        if (speed.getXDirection() == Speed.X_DIRECTION_RIGHT) {
            coordinates.setX(coordinates.getX() + speed.getX());
        } else {
            coordinates.setX(coordinates.getX() - speed.getX());
        }
        // Koordinaten der y-Achse anhand der Geschwindigkeit ändern
        if (speed.getYDirection() == Speed.Y_DIRECTION_UP) {
            coordinates.setY(coordinates.getY() - speed.getY());
        } else {
            coordinates.setY(coordinates.getY() + speed.getY());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, coordinates.getX(), coordinates.getY(), null);
    }
}
