package sigvertsen.c482.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sigvertsen.c482.main;
import sigvertsen.c482.model.Inventory;
import sigvertsen.c482.model.Part;
import sigvertsen.c482.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class is the controller for the Add Product page*/
public class addProductController implements Initializable {

    /** initialize method for the Add Product page controller
     * sets the items to be put in the top table and bottom table
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProduct_top_tbl.setItems(Inventory.getAllParts());
        addProduct_top_partID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProduct_top_partName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProduct_top_inventory_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProduct_top_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProduct_bottom_tbl.setItems(tempAssociateParts);
        addProduct_bottom_partID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProduct_bottom_partName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProduct_bottom_inv_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProduct_bottom_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    // to store associated parts temporarily
    private final ObservableList<Part> tempAssociateParts = FXCollections.observableArrayList();
    ObservableList<Part> tempPartsList = FXCollections.observableArrayList();
    Stage stage;
    Parent scene;

    @FXML
    private TextField addProduct_productName_field;

    @FXML
    private TextField addProduct_inventory_field;

    @FXML
    private TextField addProduct_price_field;

    @FXML
    private TextField addProduct_max_field;

    @FXML
    private TextField addProduct_min_field;

    @FXML
    private TextField addProduct_searchbar;

    @FXML
    private TableView<Part> addProduct_top_tbl;

    @FXML
    private TableColumn<Part, Integer> addProduct_top_partID_col;

    @FXML
    private TableColumn<Part, String> addProduct_top_partName_col;

    @FXML
    private TableColumn<Part, Integer> addProduct_top_inventory_col;

    @FXML
    private TableColumn<Part, Double> addProduct_top_price_col;

    @FXML
    private TableView<Part> addProduct_bottom_tbl;

    @FXML
    private TableColumn<Part, Integer> addProduct_bottom_partID_col;

    @FXML
    private TableColumn<Part, String> addProduct_bottom_partName_col;

    @FXML
    private TableColumn<Part, Integer> addProduct_bottom_inv_col;

    @FXML
    private TableColumn<Part, Double> addProduct_bottom_price_col;

    @FXML
    private Label addProduct_search_error_msg_lbl;

    @FXML
    private Label addProduct_input_error_lbl;

    /** event handler for Add Part button
     * adds the selected item from the top table to the temporary associated parts list
     * */
    @FXML
    void onAction_add_part(ActionEvent event) { // add part to bottom list
        Part selectedPartTopTbl = addProduct_top_tbl.getSelectionModel().getSelectedItem();
        tempAssociateParts.add(selectedPartTopTbl);
    }

    /** event handler for Remove Associated Part button
     * the user confirms the removal of an associated part
     * */
    @FXML
    void onAction_RAP(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
        Optional<ButtonType> result =  alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part selectedPartBottomTbl = addProduct_bottom_tbl.getSelectionModel().getSelectedItem();
            tempAssociateParts.remove(selectedPartBottomTbl); // remove from bottom table
        }
    }

    /** event handler for Save button
     * input from text fields are used to create a Product object and add to inventory
     * */
    @FXML
    void onAction_save_fields(ActionEvent event) throws IOException {
        try {
//            int id = prod_id;
            String name = addProduct_productName_field.getText();
            int stock = Integer.parseInt(addProduct_inventory_field.getText());
            double price = Double.parseDouble(addProduct_price_field.getText());
            int max = Integer.parseInt(addProduct_max_field.getText());
            int min = Integer.parseInt(addProduct_min_field.getText());

            Product newProd = new Product(main.getProd_id(), name, price, stock, min, max);

            // add all associated parts to Product object
            for (Part tempAssociatePart : tempAssociateParts) {
                newProd.addAssociatedPart(tempAssociatePart);
            }
            // MinMax error
            if (max < min) {
                throw new MinMaxException();
            }
            // Inventory error
            if (stock < min || stock > max) {
                throw new InventoryException();
            }

            // add Product to inventory
            Inventory.addProduct(newProd);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }

        catch (NumberFormatException e) { // invalid inputs
            addProduct_input_error_lbl.setText("Please enter valid inputs.");
        }
        catch(MinMaxException e) { // min is greater than max
            addProduct_input_error_lbl.setText("MAX must be greater than MIN.");
        }
        catch(InventoryException e) { // inventory not between min and max
            addProduct_input_error_lbl.setText("INVENTORY must be between MIN and MAX.");
        }

    }

    /** event handler for Cancel button
     * the Cancel button returns the user to the Main page
     * */
    @FXML
    void onAction_return_to_main(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** RUNTIME ERROR event handler Add Product search field
     * the user input in the searchbar is used to filter Table items
     * Part Name is a String type. Part ID is an int type.
     * I was getting an error originally because I did not convert the types of the Part ID input.
     * For this to function to work correctly, I converted the part ID to String so the contains() method could be used.
     * The contains() method returns true if the string contains the parameter string.
     * The function ran without errors after the change.
     * */
    @FXML
    void onAction_addProduct_part_search(ActionEvent event) {
        tempPartsList.clear(); // clear temp parts list
        addProduct_search_error_msg_lbl.setText(""); // reset error label
        String userInputParts = addProduct_searchbar.getText();

        // filter table items
        for (Part part : Inventory.getAllParts()) {
            if (part.getName().contains(userInputParts) || String.valueOf(part.getId()).contains(userInputParts)) {
                tempPartsList.add(part);
            }
        }
        addProduct_top_tbl.setItems(tempPartsList); // set items to table
        if (tempPartsList.size() == 0) { // no parts found
            addProduct_search_error_msg_lbl.setText("No parts found.");
        }
        if (tempPartsList.size() == 1) { // highlight item if filtered to one
            addProduct_top_tbl.getSelectionModel().select(0);
        }
    }

    /** custom exception for MinMax error*/
    static class MinMaxException extends Exception {
        public MinMaxException() {}
    }

    /** customer exception for Inventory error*/
    static class InventoryException extends Exception {
        public InventoryException() {}
    }
}
