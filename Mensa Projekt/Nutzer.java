import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;


public class Nutzer extends JFrame {
  // Anfang Attribute
  private DatabaseConnector dbConnector;
  private JTextField tVorname = new JTextField();
  private JTextField tName = new JTextField();
  private JTextField tMotto = new JTextField();
  private JButton jbEintragen = new JButton();
  private JTextArea jtaAusgabe = new JTextArea("");
    private JScrollPane jtaAusgabeScrollPane = new JScrollPane(jtaAusgabe);
  private JLabel jLabel1 = new JLabel();
  private JButton jbVerbinden = new JButton();
  // Ende Attribute
  
  public Nutzer() {
    // Frame-Initialisierung
    super("Mottoverwaltung");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    int frameWidth = 797; 
    int frameHeight = 378;
    setSize(frameWidth, frameHeight);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (d.width - getSize().width) / 2;
    int y = (d.height - getSize().height) / 2;
    setLocation(x, y);
    Container cp = getContentPane();
    cp.setLayout(null);
    // Anfang Komponenten
    
    tVorname.setBounds(16, 72, 105, 24);
    tVorname.setText("Vorname");
    cp.add(tVorname);
    tName.setBounds(136, 72, 105, 24);
    tName.setText("Name");
    cp.add(tName);
    tMotto.setBounds(16, 104, 225, 24);
    tMotto.setText("Mottovorschlag");
    cp.add(tMotto);
    jbEintragen.setBounds(16, 136, 227, 25);
    jbEintragen.setText("Mottovorschlag einreichen/wählen");
    jbEintragen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        jbEintragen_ActionPerformed(evt);
      }
    });
    cp.add(jbEintragen);
    jtaAusgabeScrollPane.setBounds(272, 48, 489, 281);
    cp.add(jtaAusgabeScrollPane);
    setTitle("AbiMotto");
    jLabel1.setBounds(16, 8, 682, 20);
    jLabel1.setText("Bitte starten Sie zuerst den Apache-Server und die MySQL-Datenbank. Importieren Sie dann die Datenbank abimotte.sql.");
    cp.add(jLabel1);
    jbVerbinden.setBounds(16, 32, 227, 25);
    jbVerbinden.setText("Datenbankverbindung");
    jbVerbinden.setMargin(new Insets(2, 2, 2, 2));
    jbVerbinden.addActionListener(new ActionListener() { 
      public void actionPerformed(ActionEvent evt) { 
        jbVerbinden_ActionPerformed(evt);
      }
    });
    cp.add(jbVerbinden);
    // Ende Komponenten
    
    setResizable(false);
    setVisible(true);     
  }
  
  // Anfang Methoden

  public void jbVerbinden_ActionPerformed(ActionEvent evt) {
    dbConnector = new DatabaseConnector("localhost", 3306, "Mensa", "root", "");
    String fehler = dbConnector.getErrorMessage();
    if (fehler == null) {
      jLabel1.setText("Datenbank wurde erfolgreich verbunden!");
    } else {
      jLabel1.setText("Fehlermeldung: " + fehler);
    }
  }

  public void jbEintragen_ActionPerformed(ActionEvent evt) {
    String vorname = tVorname.getText();
    String name = tName.getText();
    String motto = tMotto.getText();
      
    int userid=-1;
    dbConnector.executeStatement("SELECT id FROM user WHERE name='"+name+"' AND vorname='"+vorname+"'");
    QueryResult r = dbConnector.getCurrentQueryResult();
    if (r.getRowCount() == 0) {
      dbConnector.executeStatement("INSERT INTO user(name,vorname) VALUES('"+name+"','"+vorname+"')");
      dbConnector.executeStatement("SELECT id FROM user WHERE name='"+name+"' AND vorname='"+vorname+"'");
      r = dbConnector.getCurrentQueryResult();
      userid = Integer.parseInt(r.getData()[0][0]);
    } else {
      userid = Integer.parseInt(r.getData()[0][0]);
    }  

    int mottoid=-1;

    //Hier Quelltext einfügen

      
  }  

  // Ende Methoden
  
  public static void main(String[] args) {
    new Nutzer();
  }
}
