/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict373_a2_33276203;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    private Person selected;
    private Person rootPerson;

    @FXML private Label statusBar;
    @FXML private Label newstatusBar;
    @FXML private TreeView<Person> familyTree;
    @FXML private TextField fName;
    @FXML private TextField lName;
    @FXML private TextField lNameAfter;
    @FXML private TextArea textArea;
    @FXML private RadioButton male;
    @FXML private RadioButton female;
    @FXML private TextField streetName;
    @FXML private TextField streetNum;
    @FXML private TextField suburb;
    @FXML private TextField postcode;
    @FXML private Button addRelative;
    @FXML private ComboBox<String> comboRel;
    @FXML private TextField newfName;
    @FXML private TextField newlName;
    @FXML private TextField newlNameAfter;
    @FXML private RadioButton newmale;
    @FXML private RadioButton newfemale;
    @FXML private TextArea newTextArea;

    @FXML
    private void handleNew() {
        if (familyTree.getRoot() != null) {
            familyTree.setRoot(null);
        }

        MultipleSelectionModel<TreeItem<Person>> msm = familyTree.getSelectionModel();
        //Create root
        rootPerson = new Person();
        rootPerson.setFname("Root Person");
        populateTree(rootPerson, familyTree.getRoot());

        handleTreeItemClick();
    }

    @FXML
    private void onTreeItem() {
        System.out.println("Selected tree item");
    }

    @FXML
    private void handleOpen() {
        statusBar.setText("Opening Tree");
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Tree", "*.tree");
        File file = fc.showOpenDialog(statusBar.getScene().getWindow());
        if (file != null) {
            loadPerson(file);
        }
    }

    @FXML
    private void handleSave() {
        statusBar.setText("Saving...");
        System.out.println("Save");

        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Tree", "*.tree");
        File file = fc.showSaveDialog(statusBar.getScene().getWindow());
        if (file != null) {
            saveTree(rootPerson, file);
        }
    }


    @FXML
    private void handleEditDetails() {
        selected.setFname(fName.getText());
        selected.setLname(lName.getText());
        selected.setLnameAfter(lNameAfter.getText());
        selected.setBio(textArea.getText());

        if (male.isSelected()) { selected.setGender(Person.MALE); }
        else { selected.setGender(Person.FEMALE); }

        Address temp = new Address();

        temp.setStreet(streetName.getText());
        temp.setSuburb(suburb.getText());
        int streetNumber = 0;
        if ( streetNum.getText().equals("") ) {
            try {
                streetNumber = Integer.parseInt(streetNum.getText());
                temp.setNumber(streetNumber);
            } catch (Exception e) {
                statusBar.setText("Invalid street number.");
            }
        }
        int postCodeInt = 0;
        if ( postcode.getText().equals("")) {
            try {
                postCodeInt = Integer.parseInt(postcode.getText());
                temp.setPostcode(postCodeInt);
            } catch (Exception e) {
                statusBar.setText("Invalid postcode.");
            }
        }

        selected.setAddress(temp);

        familyTree.refresh();
    }

    @FXML
    private void handleAddRelative(ActionEvent event) {
        //Create a new window for adding a new relative
        Parent root;
        try {
            Stage stage = (Stage) addRelative.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader (getClass().getResource("newRelative.fxml"));
            loader.setController(this);
            root = loader.load();
            stage = new Stage();
            stage.setTitle("Add Relative to " + selected.getFname() + " " + selected.getLname());
            stage.setScene(new Scene(root, 300, 600));
            stage.show();
        } catch (IOException e) {e.printStackTrace();}
    }

    @FXML
    private void handleNewRelative() {
        String dialogMsg = "";

        Person tempPerson = new Person();
        tempPerson.setFname(newfName.getText());
        tempPerson.setLname(newlName.getText());
        tempPerson.setLnameAfter(newlNameAfter.getText());
        if (newmale.isSelected()) { tempPerson.setGender(Person.MALE); }
        else { tempPerson.setGender(Person.FEMALE); }
        tempPerson.setBio(newTextArea.getText());
        String tmp = comboRel.getValue();


        switch (tmp) {
            case "Parent":
                boolean can = selected.addParent(tempPerson);
                if (!can) {
                    dialogMsg = "Only two parents allowed";
                } else {
                    dialogMsg = "Parent added.";
                }
                break;
            case "Child":
                selected.addChild(tempPerson);
                System.out.println("Child added.");
                dialogMsg = "Child added";
                break;
            case "Spouse":
                selected.addSpouse(tempPerson);
                dialogMsg = "Spouse added";
                break;
        }

        System.out.println(tmp);

        repopulateTree();
        familyTree.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hey!");
        alert.setHeaderText(null);
        alert.setContentText(dialogMsg);
        alert.showAndWait();

        Stage stage = (Stage) newlName.getScene().getWindow();
        stage.close();
    }

    public void saveTree(Person person, File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // write object to file
            oos.writeObject(person);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadPerson(File file) {
        System.out.println("Opening file");
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // write object to file
            Object obj = ois.readObject();
            System.out.println(obj);
            if (obj instanceof Person) {
                rootPerson = (Person) obj;
                System.out.println(rootPerson.getFname());
                repopulateTree();
                handleTreeItemClick();
            }
        } catch (IOException ex) {
            System.out.println("error");
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("error");
            e.printStackTrace();
        }


    }

    public void handleTreeItemClick() {
        familyTree.setOnMouseClicked(null);
        //Handle a treeitem being clicked
        if (familyTree != null) {
            familyTree.setOnMouseClicked(event -> {
                if(event.getClickCount()==1){
                    try {
                        if (familyTree.getSelectionModel().getSelectedItem().getValue() != null) {
                            try {
                                Person item = (Person) familyTree.getSelectionModel().getSelectedItem().getValue();
                                selected = item;
                                populatePersonInfo();
                            } catch(Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            });
        }
    }

    public void repopulateTree() {
        populateTree(rootPerson, null);
    }


    public void populateTree(Person root, TreeItem parent) {
        clearPersonInfo();
        //Set the gender icon
        String imagePath = "";
        if (root.getGender() == Person.MALE) { imagePath = "img/male.png"; }
        else { imagePath = "img/female.png"; }
        final Node genderIcon =  new ImageView(new Image(getClass().getResourceAsStream(imagePath)));

        //Create a new tree item
        TreeItem p = new TreeItem(root, genderIcon);

        //Test to see if there is a root
        if (familyTree.getRoot() == null || parent == null) {
            System.out.println("Tree is null");
            familyTree.setRoot(p);
        } else {
            parent.getChildren().add(p);
        }

        TreeItem parents = new TreeItem("Parents:");
        p.getChildren().add(parents);
        for ( int i = 0; i < Person.MAX_PARENTS; i++) {
            if (root.getParent(i) != null) {
                populateTree(root.getParent(i), parents);
            }
        }

        TreeItem child = new TreeItem("Children:");
        p.getChildren().add(child);
        for (int i = 0; i < root.getChildren().size(); i++) {
            if (root.getChild(i) != null) {
                populateTree(root.getChild(i), child);
            }
        }

        TreeItem spouse = new TreeItem("Spouse:");
        p.getChildren().add(spouse);
        if (root.getSpouse() != null) {
            populateTree(root.getSpouse(), spouse);
        }
    }

    public void populatePersonInfo() {
        fName.setText(selected.getFname());
        lName.setText(selected.getLname());
        lNameAfter.setText(selected.getLnameAfter());
        textArea.setText(selected.getBio());
        if (selected.getGender().equals(Person.MALE)) {
            male.setSelected(true);
            female.setSelected(false);
        } else {
            male.setSelected(false);
            female.setSelected(true);
        }
        
        //Populate address
        streetName.setText(selected.getAddress().getStreet());
        streetNum.setText(Integer.toString(selected.getAddress().getNumber()));
        suburb.setText(selected.getAddress().getSuburb());
        postcode.setText(Integer.toString(selected.getAddress().getPostcode()));
    }

    public void clearPersonInfo() {
        fName.setText("");
        lName.setText("");
        lNameAfter.setText("");
        textArea.setText("");
        streetNum.setText("");
        streetName.setText("");
        suburb.setText("");
        postcode.setText("");
    }
}