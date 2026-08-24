import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;



public class Login extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private Nutzer aktSchueler;
  private Admin aktAdmin;
  private Mensa aktMensa;

  // Ende Attribute
  
  public Login() {
    // Frame-Initialisierung
    super("");
   
    dbVerbinden();
  }
  
  // Anfang Methoden
  public Object login(int pID, String pPasswort) { 
      dbConnector.executeStatement("SELECT Passwort FROM Nutzer WHERE uID LIKE '"+pID+"'");
      QueryResult r = dbConnector.getCurrentQueryResult();
      String passwort = r.getData()[0][0];
      if(passwort.equals(pPasswort)) {
          System.out.println("Anmeldedaten richtig!");
          dbConnector.executeStatement("SELECT Rolle FROM Nutzer WHERE uID LIKE '"+pID+"'");
          QueryResult s = dbConnector.getCurrentQueryResult();
          String rolle = s.getData()[0][0];
          if (rolle.equals("Schüler")) {
              System.out.println("Schüler");
              dbConnector.executeStatement("SELECT uID, Vorname, Name, Rolle FROM Nutzer WHERE uID LIKE '"+pID+"'");
              QueryResult a = dbConnector.getCurrentQueryResult();
              String vorname = a.getData()[0][1];
              String name = a.getData()[0][2];
              rolle = a.getData()[0][3];
              aktSchueler = erstelleSchueler(Integer.parseInt(a.getData()[0][0]), vorname, name, rolle );
              return aktSchueler;
          }
          else if (rolle.equals("Admin")) {
              System.out.println("Admin");
              dbConnector.executeStatement("SELECT uID, Vorname, Name, Rolle FROM Nutzer WHERE uID LIKE '"+pID+"'");
              QueryResult a = dbConnector.getCurrentQueryResult();
              String vorname = a.getData()[0][1];
              String name = a.getData()[0][2];
              rolle = a.getData()[0][3];
              aktAdmin = erstelleAdmin(Integer.parseInt(a.getData()[0][0]), vorname, name, rolle );
              return aktAdmin;
          }
          else if (rolle.equals("Mensa")) {
              System.out.println("Mensa");
              dbConnector.executeStatement("SELECT uID, Vorname, Name, Rolle FROM Nutzer WHERE uID LIKE '"+pID+"'");
              QueryResult a = dbConnector.getCurrentQueryResult();
              String vorname = a.getData()[0][1];
              String name = a.getData()[0][2];
              rolle = a.getData()[0][3];
              aktMensa = erstelleMensa(Integer.parseInt(a.getData()[0][0]), vorname, name, rolle );
              return aktMensa;
          }
          else {
              System.out.println("Du hast keine Berechtigung!");
          }
      }
      else {
          System.out.println("Anmeldedaten falsch!");
      }
      return null;
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
  
  private Nutzer erstelleSchueler(int pID, String pVorname, String pNachname, String pPasswort) {
      Nutzer schueler = new Nutzer(pID, pVorname, pNachname, pPasswort);
      return schueler;
  }
  
  private Admin erstelleAdmin(int pID, String pVorname, String pName, String pPasswort) {
      Admin admin = new Admin(pID, pVorname, pName, pPasswort); 
      return admin;
  }
  
  private Mensa erstelleMensa(int pID, String pVorname, String pName, String pPasswort) {
      Mensa mensa = new Mensa(pID, pVorname, pName, pPasswort); 
      return mensa;
  }
  // Ende Methoden
  
  public static void main(String[] args) {
    new Login();
  }
}
