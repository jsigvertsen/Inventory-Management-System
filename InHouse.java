package sigvertsen.c482.model;

public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * a method to set a Machine ID to an InHouse object
     * @param machineId is the Machine ID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * a method to get a Machine ID of an InHouse object
     * @return machineId will return the Machine ID set to the InHouse object
     */
    public int getMachineId() {
        return machineId;
    }
}
