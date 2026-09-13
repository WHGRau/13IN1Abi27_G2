/**
 * EINMALIGES Migrations-Programm.
 *
 * Zweck: Alle Passwoerter, die noch im Klartext in der Tabelle "nutzer"
 * stehen, durch einen BCrypt-Hash ersetzen. Muss nur EIN EINZIGES MAL
 * ausgefuehrt werden, danach kann diese Klasse aus dem Projekt entfernt
 * werden (oder einfach nie wieder aufgerufen werden - sie ist so gebaut,
 * dass ein zweiter Durchlauf nichts kaputt macht, siehe Schritt 3).
 *
 * VORAUSSETZUNG: PasswortUtil.java muss im Projekt sein und die
 * BCrypt-Bibliothek (bcrypt-0_7_0.jar) muss in BlueJ als Library
 * eingebunden sein (Tools -> Preferences -> Libraries), sonst schlaegt
 * dieses Programm sofort fehl.
 */
public class PasswortMigration
{
    public static void main(String[] args)
    {
        // DIAGNOSE-AUSGABE: Wenn diese Zeile NICHT erscheint, wurde main()
        // gar nicht wirklich ausgefuehrt (z.B. haengt noch ein BlueJ-Dialog
        // fuer den args-Parameter im Hintergrund).
        System.out.println("PasswortMigration gestartet...");
 
        // SCHRITT 1: Verbindung zur Datenbank aufbauen.
        System.out.println("Versuche Datenbankverbindung aufzubauen...");
        DatabaseConnector db = new DatabaseConnector("localhost", 3306, "Mensa", "root", "");
        System.out.println("Verbindungsversuch abgeschlossen, pruefe Ergebnis...");
        String fehler = db.getErrorMessage();
        if (fehler != null) {
            System.out.println("Verbindung fehlgeschlagen: " + fehler);
            return;
        }
        System.out.println("Verbindung erfolgreich. Starte Migration...");
 
        // SCHRITT 2: ALLE Nutzer mit ihrer uID und ihrem aktuellen Passwort-Wert
        // auslesen.
        System.out.println("Sende SELECT-Abfrage...");
        db.executeStatement("SELECT uID, Passwort FROM nutzer");
        System.out.println("SELECT-Abfrage zurueckgekehrt.");
        QueryResult r = db.getCurrentQueryResult();
 
        if (r == null) {
            System.out.println("Keine Nutzer gefunden oder Abfrage fehlgeschlagen: " + db.getErrorMessage());
            db.close();
            return;
        }
 
        System.out.println("Anzahl gefundener Nutzer: " + r.getRowCount());
 
        int migriert = 0;
        int uebersprungen = 0;
 
        // SCHRITT 3: Jede Zeile einzeln durchgehen.
        for (int i = 0; i < r.getRowCount(); i++) {
            int uID = Integer.parseInt(r.getData()[i][0]);
            String aktuellerWert = r.getData()[i][1];
 
            System.out.println("Verarbeite Zeile " + i + " (uID " + uID + ")...");
 
            // SCHRITT 3a: Pruefen, ob der Wert schon wie ein BCrypt-Hash aussieht
            if (PasswortUtil.istBereitsGehasht(aktuellerWert)) {
                System.out.println("  -> bereits gehasht, uebersprungen.");
                uebersprungen++;
                continue;
            }
 
            System.out.println("  -> hashe Passwort...");
            String hash = PasswortUtil.hashPasswort(aktuellerWert);
            System.out.println("  -> Hashen abgeschlossen.");
 
            // SCHRITT 3c: Hash zurueck in die Datenbank schreiben, per
            // PreparedStatement (der Wert von "hash" wird als reiner Datenwert
            // uebergeben, nicht in den SQL-String eingebaut).
            db.executeStatement("UPDATE nutzer SET Passwort = ? WHERE uID = ?", hash, uID);
 
            // SCHRITT 3d: Pruefen, ob das UPDATE geklappt hat.
            String updateFehler = db.getErrorMessage();
            if (updateFehler != null) {
                System.out.println("FEHLER bei uID " + uID + ": " + updateFehler);
            } else {
                System.out.println("uID " + uID + " erfolgreich migriert.");
                migriert++;
            }
        }
 
        // SCHRITT 4: Zusammenfassung ausgeben.
        System.out.println("Fertig. Migriert: " + migriert + ", uebersprungen (schon gehasht): " + uebersprungen);
        db.close();
    }
}
 