package local.hapra.pongxxkotlin;


import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import local.hapra.pongxxkotlin.impl.*;
import local.hapra.pongxxkotlin.kernel.*;

/**
 * Spiellogik
 * überarbeitet von andbra im Feb 2020
 * - Einstellmöglichkeiten hinzugefügt ( Spielerposition, erste Ball-Richtung)
 * - Spielfeld Linien hinzugefügt
 * - Code aufgeräumt
 * @author Jan Bauerdick
 *
 */
@SuppressLint("ViewConstructor")
@SuppressWarnings("FieldCanBeLocal")
public class Pong extends SurfaceView implements SurfaceHolder.Callback {
    // Speicherung der Activity ein-/ausschalten
    private final boolean useSavedInstanceState = true;
    private boolean resumeGame = false;

    private final HashMap<String, GameObject> elements = new HashMap<>();
    //private final ArrayList<GameObject> elements = new ArrayList<>();
    private GameThread thread;
    private OrientationAbstract orientation;
    private boolean gameActive;

    private int acceleration;
    private int roundsPlayed;

    private KIEngineAbstract kiEngine;

    private Intent intent;

    public Pong(Context context, Intent intent) {
        super(context);
        this.intent = intent;
        getHolder().addCallback(this);
        roundsPlayed = 0;
        gameActive = false;
        acceleration = 0;
        orientation = new Orientation(this.getContext());
        thread = new GameThread(getHolder(), this);
    }

    public boolean isActive() {
        return gameActive;
    }

    public void setActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    /**
     * bereitet die Spielelemente vor
     */
    public void initGame() {
        synchronized (elements) {
            //Log.v("initGame", "w="+getWidth()+";h="+getHeight());
            //----------------------Paddle1: controlled by user----------------------
            Paddle paddle1 = new Paddle(BitmapFactory.decodeResource(
                    getResources(), R.drawable.paddle), getWidth(),
                    getHeight(), !Settings.settingsPlayerPositionBottom);

            elements.put("p1", paddle1);

            //----------------------Paddle2: controlled by ai----------------------
            Paddle paddle2 = new Paddle(BitmapFactory.decodeResource(
                    getResources(), R.drawable.paddle), getWidth(),
                    getHeight(), Settings.settingsPlayerPositionBottom);
            elements.put("p2", paddle2);


            //------------------------- Game Area -------------------------------
            //Mittellinie
            Line line1 = new Line(getWidth(), getHeight());
            line1.setStart(0, this.getHeight()/2);
            line1.setStop(this.getWidth(), this.getHeight()/2);
            line1.getPaint().setColor(Color.GRAY);
            line1.getPaint().setStrokeWidth(6f);
            elements.put("l1", line1);

            // Linie Player 1
            Line line2 = new Line(getWidth(), getHeight());
            line2.setStart(0, this.getHeight()/4);
            line2.setStop(this.getWidth(), this.getHeight()/4);
            line2.getPaint().setColor(Color.GRAY);
            line2.getPaint().setStrokeWidth(2f);
            elements.put("l2", line2);

            // Linie Player 2
            Line line3 = new Line(getWidth(), getHeight());
            line3.setStart(0, this.getHeight() *3/4);
            line3.setStop(this.getWidth(), this.getHeight()* 3/4);
            line3.getPaint().setColor(Color.GRAY);
            line3.getPaint().setStrokeWidth(2f);
            elements.put("l3", line3);

            //------------------------- KI ENGINE -------------------------------
            if(Settings.settingsEnableKI){
                kiEngine = new KIEngine(paddle2.getGraphic().getWidth(), paddle2.getGraphic().getHeight(), getWidth(),getHeight());
            }else{
                kiEngine = new KIEngineSimple(paddle2.getGraphic().getWidth(), paddle2.getGraphic().getHeight(), getWidth(),getHeight());
            }
        }

        Canvas c = getHolder().lockCanvas();
        draw(c);
        getHolder().unlockCanvasAndPost(c);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        synchronized (elements) {
            canvas.drawColor(Color.BLACK);
            for (GameObject element : elements.values()) {
                element.draw(canvas);
            }
        }
    }

    /**
     * aktualisiert von andbra im Feb 2020
     * - vereinfacht
     * @param canvas Canvas Objekt
     */
    @Override
    public void onDraw(Canvas canvas) {
    }


    /**
     * aktualisiert von andbra im Feb 2020
     * - settingsThrowBallAlwaysToPlayer hinzugefügt
     * - rounds, roundsPlayed hinzugefügt
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (elements) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(!isActive()){
                    // Nur auf Ereignisse reagieren, wenn das Spiel nicht aktiv ist.
                    if(resumeGame) {
                        resumeGame = false;
                        startGame();
                        setActive(true);
                    }else if (roundsPlayed == 0) {
                        startGame();
                        newRound();
                        setActive(true);
                    }else if ((roundsPlayed > 0) && (roundsPlayed < Settings.rounds)){
                        newRound();
                        setActive(true);
                    }else{
                        endGame();
                    }
                }

            }else if(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS){
                // macht im Prinzip nichts
                performClick();
            }
            return true;
        }
    }

    /**
     * "soda"-Funktion: wird benötigt, um Android Studio zufrieden zu stellen
     * Die Funktion ist eigentlich dafür gedacht, um einen Text auszugeben, wenn ein Button gedrückt wurde.
     * Dieser Text kann für sehbehinderte Menschen vorgelesen werden. Wird hier nicht benötigt.
     * @author andbra
     * @return trua
     */
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    /**
     * aktualisiert von andbra im Feb 2020
     */
    public void updatePhysics() {
        if (isActive()) {
            synchronized (elements) {
                Paddle p1 = (Paddle) elements.get("p1");
                Paddle p2 = (Paddle) elements.get("p2");
                Ball ball = (Ball) elements.get("ball");

                // wir können annhemen, dass die Elemente nicht null sind
                assert p1 != null;
                assert p2 != null;
                assert ball != null;

                Coordinates coord;
                Speed speed;

                //----------------------Paddle1: controlled by user----------------------
                coord = p1.getCoordinates();
                speed = p1.getSpeed();

                // Displayauflösung beachten
                //float p1SpeedMultiplikatorX = 3f * getWidth()/720f * getHeight()/getWidth();
                float p1SpeedMultiplikatorX = getHeight()/720f ;
                float p1SpeedMultiplikatorY = getHeight()/1280f;

                // laden der Beschleunigungswerte
                int accel_x = Math.round(orientation.getXAcceleration() * p1SpeedMultiplikatorX);
                int accel_y = Math.round(orientation.getYAcceleration() * p1SpeedMultiplikatorY);
                int accel_x_direction = orientation.getXDirection();
                int accel_y_direction = orientation.getYDirection();

                // überprüfen, ob die Beschleunigungswerte positiv sind
                if(accel_x < 0){
                    accel_x = 0;
                    Toast.makeText(getContext(), R.string.errGetXAccel, Toast.LENGTH_SHORT).show();
                }

                if(accel_y < 0){
                    accel_y = 0;
                    Toast.makeText(getContext(), R.string.errGetYAccel, Toast.LENGTH_SHORT).show();
                }

                speed.setXDirection(accel_x_direction);
                speed.setYDirection(accel_y_direction);

                // Behandlung der x-Achse
                if(coord.isXMin() && (accel_x_direction == Speed.X_DIRECTION_RIGHT)) {
                    // wenn der Schläger am linken Rand ist und der Schläger nach rechts geht
                    speed.setX(accel_x);
                } else if (coord.isXMax() && (accel_x_direction == Speed.X_DIRECTION_LEFT)){
                    // wenn der Schläger am rechten Rand ist und der Schläger nach links geht
                    speed.setX(accel_x);
                } else if (coord.isXbetweenMinMax()){
                    // wenn der Schläger im Spielfeld ist
                    speed.setX(accel_x);
                } else{
                    speed.setX(0);
                }

                if(!Settings.settingsEnableY) {
                    speed.setY(0);
                } else if (coord.isYMin() && (accel_y_direction == Speed.Y_DIRECTION_DOWN) ){
                    // wenn der Schläger am oberen Rand ist
                    speed.setY(accel_y);
                } else if (coord.isYMax() && (accel_y_direction == Speed.Y_DIRECTION_UP)) {
                    // wenn der Schläger am unteren Rand ist
                    speed.setY(accel_y);
                } else if (coord.isYbetweenMinMax()){
                    // wenn der Schläger im Spielfeld ist
                    speed.setY(accel_y);
                } else {
                    speed.setY(0);
                }


                //----------------------Paddle2: controlled by AI Engine ----------------

                kiEngine.updateSpeed(p2.getSpeed(), p2.getCoordinates().clone() , ball.getSpeed().clone(), ball.getCoordinates().clone());

                //---------------------------Ball---------------------------------------
                coord = ball.getCoordinates();
                speed = ball.getSpeed();

                boolean speedUp = false;

                // Toggle y-direction on collision with paddles
                if(ball.hasCollisionTop(p1)){
                    // der Ball wird oben von Paddle 1 berührt
                    if (ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_UP) {
                        // der Ball bewegt sich nach oben
                        speed.toggleYDirection();
                        speedUp = true;
                    } else{
                        // ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN
                        if(p1.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN && p1.getSpeed().getY() > ball.getSpeed().getY()){
                            // beschleunigen, wenn man den Ball von der falschen Seite berührt und das Paddle schneller als der Ball ist
                           ball.getSpeed().setY(p1.getSpeed().getY());
                        }
                    }
                    // Ball wird auf die Unterseite des Paddles gesetzt
                    ball.getCoordinates().setY(p1.getCoordinates().getY() + p1.getGraphic().getHeight() + 1 );
                } else if (ball.hasCollisionBottom(p1)) {
                    // der Ball wird unten von Paddle 1 berührt
                    if (ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN) {
                        // der Ball bewegt sich nach unten
                        speed.toggleYDirection();
                        speedUp = true;
                    } else{
                        // ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_UP
                        if(p1.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN && p1.getSpeed().getY() > ball.getSpeed().getY()){
                            // beschleunigen, wenn man den Ball von der falschen Seite berührt und das Paddle schneller als der Ball ist
                            ball.getSpeed().setY(p1.getSpeed().getY());
                        }
                    }
                    ball.getCoordinates().setY(p1.getCoordinates().getY() - ball.getGraphic().getHeight());
                } else if(ball.hasCollisionTop(p2)){
                    if (ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_UP) {
                        speed.toggleYDirection();
                        speedUp = true;
                    } else{
                        // ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN
                        if(p2.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN && p2.getSpeed().getY() > ball.getSpeed().getY()){
                            // beschleunigen, wenn man den Ball von der falschen Seite berührt und das Paddle schneller als der Ball ist
                            ball.getSpeed().setY(p2.getSpeed().getY());
                        }
                    }
                    ball.getCoordinates().setY(p2.getCoordinates().getY() + p2.getGraphic().getHeight());
                }else if(ball.hasCollisionBottom(p2)) {
                    if (ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN) {
                        speed.toggleYDirection();
                        speedUp = true;
                    } else{
                        // ball.getSpeed().getYDirection() == Speed.Y_DIRECTION_UP
                        if(p2.getSpeed().getYDirection() == Speed.Y_DIRECTION_DOWN && p2.getSpeed().getY() > ball.getSpeed().getY()){
                            // beschleunigen, wenn man den Ball von der falschen Seite berührt und das Paddle schneller als der Ball ist
                            ball.getSpeed().setY(p2.getSpeed().getY());
                        }
                    }
                    ball.getCoordinates().setY(p2.getCoordinates().getY() - ball.getGraphic().getHeight());
                }

                // Wenn der Ball von einem Paddle geschlagen wurde, wird die Geschwindigkeit erhöht
                if(speedUp) {
                    PongTools.Companion.vibratePaddle(getContext());
                    if (acceleration < 10) {
                        acceleration++;
                    }
                    if (acceleration > 4) {
                        ball.getSpeed().setY(ball.getSpeed().getY() + (acceleration)/2 - 2);
                    }
                }

                // Toggle x-direction on collision with borders
                if (coord.getX() < 0) {
                    speed.toggleXDirection();
                    coord.setX(-coord.getX());
                } else if (coord.getX() + ball.getGraphic().getWidth() > getWidth()) {
                    speed.toggleXDirection();
                    coord.setX(coord.getX() + getWidth()
                            - (coord.getX() + ball.getGraphic().getWidth()));

                }

                //---------------------------Update Positions---------------------------------------
                p1.updatePosition();
                p2.updatePosition();
                ball.updatePosition();

                //---------------------------Check Goals---------------------------------------
                //goal for AI
                if (coord.getY() <= 0) {
                    PongTools.Companion.vibrateGoal(getContext());
                    //goal for AI
                    elements.remove("ball");
                    setActive(false);
                }else if (coord.getY() + ball.getGraphic().getHeight() >= getHeight()) {
                    PongTools.Companion.vibrateGoal(getContext());
                    //goal for user
                    elements.remove("ball");
                    setActive(false);
                }
            }
        }

    }

    /**
     *
     */
    private void loadState () {
        int h = intent.getIntExtra("height",0);
        int w = intent.getIntExtra("width",0);
        if(h != getHeight() || w != getWidth()){
            // Display Auflösung hat sich geändert
            String msg = "alt=("+ h +","+ w +")neu=("+ getHeight() +","+ getWidth() +")";
            Log.v("loadState", "Display Auflösung hat sich geändert " + msg);
            return;
        }

        Settings.settingsEnableKI = intent.getBooleanExtra("settingsEnableKI", false);
        Settings.settingsPlayerPositionBottom = intent.getBooleanExtra("settingsPlayerPositionBottom", true);
        Settings.settingsEnableY = intent.getBooleanExtra("settingsEnableY", false);
        Settings.settingsThrowBallAlwaysToPlayer = intent.getBooleanExtra("settingsThrowBallAlwaysToPlayer", true);
        Settings.rounds = intent.getIntExtra("rounds", 3);

        roundsPlayed = intent.getIntExtra("roundsPlayed",0);
        //gameActive = intent.getBooleanExtra("gameActive", false);
        acceleration = intent.getIntExtra("acceleration",0);


        Paddle p1 = (Paddle) elements.get("p1");
        Paddle p2 = (Paddle) elements.get("p2");
        assert p1 != null;
        assert p2 != null;
        Coordinates p1c = (Coordinates) intent.getSerializableExtra("p1c");
        Speed p1s = (Speed) intent.getSerializableExtra("p1s");
        p1.setCoordinates(p1c);
        p1.setSpeed(p1s);
        assert p1c != null;
        //Log.v("loadState", p1c.toString());
        Coordinates p2c = (Coordinates) intent.getSerializableExtra("p2c");
        Speed p2s = (Speed) intent.getSerializableExtra("p2s");
        p2.setCoordinates(p2c);
        p2.setSpeed(p2s);

        if(intent.hasExtra("ballc")){
            Ball b = new Ball(BitmapFactory.decodeResource(
                    getResources(), R.drawable.whiteball), getWidth(), getHeight());

            b.setCoordinates((Coordinates) intent.getSerializableExtra("ballc"));
            b.setSpeed((Speed) intent.getSerializableExtra("balls"));
            elements.put("ball", b);
        }

        kiEngine = (KIEngineAbstract) intent.getSerializableExtra("ki");

        Canvas c = getHolder().lockCanvas();
        draw(c);
        getHolder().unlockCanvasAndPost(c);
        resumeGame = true;
    }

    /**
     * speichert alle notwendigen Informationen im Intent
     */
    public void saveState(){
        if (!elements.containsKey("p1") && !elements.containsKey("p2")) return;
        synchronized (elements) {
            Paddle p1 = (Paddle) elements.get("p1");
            Paddle p2 = (Paddle) elements.get("p2");
            if(elements.containsKey("ball")){
                Ball ball = (Ball) elements.get("ball");
                assert ball != null;
                intent.putExtra("ballc", ball.getCoordinates());
                intent.putExtra("balls", ball.getSpeed());

            }

            assert p1 != null;
            assert p2 != null;
            intent.putExtra("p1c", p1.getCoordinates());
            intent.putExtra("p2c", p2.getCoordinates());
            intent.putExtra("p1s", p1.getSpeed());
            intent.putExtra("p2s", p2.getSpeed());

            intent.putExtra("settingsEnableKI", Settings.settingsEnableKI);
            intent.putExtra("settingsPlayerPositionBottom", Settings.settingsPlayerPositionBottom);
            intent.putExtra("settingsEnableY", Settings.settingsEnableY);
            intent.putExtra("settingsThrowBallAlwaysToPlayer", Settings.settingsThrowBallAlwaysToPlayer);
            intent.putExtra("rounds", Settings.rounds);
            intent.putExtra("height", getHeight());
            intent.putExtra("width", getWidth());
            intent.putExtra("roundsPlayed", this.roundsPlayed);
            intent.putExtra("gameActive", this.gameActive);
            intent.putExtra("acceleration", this.acceleration);
            intent.putExtra("saved", true);
            intent.putExtra("ki", kiEngine);
        }
    }

    public void onResume(){
        orientation.startAccelSensor();
    }

    /**
     * stoppt den Beschleunigungssensor
     * beendet den Thread und wartet
     * speichert ggf. den Zustand der App
     */
    public void onPause(){
        orientation.stopAccelSensor();
        thread.setRunning(false);
        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }
        if(useSavedInstanceState){
            saveState();
        }
    }


    private void startGame(){
        thread.setRunning(true);
        thread.start();
    }

    /**
     * startet eine neue Runde
     */
    private void newRound(){
        roundsPlayed++;
        Ball b = new Ball(BitmapFactory.decodeResource(
                getResources(), R.drawable.whiteball), getWidth(), getHeight());
        b.resetBall();

        // Damit der Ball in Richtung des Spielers startet
        if(Settings.settingsThrowBallAlwaysToPlayer){
            if(Settings.settingsPlayerPositionBottom){
                b.getSpeed().setYDirection(Speed.Y_DIRECTION_DOWN);
            }else {
                b.getSpeed().setYDirection(Speed.Y_DIRECTION_UP);
            }
        }
        elements.put("ball",b);
    }

    private void endGame(){
        thread.setRunning(false);
        ((Activity) getContext()).finish();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (thread != null) thread.setRunning(visibility != VISIBLE);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initGame();
        // stellt den App Zustand wieder her
        if(useSavedInstanceState){
            if(intent.getBooleanExtra("saved", false)){
                intent.putExtra("saved", false);
                if(intent.getIntExtra("roundsPlayed",0) > 0 ){
                    Log.v("onResume", "loadState");
                    loadState();
                }
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException ignored) {
            }
        }
    }
}