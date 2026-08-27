 

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

// Imports für GUI Komponenten
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

// Imports für Tableview
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

// Imports für Scenenwechsel
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Controller {
    
    /* ------------------------------
     * Elemente der Scene1  
     * ------------------------------
     */
    
    @FXML
    private Button anmeldeButton;

    @FXML
    private TextField idField;

    @FXML
    private TextField passwortField;
    

   
    /* ------------------------------
     * Elemente des Mensa Homescreen 
     * ------------------------------
     */
    @FXML
    private Button buttonScene2;
    
    @FXML
    private Button verkaufenButton;
    
    @FXML
    private TableView<ObservableList<String>> ProduktTable;
    
    @FXML
    private TableColumn<ObservableList<String>, String> NameColumn;
    
    @FXML
    private TableColumn<ObservableList<String>, String>MengeColumn;
    
    @FXML
    private TableColumn<ObservableList<String>, String> PreisColumn;
    
    @FXML   
    private TextField artikelIDField;
    
    @FXML   
    private TextField userIDField;
    
    @FXML
    private TextField mengeField;
    
    @FXML
    private Button homeButton;
    
    @FXML
    private Button aufladenButton;
    
    @FXML
    private Button hinzufuegenButton;
    
    @FXML
    private Button statistikButton;

    //Elemente Mensa Aufladen Screen
    
    @FXML
    private Button homeButton2;
    
    @FXML
    private Button aufladenButton2;
    
    @FXML
    private Button hinzufuegenButton2;
    
    @FXML
    private Button statistikButton2;
    
    @FXML
    private Button buttonScene12;
    
    @FXML
    private TextField userIDField2;
    
    @FXML
    private TextField betragField;
    
    @FXML
    private Button aufladenKnopf;
    
    //Elemente Mensa Hinzufügen Screen
    
     @FXML
    private Button homeButton3;
    
    @FXML
    private Button aufladenButton3;
    
    @FXML
    private Button hinzufuegenButton3;
    
    @FXML
    private Button statistikButton3;
    
    @FXML
    private Button buttonScene13;
    
    //Elemente Mensa Statistik Screen
    
    @FXML
    private Button homeButton4;
    
    @FXML
    private Button aufladenButton4;
    
    @FXML
    private Button hinzufuegenButton4;
    
    @FXML
    private Button statistikButton4;
    
    @FXML
    private Button buttonScene14;
    
    //Elemente Nutzer Main Screen
    
    @FXML
    private Button nutzerHome1;
    
    @FXML
    private Button changePasswort1;
    
    @FXML
    private Button nutzerLogout1;
    
    @FXML
    private Label kontostandLabel;
 
    // Verbindung vom Controller zum Model   
    private Login login;
    private Mensa mensa; 
    private Nutzer nutzer;
    
    public Controller(){
        login = ModelLoader.getModel();
        
    }
    
    public void mensaInitialize(){
        NameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        MengeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        PreisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        zeigeLager();
    }

    @FXML
    void login(ActionEvent event) {
        
        int uID = Integer.parseInt(idField.getText());
        String passwort        = passwortField.getText(); 
        login.login(uID, passwort);
    
        if (login.login(uID, passwort) instanceof Mensa){
            mensa = (Mensa)login.login(uID, passwort);
            try {
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                switchToMensaNow(stage);
            } catch (IOException e) {
                e.printStackTrace();
                // Optional: Fehlermeldung für den Nutzer anzeigen
            }
        }
        else if (login.login(uID, passwort) instanceof Nutzer){
            nutzer = (Nutzer)login.login(uID, passwort);
            try {
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                switchToNutzerNow(stage);
            } catch (IOException e) {
                e.printStackTrace();
                // Optional: Fehlermeldung für den Nutzer anzeigen
            }
        }
           
    }
    
    
    @FXML
    public void zeigeLager() {
        if (mensa != null) {
            ObservableList<ObservableList<String>> tabelleDaten = FXCollections.observableArrayList();
            ArrayList<String> datenAusDb = mensa.getLager();
            // Immer 3 Werte auf einmal als eine Zeile zusammenfassen:
            for (int i = 0; i < datenAusDb.size(); i += 3) {
                ObservableList<String> zeile = FXCollections.observableArrayList();
                
                zeile.add(datenAusDb.get(i));     // Index 0: Name
                zeile.add(datenAusDb.get(i + 1)); // Index 1: Preis
                zeile.add(datenAusDb.get(i + 2)); // Index 2: Menge
                tabelleDaten.add(zeile);
            }
            // Der TableView übergeben
            ProduktTable.setItems(tabelleDaten);
        }
    }
    
    @FXML
    public void verkaufen(ActionEvent event) {
        if (mensa != null) {
            int pID = Integer.parseInt(artikelIDField.getText());
            int uID = Integer.parseInt(userIDField.getText());
            int menge = Integer.parseInt(mengeField.getText());
            
            mensa.verkaufen(pID, uID, menge);
            zeigeLager();
        }
    }
    
    @FXML
    public void aufladen(ActionEvent event) {
        if (mensa != null) {
            System.out.println("hey");
            int uID = Integer.parseInt(userIDField2.getText());
            float betrag = Float.parseFloat(betragField.getText());
            
            mensa.geldAufladen(uID, betrag);
        }
    }
    
    @FXML
    public void verkaufEinfuegen(MouseEvent event) {
        String selectedID = Integer.toString( ProduktTable.getSelectionModel().getSelectedIndex());
        artikelIDField.setText(selectedID);
    }

    @FXML
    public void switchtoScene1(ActionEvent event) throws IOException{      
        Parent root = FXMLLoader.load(getClass().getResource("scenes/scene1.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    //Mensa Szenenwechsel Methoden
    @FXML
    public void switchToAufladen(ActionEvent event) throws IOException{
         FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/mensaaufladen.fxml"));
        Parent root = loader.load();
        Controller neuerController = loader.getController();
        neuerController.setMensa(mensa);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void switchToHinzufuegen(ActionEvent event) throws IOException{
        
         FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/mensahinzufuegen.fxml"));
        Parent root = loader.load();
        Controller neuerController = loader.getController();
        neuerController.setMensa(mensa);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void switchToStatistik(ActionEvent event) throws IOException{
        
         FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/mensastatistik.fxml"));
        Parent root = loader.load();
        Controller neuerController = loader.getController();
        neuerController.setMensa(mensa);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
        
    @FXML
    public void switchToMensa(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        switchToMensaNow(stage);
    }
    
    public void switchToMensaNow(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/scene2.fxml"));
        Parent root = loader.load();
        Controller neuerController = loader.getController();
        neuerController.setMensa(mensa);
        neuerController.mensaInitialize();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
 
    public void setMensa(Mensa pMensa) {
        this.mensa = pMensa;
    }
    
    //Nutzer Szenenwechsel Methoden
    
    public void nutzerInitialize() {
        
    }
    
    public void switchToNutzerNow(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/nutzermainscreen.fxml"));
        Parent root = loader.load();
        Controller neuerController = loader.getController();
        neuerController.setNutzer(nutzer);
        neuerController.nutzerInitialize();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void switchToNutzer(ActionEvent event) throws IOException{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/nutzermainscreen.fxml"));
        Parent root = loader.load();
        Controller neuerController = loader.getController();
        neuerController.setNutzer(nutzer);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void setNutzer(Nutzer pNutzer) {
        this.nutzer = pNutzer;
    }
        
    }   


