import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Hilfsklasse fuer sicheres Passwort-Hashing mit BCrypt.
 * Benoetigt die Bibliothek "at.favre.lib:bcrypt" (bcrypt-0_7_0.jar) als
 * externe JAR im BlueJ-Projekt (Tools -> Preferences -> Libraries).
 *
 * Verwendung:
 *   String hash = PasswortUtil.hashPasswort(eingabe);         // beim Anlegen/Aendern
 *   boolean ok  = PasswortUtil.pruefePasswort(eingabe, hash); // beim Login
 */
public class PasswortUtil
{
    // "Cost factor" fuer BCrypt. 10-12 ist ein guter Standard.
    // Hoehere Werte = sicherer, aber langsamer.
    private static final int COST_FACTOR = 12;

    /**
     * Erzeugt aus einem Klartext-Passwort einen sicheren BCrypt-Hash.
     * Der zurueckgegebene String (ca. 60 Zeichen) wird in der Datenbank
     * gespeichert, NIEMALS das Klartext-Passwort selbst.
     */
    public static String hashPasswort(String klartextPasswort)
    {
        if (klartextPasswort == null || klartextPasswort.isEmpty()) {
            throw new IllegalArgumentException("Passwort darf nicht leer sein.");
        }
        return BCrypt.withDefaults().hashToString(COST_FACTOR, klartextPasswort.toCharArray());
    }

    /**
     * Prueft, ob ein eingegebenes Klartext-Passwort zum gespeicherten
     * BCrypt-Hash aus der Datenbank passt. Fuer den Login verwenden.
     */
    public static boolean pruefePasswort(String eingabePasswort, String gespeicherterHash)
    {
        if (eingabePasswort == null || gespeicherterHash == null) {
            return false;
        }
        BCrypt.Result result = BCrypt.verifyer().verify(eingabePasswort.toCharArray(), gespeicherterHash);
        return result.verified;
    }

    /**
     * Hilfsmethode, um zu erkennen, ob ein in der DB gespeicherter Wert
     * bereits ein BCrypt-Hash ist oder noch ein altes Klartext-Passwort.
     * Nuetzlich fuer eine schrittweise Migration bestehender Nutzer.
     */
    public static boolean istBereitsGehasht(String gespeicherterWert)
    {
        return gespeicherterWert != null
            && (gespeicherterWert.startsWith("$2a$")
                || gespeicherterWert.startsWith("$2b$")
                || gespeicherterWert.startsWith("$2y$"));
    }
}
