package sigvertsen.c482.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * a method to get the ID of a Product Object
     * @return id will return the ID of the Product object
     */
    public int getId() {
        return id;
    }

    /**
     * a method to set the ID of a Product Object
     * @param id is the ID to set to the Product object
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * a method to get the Name of a Product Object
     * @return name will return the Name of the Product object
     */
    public String getName() {
        return name;
    }

    /**
     * a method to set the Name of a Product Object
     * @param name is the Name to set to the Product object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * a method to get the Price of a Product Object
     * @return price will return the Price of the Product object
     */
    public double getPrice() {
        return price;
    }

    /**
     * a method to set the Price of a Product Object
     * @param price is the Price to set to the Product object
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * a method to get the stock of a Product Object
     * @return stock will return the Stock of the Product object
     */
    public int getStock() {
        return stock;
    }

    /**
     * a method to set the Stock of a Product Object
     * @param stock is the Stock to set to the Product object
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * a method to get the Min of a Product Object
     * @return min will return the Min of the Product object
     */
    public int getMin() {
        return min;
    }

    /**
     * a method to set the Min of a Product Object
     * @param min is the Min to set to the Product object
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * a method to get the Max of a Product Object
     * @return max will return the Max of the Product object
     */
    public int getMax() {
        return max;
    }

    /**
     * a method to set the Max of a Product Object
     * @param max is the Max to set to the Product object
     */
    public void setMax(int max) {
        this.max = max;
    }

    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    } // add associated parts to product object

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart); // delete associated parts from product object
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    } // get all associated parts
}
