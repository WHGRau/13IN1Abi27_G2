public class Konto{
    private int kID;
    private int uID;
    private float kontostand;
    private DatabaseConnector dbConnector;
    
    public Konto(int pID) {
        dbVerbinden();
        uID = pID;
        String sql = ("SELECT kID FROM konto WHERE uID LIKE '"+uID+"'");
        dbConnector.executeStatement(sql);
        QueryResult qr = dbConnector.getCurrentQueryResult();
        if(qr.getRowCount()==1){
            kID = Integer.parseInt(qr.getData()[0][0]);
            sql = ("SELECT kontostand FROM konto WHERE uID LIKE '"+uID+"'");
            dbConnector.executeStatement(sql);
            qr = dbConnector.getCurrentQueryResult();
            kontostand = Integer.parseInt(qr.getData()[0][0]);
        } else {
           dbConnector.executeStatement("INSERT INTO konto(uID, Pin, Kontostand) VALUES('"+uID+"','0000','0')");
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