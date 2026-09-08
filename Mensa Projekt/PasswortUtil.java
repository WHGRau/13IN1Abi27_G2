import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Hilfsklasse fuer sicheres Passwort-Hashing mit BCrypt.
 *
 * VORAUSSETZUNG: Die Bibliothek "at.favre.lib:bcrypt" (bcrypt-0_7_0.jar)
 * muss als externe JAR im BlueJ-Projekt eingebunden sein:
 * Tools -> Preferences -> Libraries -> JAR hinzufuegen -> BlueJ neu starten.
 *
 * Diese Klasse ist die EINZIGE Stelle im Projekt, die direkt mit der
 * BCrypt-Bibliothek redet. Alle anderen Klassen (Login, Admin, Nutzer,
 * PasswortMigration) rufen nur hashPasswort() und pruefePasswort() auf.
 * Vorteil: Falls ihr jemals die BCrypt-Bibliothek wechselt, muesst ihr
 * nur diese eine Datei anpassen.
 */
public class PasswortUtil
{
    // Der "Cost factor" bestimmt, wie rechenintensiv (=langsam) das Hashen ist.
    // 10-12 ist ein guter Standard fuer 2024+. Hoeher = sicherer gegen
    // Brute-Force-Angriffe, aber auch langsamer bei jedem Login.
    private static final int COST_FACTOR = 12;

    /**
     * Erzeugt aus einem Klartext-Passwort einen sicheren BCrypt-Hash-String.
     * Wird aufgerufen, wenn ein Passwort NEU gesetzt wird (neuer Nutzer,
     * Passwort-Aenderung, Passwort-Reset).
     *
     * @param klartextPasswort das vom Nutzer eingegebene, unverschluesselte Passwort
     * @return ein ca. 60 Zeichen langer String wie "$2a$12$R9h/cIPz0gi...",
     *         der SICHER in der Datenbank gespeichert werden kann
     */
    public static String hashPasswort(String klartextPasswort)
    {
        // SCHRITT 1: Grundpruefung, damit wir kein leeres Passwort hashen
        if (klartextPasswort == null || klartextPasswort.isEmpty()) {
            throw new IllegalArgumentException("Passwort darf nicht leer sein.");
        }

        // SCHRITT 2: BCrypt.withDefaults() liefert ein "Hasher"-Objekt.
        // hashToString(costFactor, passwortAlsCharArray) macht intern:
        //   a) einen zufaelligen Salt erzeugen
        //   b) das Passwort COST_FACTOR-fach (2^12 Runden) mit dem Salt verrechnen
        //   c) Algorithmus-Version + Cost-Factor + Salt + Ergebnis-Hash
        //      zu einem einzigen String zusammenfuegen
        // Das toCharArray() (statt String) ist BCrypt-Konvention, damit das
        // Passwort nicht unnoetig lange als unveraenderlicher String im
        // Speicher "haengen bleibt".
        return BCrypt.withDefaults().hashToString(COST_FACTOR, klartextPasswort.toCharArray());
    }

    /**
     * Prueft, ob ein eingegebenes Klartext-Passwort zu einem gespeicherten
     * BCrypt-Hash passt. Wird beim LOGIN aufgerufen.
     *
     * @param eingabePasswort   was der Nutzer beim Einloggen eintippt (Klartext)
     * @param gespeicherterHash der Hash-String, der in der DB-Spalte "Passwort" steht
     * @return true, wenn das Passwort passt, sonst false
     */
    public static boolean pruefePasswort(String eingabePasswort, String gespeicherterHash)
    {
        // SCHRITT 1: Grundpruefung gegen Null-Werte (z.B. wenn kein Nutzer gefunden wurde)
        if (eingabePasswort == null || gespeicherterHash == null) {
            return false;
        }

        // SCHRITT 2: BCrypt.verifyer().verify(...) liest aus gespeicherterHash
        // automatisch den damals verwendeten Salt und Cost-Factor wieder aus,
        // hasht eingabePasswort damit erneut und vergleicht das Ergebnis mit
        // dem gespeicherten Hash. Wir muessen also NIE selbst zwei Strings
        // mit .equals() vergleichen.
        BCrypt.Result result = BCrypt.verifyer().verify(eingabePasswort.toCharArray(), gespeicherterHash);

        // SCHRITT 3: result.verified ist ein boolean, true wenn die Passwoerter
        // (nach Hashing) uebereinstimmen.
        return result.verified;
    }

    /**
     * Hilfsmethode fuer die Migration bestehender Daten: erkennt, ob ein
     * Wert aus der DB bereits ein BCrypt-Hash ist (beginnt mit $2a$, $2b$
     * oder $2y$) oder noch ein altes Klartext-Passwort.
     *
     * @param gespeicherterWert der Rohwert aus der Spalte "Passwort"
     * @return true, wenn es schon ein BCrypt-Hash ist
     */
    public static boolean istBereitsGehasht(String gespeicherterWert)
    {
        return gespeicherterWert != null
            && (gespeicherterWert.startsWith("$2a$")
                || gespeicherterWert.startsWith("$2b$")
                || gespeicherterWert.startsWith("$2y$"));
    }
}
