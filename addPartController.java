package sigvertsen.c482.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sigvertsen.c482.main;
import sigvertsen.c482.model.InHouse;
import sigvertsen.c482.model.Inventory;
import sigvertsen.c482.model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** This class is the controller for the Add Part page*/
public class addPartController implements Initializable {

    Stage stage;
    Parent scene;

    /** initialize method for the Add Part page controller
     * the InHouse radio button is selected by default on open of page
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPart_inHouse_radio.setSelected(true);
    }

    @FXML
    private TextField addPart_partName_field;

    @FXML
    private TextField addPart_inventory_field;

    @FXML
    private TextField addPart_price_field;

    @FXML
    private TextField addPart_max_field;

    @FXML
    private TextField addPart_min_field;

    @FXML
    private TextField addPart_machineId_companyName_field;

    @FXML
    private Label addPart_machineId_companyName_lbl;

    @FXML
    private RadioButton addPart_inHouse_radio;

    @FXML
    private Label addPart_input_error_lbl;

    /** event handler for InHouse radio button
     * when the InHouse radio button is selected, Company Name label is set to Machine ID
     * */
    @FXML
    void onAction_inHouse_mode(ActionEvent event) {
        addPart_machineId_companyName_lbl.setText("Machine ID");
    }

    /** event handler for Outsourced radio button
     * when the Outsourced radio button is selected, Machine ID label is set to Company Name
     * */
    @FXML
    void onAction_outSourced_mode(ActionEvent event) {
        addPart_machineId_companyName_lbl.setText("Company Name");
    }

    /** event handler for Save button
     * input from the text fields are used to create a Part object of either InHouse or Outsourced type
     * Exceptions are raised for MinMax errors and Inventory errors
     * */
    @FXML
    void onAction_save_fields(ActionEvent event) throws IOException {
        try {
//            int id = Integer.parseInt(addPart_part_id_field.getText());
            String name = addPart_partName_field.getText();
            int stock = Integer.parseInt(addPart_inventory_field.getText());
            double price = Double.parseDouble(addPart_price_field.getText());
            int max = Integer.parseInt(addPart_max_field.getText());
            int min = Integer.parseInt(addPart_min_field.getText());

            // MinMax error
            if (max < min) {
                throw new MinMaxException();
            }

            // Inventory error
            if (stock < min || stock > max) {
                throw new InventoryException();
            }

            // if inhouse radio selected, create inhouse part
            if (addPart_inHouse_radio.isSelected()) {
                int machineId = Integer.parseInt(addPart_machineId_companyName_field.getText());
                Inventory.addPart(new InHouse(main.getPart_id(), name, price, stock, min, max, machineId));
            }
            // if outsourced radio selected, create outsourced part
            else {
                String companyName = addPart_machineId_companyName_field.getText();
                Inventory.addPart(new Outsourced(main.getPart_id(), name, price, stock, min, max, companyName));
            }

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NumberFormatException e) { // invalid inputs in fields
            addPart_input_error_lbl.setText("Please enter valid inputs.");
        }
        catch(MinMaxException e) { // min is less than max
            addPart_input_error_lbl.setText("MAX must be greater than MIN.");
        }
        catch(InventoryException e) { // inventory not between min and max
            addPart_input_error_lbl.setText("INVENTORY must be between MIN and MAX.");
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

    /** custom exception for MinMax error*/
    static class MinMaxException extends Exception {
        public MinMaxException() {}
    }

    /** customer exception for Inventory level error*/
    static class InventoryException extends Exception {
        public InventoryException() {}
    }
}
