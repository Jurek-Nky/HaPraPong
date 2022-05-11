package local.hapra.pongxxkotlin;


/**
 * Spieleinstellungen
 * hinzugefügt von andbra im Feb 2020
 */
public class Settings {

    // True: Ball wird immer zum Spieler gestartet
    // False: Ball wird zufällig gestartet
    public static boolean settingsThrowBallAlwaysToPlayer = true;

    // True: Spieler unten, Computer oben
    // False: Spieler open, Computer unten
    public static boolean settingsPlayerPositionBottom = true;

    // True: X,Y Achsen aktiv
    // False: X Achse aktiv
    public static boolean settingsEnableY = false;

    // True: Paddle2 intelligente KI
    // False: Paddle2 links/rechts bewegungen
    public static boolean settingsEnableKI = false;

    // Anzahl der Runden
    public static int rounds = 3;




}
