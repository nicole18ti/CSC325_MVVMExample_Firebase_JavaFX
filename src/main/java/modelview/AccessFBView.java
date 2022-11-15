package modelview;

/**
 * Tiffany Chan
 * CSC325
 * Lab 10
 * 
 * 1. done
 * 2. done
 * 3, 4, 5 didn't work on row, but worked on textArea
 * 
 * 6. enabled google and facebook authentication - done
 * 7. set up the storge on firebase and have one picture in it
 */

import com.mycompany.mvvmexample.App;
import viewmodel.AccessDataViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.mycompany.mvvmexample.FirestoreContext;
import com.mycompany.mvvmexample.FirestoreContext;
import com.mycompany.mvvmexample.FirestoreContext;
import com.mycompany.mvvmexample.FirestoreContext;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import models.Person;

public class AccessFBView {

    @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;
    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TableView outputField;
    @FXML
    private Button registerField;
    @FXML
    private TableColumn nameColumn = new TableColumn<Person, String>("Name");
    @FXML
    private TableColumn majorColumn = new TableColumn<Person, String>("Major");
    @FXML
    private TableColumn ageColumn = new TableColumn<Person, String>("Age");

    private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();

    private Person person;

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    void initialize() {

        AccessDataViewModel accessDataViewModel = new AccessDataViewModel();
        nameField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        majorField.textProperty().bindBidirectional(accessDataViewModel.userMajorProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
        clearTextField();
    }

    @FXML
    private void readRecord(ActionEvent event) {
        // readFirebase();
    }

    /**
     * Question 1: Clear the testField when click addData and read from Firebase
     */
    public void clearTextField() {
        nameField.clear();
        majorField.clear();
        ageField.clear();
    }

    /**
     * Question 2: Modify the project so won't show repeat of the retrieved data
     */
    public void clearTextArea() {
        for (int i = 0; i < outputField.getItems().size(); i++) {
            outputField.getItems().clear();
            // tableView/outputField.getItems().clear(); should do the same thing
        }
    }

    public void addData() {

        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());
        // Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        clearTextField();
    }

    

    
    public boolean readFirebase() {
        key = false;
        clearTextArea(); // put it at the beginning, so it would clear every time.
        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = App.fstore.collection("References").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();

            if (documents.size() > 0) {
                System.out.println("Outing....");

                for (QueryDocumentSnapshot document : documents) {
                    /*
                    outputField.setText(outputField.getItems() + document.getData().get("Name") + " , Major: "
                            + document.getData().get("Major") + " , Age: "
                            + document.getData().get("Age") + " \n ");
                    System.out.println(document.getId() + " => " + document.getData().get("Name"));
     */
 
                    person = new Person(String.valueOf(document.getData().get("Name")),
                            document.getData().get("Major").toString(),
                            Integer.parseInt(document.getData().get("Age").toString()));
                    listOfUsers.add(person);
                    nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Name"));
                    majorColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Major"));
                    ageColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("Age"));

                    listOfUsers.add(person);

                    outputField.getItems().add(person);

                }
            } else {
                System.out.println("No data");
            }
            key = true;

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;

    }
    
    
    /**
     * Enable authentication?
     */
 /*
    public void sendVerificationEmail() {
        try {
            UserRecord user = App.fauth.getUser("name");
            //String url = user.getPassword();

        } catch (Exception e) {
        }
    }

    /**
     *
     * Fix the registration form - done
     *
     * Return: Successfully created new user: "UserId"
     */
 /*
    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                //.setEmail(nameField.getText().toString().trim() + "tiffantchan@example.com")
                .setEmail(majorField.getText().toString().trim() + "@example.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName(nameField.getText().trim())
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
     */
}
