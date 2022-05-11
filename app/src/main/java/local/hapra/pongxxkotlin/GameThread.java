package local.hapra.pongxxkotlin;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Der Thread, der das Spiel ausfuehrt
 * @author Jan Bauerdick
 *
 */
public class GameThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private Pong gameElement;
    private boolean running = false;

    public GameThread(SurfaceHolder surfaceHolder, Pong gameElement) {
        this.surfaceHolder = surfaceHolder;
        this.gameElement = gameElement;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning(){
        return running;
    }
    /**
     *
     */
    @Override
    public void run() {
        Canvas c;
        while (running) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                if(c != null){
                    synchronized (c) {
                        gameElement.draw(c);
                        gameElement.updatePhysics();
                    }
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

}