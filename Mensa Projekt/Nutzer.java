import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;


public class Nutzer extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  // Ende Attribute
  
  public Nutzer() {
    // Frame-Initialisierung
    super("");
    
    dbVerbinden();
  }
  
  // Anfang Methoden
  public void dbVerbinden() {
    dbConnector = new DatabaseConnector("localhost", 3306, "Mensa", "root", "");
    String fehler = dbConnector.getErrorMessage();
    if (fehler == null) {
      System.out.println("Datenbank wurde erfolgreich verbunden!");
    } else {
      System.out.println("Fehlermeldung: " + fehler);
    }
}
  

  // Ende Methoden
  
  public static void main(String[] args) {
    new Nutzer();
  }
  
  
}
