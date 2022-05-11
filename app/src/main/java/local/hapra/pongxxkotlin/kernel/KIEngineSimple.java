package local.hapra.pongxxkotlin.kernel;

@SuppressWarnings("FieldCanBeLocal")
public class KIEngineSimple extends KIEngineAbstract {

    // Geschwindigkeit von Paddle2
    private int KISpeedSimple = 6;

    public KIEngineSimple(int p2GraphicWidth, int p2GraphicHeight, int contextWidth, int contextHeight) {
        super(p2GraphicWidth, p2GraphicHeight, contextWidth, contextHeight);
    }

    @Override
    public void updateSpeed(Speed paddle2Speed, Coordinates paddle2Coord, Speed ballSpeed, Coordinates ballCoord) {

        paddle2Speed.setX(KISpeedSimple);

        // Toggle x-direction on collision with borders
        if (paddle2Coord.getX() <= 0) {
            paddle2Speed.toggleXDirection();
        } else if (paddle2Coord.getX() + getP2GraphicWidth() >= getContextWidth()) {
            paddle2Speed.toggleXDirection();
        }
    }
}
