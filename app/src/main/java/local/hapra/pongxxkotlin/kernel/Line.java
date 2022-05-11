package local.hapra.pongxxkotlin.kernel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * erstellt von andbra im Feb 2020
 *
 */
public class Line extends GameObject {

    private Paint paint;
    private Coordinates start;
    private Coordinates stop;

    public Line (int contextWidth, int contextHeight){
        super(contextWidth, contextHeight);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1f);
        this.start = new Coordinates(0,0);
        this.stop = new Coordinates(0,0);
    }

    public Paint getPaint(){
        return paint;
    }

    public void setStart(int x, int y){
        this.start.setX(x);
        this.start.setY(y);
    }

    public void setStop(int x, int y){
        this.stop.setX(x);
        this.stop.setY(y);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawLine(start.getX(), start.getY(), stop.getX(), stop.getY(), paint);
    }
}
