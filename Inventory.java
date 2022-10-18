package sigvertsen.c482.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** class Inventory.java*/
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * a method to add a new Part to allParts list
     * @param newPart is the new Part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    } // add part to inventory

    /**
     * a method to add a new Part to allProduct list
     * @param newProduct is the new Product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    } // add product to inventory

    /**
     * a method to lookup a Part object
     * @param partId is the Part ID to lookup
     * @return resultPart is the Part object to find
     */
    public static Part lookupPart(int partId) {
        // find part object from part id
        Part resultPart = null;
        for (Part part : allParts) {
            if (part.getId() == partId) {
                resultPart = part;
            }
        }
        return resultPart;
    }

    /**
     * a method to lookup a Product object
     * @param productId is the Part ID to lookup
     * @return resultProd is the Part object to find
     */
    public static Product lookupProduct(int productId) {
        // find product object from product id
        Product resultProd = null;
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                resultProd = product;
            }
        }
        return resultProd;
    }

    /**
     * a method to lookup a Part
     * @param partName is the Part Name to lookup
     * @return tempPartsList is the resultant list of matching parts
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> tempPartsList = FXCollections.observableArrayList();

        // search parts inventory for part id or part name
        for (Part part : allParts) {
            if (part.getName().contains(partName) || String.valueOf(part.getId()).contains(partName)) {
                tempPartsList.add(part);
            }
        }
        return tempPartsList;
    }

    /**
     * a method to update a part
     * @param index is the index of the Part to update
     * @param selectedPart is the new Part to add
     */
    public static void updatePart(int index, Part selectedPart) {

        allParts.remove(index); // remove part at index from inventory
        allParts.add(selectedPart); // add selected part to inventory
    }

    /**
     * a method to update a product
     * @param index is the index of the Product to update
     * @param newProduct is the new Product to add
     */
    public static void updateProduct(int index, Product newProduct) {

        // delete all associated parts
        for (Part associatedPart : allProducts.get(index).getAllAssociatedParts()) {
            if (newProduct.deleteAssociatedPart(associatedPart)) {
                newProduct.deleteAssociatedPart(associatedPart);
            }
        }
        allProducts.remove(index); // remove part at input index from inventory
        allProducts.add(newProduct); // add new product to inventory
    }

    /**
     * a method to delete a part
     * @param selectedPart is the Part object to delete
     */
    public static boolean deletePart(Part selectedPart) {
        return Inventory.getAllParts().remove(selectedPart); // remove selected part from inventory
    }

    /**
     * a method to delete a product
     * @param selectedProduct is the Product object to delete
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return Inventory.getAllProducts().remove(selectedProduct); // remove selected product from inventory
    }

    /**
     * a method get all parts
     * @return allParts returns allParts list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts; // show all parts
    }

    /**
     * a method to get all products
     * @return allProducts returns allProducts list
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts; // show all products
    }

}
