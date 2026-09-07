import java.sql.*;

/**
 * <p>
 * Materialien zu den zentralen NRW-Abiturpruefungen im Fach Informatik ab 2018
 * </p>
 * <p>
 * Klasse DatabaseConnector
 * </p>
 * <p>
 * Ein Objekt der Klasse DatabaseConnector ermoeglicht die Abfrage und Manipulation 
 * einer MySQL-Datenbank. 
 * Beim Erzeugen des Objekts wird eine Datenbankverbindung aufgebaut, so dass 
 * anschließend SQL-Anweisungen an diese Datenbank gerichtet werden koennen.
 * </p>
 * 
 * ERWEITERT um executeStatement(String, Object...) fuer PreparedStatements
 * (Schutz vor SQL-Injection).
 * 
 * @author Qualitaets- und UnterstuetzungsAgentur - Landesinstitut fuer Schule (Original)
 * @version 2016-01-24 (Original), erweitert 2026
 */
public class DatabaseConnector{
  private java.sql.Connection connection;  
  private QueryResult currentQueryResult = null;
  private String message = null;

  /**
   * Ein Objekt vom Typ DatabaseConnector wird erstellt, und eine Verbindung zur Datenbank 
   * wird aufgebaut.
   */
  public DatabaseConnector(String pIP, int pPort, String pDatabase, String pUsername, String pPassword){
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://"+pIP+":"+pPort+"/"+pDatabase, pUsername, pPassword);
    } catch (Exception e) {
      message = e.getMessage();
    }
  }

  /**
   * ORIGINAL-METHODE (bleibt bestehen fuer Abwaertskompatibilitaet).
   * ACHTUNG: Baut SQL-String direkt aus dem Parameter. NIE mit Nutzereingaben
   * per String-Verkettung verwenden -> stattdessen die neue Methode
   * executeStatement(String, Object...) benutzen.
   */
  public void executeStatement(String pSQLStatement){  
    currentQueryResult = null;
    message = null;

    try {
      Statement statement = connection.createStatement();

      if (statement.execute(pSQLStatement)) {
        ResultSet resultset = statement.getResultSet();
        int columnCount = resultset.getMetaData().getColumnCount();

        String[] resultColumnNames = new String[columnCount];
        String[] resultColumnTypes = new String[columnCount];
        for (int i = 0; i < columnCount; i++){
          resultColumnNames[i] = resultset.getMetaData().getColumnLabel(i+1);
          resultColumnTypes[i] = resultset.getMetaData().getColumnTypeName(i+1);
        }

        Queue<String[]> rows = new Queue<String[]>();
        int rowCount = 0;
        while (resultset.next()){
          String[] resultrow =  new String[columnCount];
          for (int s = 0; s < columnCount; s++){
            resultrow[s] = resultset.getString(s+1);
          }
          rows.enqueue(resultrow);
          rowCount = rowCount + 1;
        }

        String[][] resultData = new String[rowCount][columnCount];
        int j = 0;
        while (!rows.isEmpty()){
          resultData[j] = rows.front();
          rows.dequeue();          
          j = j + 1;
        }

        statement.close();
        currentQueryResult =  new QueryResult(resultData, resultColumnNames, resultColumnTypes); 

      } else {
        statement.close();       
      }

    } catch (Exception e) {
      message = e.getMessage();
    }
  }

  /**
   * NEUE METHODE: fuehrt pSQLStatement als PreparedStatement aus.
   * Platzhalter im SQL-String werden als "?" geschrieben, die zugehoerigen
   * Werte werden als pParams uebergeben (in der richtigen Reihenfolge).
   * Schuetzt zuverlaessig vor SQL-Injection, egal welchen Inhalt die
   * Parameter haben.
   *
   * Beispiel:
   *   dbConnector.executeStatement("SELECT * FROM nutzer WHERE username = ?", username);
   *   dbConnector.executeStatement("UPDATE konto SET Kontostand = Kontostand - ? WHERE uID = ?", betrag, uID);
   */
  public void executeStatement(String pSQLStatement, Object... pParams){
    currentQueryResult = null;
    message = null;

    try (PreparedStatement statement = connection.prepareStatement(pSQLStatement)) {

      for (int i = 0; i < pParams.length; i++) {
        statement.setObject(i + 1, pParams[i]);
      }

      if (statement.execute()) {
        ResultSet resultset = statement.getResultSet();
        int columnCount = resultset.getMetaData().getColumnCount();

        String[] resultColumnNames = new String[columnCount];
        String[] resultColumnTypes = new String[columnCount];
        for (int i = 0; i < columnCount; i++){
          resultColumnNames[i] = resultset.getMetaData().getColumnLabel(i+1);
          resultColumnTypes[i] = resultset.getMetaData().getColumnTypeName(i+1);
        }

        Queue<String[]> rows = new Queue<String[]>();
        int rowCount = 0;
        while (resultset.next()){
          String[] resultrow = new String[columnCount];
          for (int s = 0; s < columnCount; s++){
            resultrow[s] = resultset.getString(s+1);
          }
          rows.enqueue(resultrow);
          rowCount = rowCount + 1;
        }

        String[][] resultData = new String[rowCount][columnCount];
        int j = 0;
        while (!rows.isEmpty()){
          resultData[j] = rows.front();
          rows.dequeue();
          j = j + 1;
        }

        currentQueryResult = new QueryResult(resultData, resultColumnNames, resultColumnTypes);
      }

    } catch (Exception e) {
      message = e.getMessage();
    }
  }

  public QueryResult getCurrentQueryResult(){
    return currentQueryResult;
  }

  public String getErrorMessage(){
    return message;
  }

  public void close(){
    try{
      connection.close();
    } catch (Exception e) {
      message = e.getMessage();
    }
  }

}
