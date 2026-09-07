public class Konto{
    private int kID;
    private int uID;
    private float kontostand;
    private DatabaseConnector dbConnector;

    public Konto(int pID) {
        dbVerbinden();
        uID = pID;
        dbConnector.executeStatement("SELECT kID FROM konto WHERE uID = ?", uID);
        QueryResult qr = dbConnector.getCurrentQueryResult();
        if (qr != null && qr.getRowCount() == 1) {
            kID = Integer.parseInt(qr.getData()[0][0]);
            dbConnector.executeStatement("SELECT Kontostand FROM konto WHERE uID = ?", uID);
            qr = dbConnector.getCurrentQueryResult();
            kontostand = Float.parseFloat(qr.getData()[0][0]);
        } else {
            dbConnector.executeStatement(
                "INSERT INTO konto(uID, Pin, Kontostand) VALUES(?, 0, 0)", uID);
        }
    }

    public float getKontostand() {
        return kontostand;
    }

    public void dbVerbinden() {
        dbConnector = new DatabaseConnector("localhost", 3306, "Mensa", "root", "");
        String fehler = dbConnector.getErrorMessage();
        if (fehler == null) {
          System.out.println("Datenbank wurde erfolgreich verbunden!");
        } else {
          System.out.println("Fehlermeldung: " + fehler);
        }
    }
}
