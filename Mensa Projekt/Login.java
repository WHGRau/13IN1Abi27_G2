import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;


public class Login extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;


  // Ende Attribute
  
  public Login() {
    // Frame-Initialisierung
    super("Mottoverwaltung");
   
    dbVerbinden();
  }
  
  // Anfang Methoden
  public void login(int pID, String pPasswort) { 
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
              erstelleSchueler(Integer.parseInt(a.getData()[0][0]), vorname, name, rolle );
          }
          else if (rolle.equals("Admin")) {
              System.out.println("Admin");
          }
          else if (rolle.equals("Mensa")) {
              System.out.println("Mensa");
          }
          else {
              System.out.println("Du hast keine Berechtigung!");
          }
      }
      else {
          System.out.println("Anmeldedaten falsch!");
      }
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
  
  public Nutzer erstelleSchueler(int pID, String pVorname, String pNachname, String pPasswort) {
      Nutzer schueler = new Nutzer(pID, pVorname, pNachname, pPasswort);
      return schueler;
  }
  // Ende Methoden
  
  public static void main(String[] args) {
    new Login();
  }
}
