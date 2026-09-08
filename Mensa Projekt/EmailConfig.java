import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Laedt die E-Mail-Zugangsdaten (Absender + App-Passwort) einmalig aus
 * einer lokalen Datei email.properties, statt sie im Java-Code stehen zu
 * haben. Wird von Login.java und Admin.java gemeinsam benutzt.
 *
 * email.properties liegt im BlueJ-Projektordner (gleicher Ordner wie die
 * .java-Dateien) und sieht so aus:
 *   absender=mensamaxxing@gmail.com
 *   appPasswort=euer-app-passwort
 *
 * WICHTIG: email.properties NIE in Git committen (in .gitignore eintragen).
 */
public class EmailConfig {

    // "static" heisst: Diese Properties werden nur EINMAL fuer das ganze
    // Programm geladen, nicht jedes Mal neu von der Festplatte gelesen.
    private static Properties props;

    /**
     * Laedt die Datei email.properties, falls noch nicht geschehen.
     * Wird intern von getAbsender()/getAppPasswort() aufgerufen.
     */
    private static void laden() {
        // SCHRITT 1: Nur laden, wenn noch nicht geladen (props == null)
        if (props == null) {
            props = new Properties();

            // SCHRITT 2: Datei oeffnen und einlesen. try-with-resources
            // schliesst die Datei automatisch wieder, auch bei einem Fehler.
            try (FileInputStream in = new FileInputStream("email.properties")) {
                props.load(in);
            } catch (IOException e) {
                // SCHRITT 3: Falls die Datei fehlt oder nicht lesbar ist,
                // geben wir eine klare Fehlermeldung auf der Konsole aus,
                // statt das Programm abstuerzen zu lassen.
                System.err.println("email.properties nicht gefunden oder nicht lesbar: " + e.getMessage());
            }
        }
    }

    /**
     * Liefert die Absender-E-Mail-Adresse aus email.properties.
     */
    public static String getAbsender() {
        laden();
        return props.getProperty("absender");
    }

    /**
     * Liefert das App-Passwort aus email.properties.
     */
    public static String getAppPasswort() {
        laden();
        return props.getProperty("appPasswort");
    }
}
