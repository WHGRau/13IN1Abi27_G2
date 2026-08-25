 

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

// Imports für GUI Komponenten
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

// Imports für Tableview
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
     * Elemente der Scene2  
     * ------------------------------
     */
    @FXML
    private Button buttonScene2;
     

    
    // Verbindung vom Controller zum Model   
    private Login login; 
    
    public Controller(){
        login = ModelLoader.getModel();
        
    }
    
    public void initialize(){
        // Prüfen, ob scene1 geladen wird
        //tabelViewRefresh();
    }

    @FXML
    void login(ActionEvent event) {
        
        int uID = Integer.parseInt(idField.getText());
        String passwort        = passwortField.getText(); 
        login.login(uID, passwort);
           
    }
    
    // Hilfsmethode für die Tableview
   
    
    // Hilfsmethode für die Tableview, wird nicht benötigt, wenn mit ArrayLists gearbeitet wird.
    //private ArrayList<ToDo> convertToArrayList(List<ToDo> pList){
        //ArrayList<ToDo> ausgabe = new ArrayList<ToDo>();
        //pList.toFirst();
        //while(pList.hasAccess()){            
            //ausgabe.add(pList.getContent());
            //pList.next();
        //}
        //return ausgabe;
    //}
    
    //@FXML
    //void tableViewClicked(MouseEvent event) {
       // int selectedID = toDoTableView.getSelectionModel().getSelectedIndex();
       // model.removeToDo(selectedID);
       // tabelViewRefresh();
    //}

    @FXML
    public void switchtoScene1(ActionEvent event) throws IOException{      
        Parent root = FXMLLoader.load(getClass().getResource("scenes/scene1.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        initialize();
    }
    
    @FXML
    public void switchtoScene2(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("scenes/scene2.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }     
    
}

