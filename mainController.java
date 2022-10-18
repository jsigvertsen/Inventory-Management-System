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

/** This class is the controller for the Main page*/
public class mainController implements Initializable {

    /** initialize method for the Main page controller
     * parts table is set to show all parts that exist
     * products table is set to show all products that exist
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        main_parts_tbl.setItems(Inventory.getAllParts());
        main_parts_partID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        main_parts_partName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        main_parts_inv_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        main_parts_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

        main_products_tbl.setItems(Inventory.getAllProducts());
        main_products_productID_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        main_products_product_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        main_products_inv_col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        main_products_price_col.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

//    ObservableList<Part> tempPartsList = FXCollections.observableArrayList();
    ObservableList<Product> tempProductList = FXCollections.observableArrayList();
    Stage stage;
    Parent scene;

    private static Part selectedPartRow = null;

    private static Product selectedProductRow = null;

    @FXML
    private TableView<Part> main_parts_tbl;

    @FXML
    private TableColumn<Part, Integer> main_parts_partID_col;

    @FXML
    private TableColumn<Part, String> main_parts_partName_col;

    @FXML
    private TableColumn<Part, Integer> main_parts_inv_col;

    @FXML
    private TableColumn<Part, Double> main_parts_price_col;

    @FXML
    private TableView<Product> main_products_tbl;

    @FXML
    private TableColumn<Product, Integer> main_products_productID_col;

    @FXML
    private TableColumn<Product, String> main_products_product_name_col;

    @FXML
    private TableColumn<Product, Integer> main_products_inv_col;

    @FXML
    private TableColumn<Product, Double> main_products_price_col;

    @FXML
    private TextField main_parts_searchbar;

    @FXML
    private TextField main_products_searchbar;

    @FXML
    private Label main_parts_srch_error_lbl;

    @FXML
    private Label main_products_srch_error_lbl;

    // handoff part to next scene
    public static Part getPartHandOff() {
        return selectedPartRow;
    }

    // handoff product to next scene
    public static Product getProductHandOff() {
        return selectedProductRow;
    }

    /** event handler for Add Part button
     * sends the user to the Add Part page
     * */
    @FXML
    void onAction_part_add(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/addPart.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** event handler for Modify Part button
     * sends the user to the Modify Part page
     * if nothing is selected an error is thrown
     * */
    @FXML
    void onAction_part_modify(ActionEvent event) throws IOException {
        if (main_parts_tbl.getSelectionModel().getSelectedItem() == null) {
            main_parts_srch_error_lbl.setText("Nothing selected. Highlight a part to modify it.");
        }
        else {
            selectedPartRow = main_parts_tbl.getSelectionModel().getSelectedItem();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/modifyPart.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** event handler for Delete Part button
     * the selected part is deleted after confirmation from the user
     * */
    @FXML
    void onAction_part_delete(ActionEvent event) {
        // check if something is selected
        if (main_parts_tbl.getSelectionModel().getSelectedItem() == null) {
            main_parts_srch_error_lbl.setText("Nothing selected. No parts were deleted.");
        }
        else {
            // confirm delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedPartRow = main_parts_tbl.getSelectionModel().getSelectedItem();
                // delete part from inventory
                if (Inventory.deletePart(selectedPartRow)) {
                    Inventory.deletePart(selectedPartRow);
                }
                // reset search error label
                main_parts_srch_error_lbl.setText("");
            }
        }
    }

    /** event handler for the Add Product button
     * sends the user to the Add Product page
     * */
    @FXML
    void onAction_product_add(ActionEvent event) throws IOException{
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/addProduct.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** event handler for the Modify Product button
     * sends the user to the Modify Product page
     * if nothing is selected, an error is thrown
     * */
    @FXML
    void onAction_product_modify(ActionEvent event) throws IOException{

        // check if something is selected
        if (main_products_tbl.getSelectionModel().getSelectedItem() == null) {
            main_products_srch_error_lbl.setText("Nothing selected. Highlight a product to modify it.");
        }
        else {
            // set selected row
            selectedProductRow = main_products_tbl.getSelectionModel().getSelectedItem();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/modifyProduct.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** event handler for the Delete Product button
     * the selected product is deleted after confirmation from the user
     * if the product has associated parts, an error is thrown and the product is not deleted
     * if nothing is selected, an error is thrown and nothing is deleted
     * */
    @FXML
    void onAction_product_delete(ActionEvent event) {

        try {
            selectedProductRow = main_products_tbl.getSelectionModel().getSelectedItem();

            // throw error if product has associated parts
            if (selectedProductRow.getAllAssociatedParts().size() > 0) {
                throw new AssociatedPartsException();
            }

            // confirm delete
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
            Optional<ButtonType> result =  alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // delete from inventory
                if (Inventory.deleteProduct(selectedProductRow)) {
                    Inventory.deleteProduct(selectedProductRow);
                }

                // reset error label
                main_products_srch_error_lbl.setText("");
            }
        }
        catch (AssociatedPartsException e) {
            main_products_srch_error_lbl.setText("This product has associated parts. Delete not allowed.");
        }

        // nothing selected error
        catch (NullPointerException e) {
            main_products_srch_error_lbl.setText("Nothing selected. No products were deleted.");
        }
    }

    /** event handler for the Exit button
     * closes the application
     * */
    @FXML
    void onAction_exit(ActionEvent event) {
        System.exit(0);
    }

    /** event handler for the Part Search field
     * the user input in the searchbar is used to filter table items
     * if no parts are found, an error is thrown
     * */
    @FXML
    void onAction_part_search(ActionEvent event) {
        try {
            main_parts_tbl.getSelectionModel().select(null); // reset highlight
            main_parts_srch_error_lbl.setText(""); // reset error label
            String userInputParts = main_parts_searchbar.getText();

            main_parts_tbl.setItems(Inventory.lookupPart(userInputParts)); // filter table items

            if (Inventory.lookupPart(userInputParts).size() == 0) { // if no parts found
                main_parts_srch_error_lbl.setText("No parts found.");
            }
            if (Inventory.lookupPart(userInputParts).size() == 1) { // highlight item if filtered to 1
                main_parts_tbl.getSelectionModel().select(0);
            }
        }
        catch (NumberFormatException e) { // nothing in search bar
            main_parts_tbl.setItems(Inventory.getAllParts());
        }
    }

    /** event handler for the Product Search field
     * the user input in the searchbar is used to filter table items
     * if no products are found, an error is thrown
     * */
    @FXML
    void onAction_product_search(ActionEvent event) {
        tempProductList.clear(); // clear temp products list
        main_products_srch_error_lbl.setText(""); // reset error label
        String userInputProducts = main_products_searchbar.getText();

        // return matches for id or name
        for (Product product : Inventory.getAllProducts()) {
            if (product.getName().contains(userInputProducts) || String.valueOf(product.getId()).contains(userInputProducts)) {
                tempProductList.add(product);
            }
        }
        main_products_tbl.setItems(tempProductList); // filter main table items
        if (tempProductList.size() == 0) { // no products found
            main_products_srch_error_lbl.setText("No products found.");
        }
        if (tempProductList.size() == 1) { // highlight product if filtered to 1
            main_products_tbl.getSelectionModel().select(0);
        }
    }

    /** custom exception for deleting Product with Associated Parts*/
    static class AssociatedPartsException extends Exception {
        public AssociatedPartsException() {}
    }
}
