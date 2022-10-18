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
import sigvertsen.c482.model.InHouse;
import sigvertsen.c482.model.Inventory;
import sigvertsen.c482.model.Outsourced;
import sigvertsen.c482.model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** This class is the controller for the Modify Part page*/
public class modifyPartController implements Initializable {

    Part selectedRow = mainController.getPartHandOff();
    Part selectedObject = Inventory.lookupPart(selectedRow.getId());
    Stage stage;
    Parent scene;

    /** initialize method for the Modify Part page
     * The radio buttons are set to match the Part to be modified
     * All fields are set to match the Part to be modified
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyPart_id_field.setText(String.valueOf(selectedObject.getId()));
        modifyPart_partName_field.setText(selectedObject.getName());
        modifyPart_inventory_field.setText(String.valueOf(selectedObject.getStock()));
        modifyPart_price_field.setText(String.valueOf(selectedObject.getPrice()));
        modifyPart_max_field.setText(String.valueOf(selectedObject.getMax()));
        modifyPart_min_field.setText(String.valueOf(selectedObject.getMin()));

        if (selectedObject instanceof InHouse) {
            modifyPart_inhouse_radio.setSelected(true);
            modifyPart_machineId_companyName_lbl.setText("Machine ID");
            modifyPart_machineId_companyName_field.setText(String.valueOf(((InHouse) selectedObject).getMachineId()));
        }
        if (selectedObject instanceof Outsourced) {
            modifyPart_outsourced_radio.setSelected(true);
            modifyPart_machineId_companyName_lbl.setText("Company Name");
            modifyPart_machineId_companyName_field.setText(((Outsourced) selectedObject).getCompanyName());
        }
    }

    @FXML
    private TextField modifyPart_id_field;

    @FXML
    private TextField modifyPart_partName_field;

    @FXML
    private TextField modifyPart_inventory_field;

    @FXML
    private TextField modifyPart_price_field;

    @FXML
    private TextField modifyPart_max_field;

    @FXML
    private TextField modifyPart_min_field;

    @FXML
    private TextField modifyPart_machineId_companyName_field;

    @FXML
    private RadioButton modifyPart_inhouse_radio;

    @FXML
    private RadioButton modifyPart_outsourced_radio;

    @FXML
    private Label modifyPart_machineId_companyName_lbl;

    @FXML
    private Label modifyPart_input_error_lbl;

    /** event handler for InHouse radio button
     * when the InHouse radio button is selected, Company Name label is set to Machine ID
     * */
    @FXML
    void onAction_inHouse_mode(ActionEvent event) {
        modifyPart_machineId_companyName_lbl.setText("Machine ID");
    }

    /** event handler for Outsourced radio button
     * when the Outsourced radio button is selected, Machine ID label is set to Company Name
     * */
    @FXML
    void onAction_outSourced_mode(ActionEvent event) {
        modifyPart_machineId_companyName_lbl.setText("Company Name");
    }

    /** event handler for Save button
     * Input from the text fields are used to create a Part object that will replace the Part to be modified
     * Exceptions are raised for MinMax errors and Inventory errors
     * */
    @FXML
    void onAction_save(ActionEvent event) throws IOException {
        try {
            int indexSelectedRow = Inventory.getAllParts().indexOf(selectedObject); // index of selected object

            int id = Integer.parseInt(modifyPart_id_field.getText());
            String name = modifyPart_partName_field.getText();
            int stock = Integer.parseInt(modifyPart_inventory_field.getText());
            double price = Double.parseDouble(modifyPart_price_field.getText());
            int max = Integer.parseInt(modifyPart_max_field.getText());
            int min = Integer.parseInt(modifyPart_min_field.getText());
            int m = -1;
            String c = "Default Name";

            // MinMax error
            if (max < min) {
                throw new MinMaxException();
            }

            // Inventory error
            if (stock < min || stock > max) {
                throw new InventoryException();
            }

            if (modifyPart_inhouse_radio.isSelected()) {
                // create new InHouse object
                InHouse newInHouse = new InHouse(id, name, price, stock, min, max, m);
                int machineId = Integer.parseInt(modifyPart_machineId_companyName_field.getText()); // set machine id
                if (String.valueOf(machineId).length() > 0) { // set machine if field not empty
                    newInHouse.setMachineId(machineId);
                }
                Inventory.updatePart(indexSelectedRow, newInHouse); // update part
            }
            if (modifyPart_outsourced_radio.isSelected()) {
                // create new Outsourced object
                Outsourced newOutsourced = new Outsourced(id, name, price, stock, min, max, c);
                String companyName = modifyPart_machineId_companyName_field.getText();
                if (companyName.length() > 0) { // set company name if field not empty
                    newOutsourced.setCompanyName(companyName);
                }
                Inventory.updatePart(indexSelectedRow, newOutsourced); // update part
            }

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NumberFormatException e) { // invalid inputs
            modifyPart_input_error_lbl.setText("Please enter valid inputs.");
        }
        catch(MinMaxException e) { // min is greater than max
            modifyPart_input_error_lbl.setText("MAX must be greater than MIN.");
        }
        catch(InventoryException e) { // inventory not between min and max
            modifyPart_input_error_lbl.setText("INVENTORY must be between MIN and MAX.");
        }

    }

    /** event handler for Cancel button
     * sends the user to the Main page
     * */
    @FXML
    void onAction_cancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sigvertsen/c482/view/main.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();

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
