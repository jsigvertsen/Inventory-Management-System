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
import sigvertsen.c482.model.Inventory;
import sigvertsen.c482.model.Part;
import sigvertsen.c482.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class is the controller for the Modify Product page*/
public class modifyProductController implements Initializable {
    /** initialize method for the Modify Product page
     * top table is set to include all parts
     * bottom table is set to include all associated parts of the product to be modified
     * all fields are set to match the Product object to be modified
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyProduct_top_tbl.setItems(Inventory.getAllParts());
        modifyProduct_top_partID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProduct_top_partName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProduct_top_inventory_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProduct_top_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

        tempAssociateParts = selectedObject.getAllAssociatedParts();

        modifyProduct_bottom_tbl.setItems(tempAssociateParts);
        modifyProduct_bottom_partID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProduct_bottom_partName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProduct_bottom_inv_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProduct_bottom_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyProduct_productID_field.setText(String.valueOf(selectedObject.getId()));
        modifyProduct_productName_field.setText(selectedObject.getName());
        modifyProduct_inventory_field.setText(String.valueOf(selectedObject.getStock()));
        modifyProduct_price_field.setText(String.valueOf(selectedObject.getPrice()));
        modifyProduct_max_field.setText(String.valueOf(selectedObject.getMax()));
        modifyProduct_min_field.setText(String.valueOf(selectedObject.getMin()));

    }

    private ObservableList<Part> tempAssociateParts = FXCollections.observableArrayList();
    ObservableList<Part> tempPartsList = FXCollections.observableArrayList();
    Product selectedProd = mainController.getProductHandOff(); // handoff of selected product

    Product selectedObject = Inventory.lookupProduct(selectedProd.getId()); // id of selected object
    Stage stage;
    Parent scene;

    @FXML
    private TextField modifyProduct_productID_field;

    @FXML
    private TextField modifyProduct_productName_field;

    @FXML
    private TextField modifyProduct_inventory_field;

    @FXML
    private TextField modifyProduct_price_field;

    @FXML
    private TextField modifyProduct_max_field;

    @FXML
    private TextField modifyProduct_min_field;

    @FXML
    private TextField modifyProduct_searchbar;

    @FXML
    private TableView<Part> modifyProduct_top_tbl;

    @FXML
    private TableColumn<Part, Integer> modifyProduct_top_partID_col;

    @FXML
    private TableColumn<Part, String> modifyProduct_top_partName_col;

    @FXML
    private TableColumn<Part, Integer> modifyProduct_top_inventory_col;

    @FXML
    private TableColumn<Part, Double> modifyProduct_top_price_col;

    @FXML
    private TableView<Part> modifyProduct_bottom_tbl;

    @FXML
    private TableColumn<Part, Integer> modifyProduct_bottom_partID_col;

    @FXML
    private TableColumn<Part, String> modifyProduct_bottom_partName_col;

    @FXML
    private TableColumn<Part, Integer> modifyProduct_bottom_inv_col;

    @FXML
    private TableColumn<Part, Double> modifyProduct_bottom_price_col;

    @FXML
    private Label modifyProduct_search_error_msg_lbl;

    @FXML
    private Label modifyProduct_input_error_lbl;

    /** event handler for Add Part button
     * the selected part from the table is added to the bottom table
     * */
    @FXML
    void onAction_part_add(ActionEvent event) {
        // add selected item to temp associated parts
        Part selectedPartTopTbl = modifyProduct_top_tbl.getSelectionModel().getSelectedItem();
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
            Part selectedPartBottomTbl = modifyProduct_bottom_tbl.getSelectionModel().getSelectedItem();
            // remove part
            tempAssociateParts.remove(selectedPartBottomTbl);
        }

    }

    /** event handler for Save button
     * input from text fields are used to create a Product object and add to inventory
     * Exceptions are raised for MinMax errors and Inventory errors
     * */
    @FXML
    void onAction_save(ActionEvent event) throws IOException {
        try {
            int indexSelectedRow =  Inventory.getAllProducts().indexOf(selectedObject);
            int id = selectedObject.getId();
            String name = modifyProduct_productName_field.getText();
            int stock = Integer.parseInt(modifyProduct_inventory_field.getText());
            double price = Double.parseDouble(modifyProduct_price_field.getText());
            int max = Integer.parseInt(modifyProduct_max_field.getText());
            int min = Integer.parseInt(modifyProduct_min_field.getText());

            // MinMax error
            if (max < min) {
                throw new MinMaxException();
            }

            // Inventory error
            if (stock < min || stock > max) {
                throw new InventoryException();
            }

            Product newProd = new Product(id, name, price, stock, min, max);
            Inventory.updateProduct(indexSelectedRow, newProd);

            // add all associated parts selected to Product object
            for (Part associatedPart : tempAssociateParts) {
                newProd.addAssociatedPart(associatedPart);
            }

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NumberFormatException e) { // invalid inputs
            modifyProduct_input_error_lbl.setText("Please enter valid inputs.");
        }
        catch(MinMaxException e) { // min is greater than max
            modifyProduct_input_error_lbl.setText("MAX must be greater than MIN.");
        }
        catch(InventoryException e) { // inventory not between min and max
            modifyProduct_input_error_lbl.setText("INVENTORY must be between MIN and MAX.");
        }


    }

    /** event handler for Cancel button
     * returns the user to the Main page
     * */
    @FXML
    void onAction_cancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** event handler for Part Search field
     * the user input in the searchbar is used to filter table items
     * */
    @FXML
    void onAction_modifyProduct_part_search(ActionEvent event) {
        tempPartsList.clear();
        modifyProduct_search_error_msg_lbl.setText("");
        String userInputParts = modifyProduct_searchbar.getText();

        // search for id and part name
        for (Part part : Inventory.getAllParts()) {
            if (part.getName().contains(userInputParts) || String.valueOf(part.getId()).contains(userInputParts)) {
                tempPartsList.add(part); // add matching parts to temp list
            }
        }
        modifyProduct_top_tbl.setItems(tempPartsList); // set table to temp list
        if (tempPartsList.size() == 0) { // no parts found
            modifyProduct_search_error_msg_lbl.setText("No parts found.");
        }
        if (tempPartsList.size() == 1) { // highlight row if filtered to one result
            modifyProduct_top_tbl.getSelectionModel().select(0);
        }
    }

    /** custom exception for MinMax error*/
    static class MinMaxException extends Exception {
        public MinMaxException() {}
    }

    /** custom exception for Inventory error*/
    static class InventoryException extends Exception {
        public InventoryException() {}
    }
}
